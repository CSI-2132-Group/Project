package csi2136.project.net.listener;

import csi2136.project.net.context.ClientContext;
import csi2136.project.net.context.Context;
import csi2136.project.net.context.ServerContext;
import csi2136.project.net.packet.buffer.ByteBuffer;
import csi2136.project.net.packet.message.C2SMessage;
import csi2136.project.net.packet.message.Packet;
import csi2136.project.net.packet.message.S2CMessage;
import csi2136.project.net.util.NetAddress;
import csi2136.project.net.util.PacketRegistry;
import csi2136.project.net.util.Side;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class Listener extends Thread {

    private static int ID = 0;

    private Socket socket;
    private NetAddress connectionAddress;
    private int listenerId;

    private DataInputStream socketInputStream;
    private DataOutputStream socketOutputStream;
    private boolean shouldDisconnect;

    private Side side = Side.CLIENT;
    private ServerListener serverListener;

    private List<Consumer<Listener>> connectionEstablishedConsumers = new ArrayList<>();
    private List<BiConsumer<Listener, String>> connectionClosedConsumers = new ArrayList<>();
    private List<Consumer<Context>> contextCreatedConsumers = new ArrayList<>();
    private List<BooleanSupplier> scheduledTasks = new ArrayList<>();

    public Listener() {

    }

    public Listener connect(NetAddress address) {
        return this.connect(address.getIp(), address.getPort());
    }

    public Listener connect(Socket socket) {
        this.connectionAddress = new NetAddress(socket.getInetAddress().getHostAddress(), socket.getPort());

        try {
            this.socket = socket;
            this.socketInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.socketOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch(IOException e) {
            this.disconnect();
            return this;
        }

        this.listenerId = ID++;
        this.setName("[Net] Listener " + this.getListenerId());
        return this;
    }

    public Listener connect(String ip, int port) {
        this.connectionAddress = new NetAddress(ip, port);

        try {
            this.socket = new Socket(ip, port);
            this.socketInputStream = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            this.socketOutputStream = new DataOutputStream(this.socket.getOutputStream());
        } catch(IOException e) {
            this.disconnect(e.getMessage());
            return this;
        }

        this.listenerId = ID++;
        this.setName("[Net] Listener " + this.getListenerId());
        return this;
    }

    public int getListenerId() {
        return this.listenerId;
    }

    public NetAddress getAddress() {
        return this.connectionAddress;
    }

    public void setServer(ServerListener serverListener) {
        this.side = Side.SERVER;
        this.serverListener = serverListener;
    }

    private Context getContext() {
        if(this.side == Side.CLIENT) {
            ClientContext clientContext = new ClientContext();
            clientContext.listener = this;

            this.contextCreatedConsumers.forEach(contextConsumer -> {
                contextConsumer.accept(clientContext);
            });
            return clientContext;
        } else if(this.side == Side.SERVER) {
            ServerContext serverContext = new ServerContext();
            serverContext.serverListener = this.serverListener;
            serverContext.listener = this;

            this.contextCreatedConsumers.forEach(contextConsumer -> {
                contextConsumer.accept(serverContext);
            });

            return serverContext;
        }

        return null;
    }

    public Listener onConnectionEstablished(Consumer<Listener> listenerConsumer) {
        this.connectionEstablishedConsumers.add(listenerConsumer);
        return this;
    }

    public Listener onConnectionClosed(BiConsumer<Listener, String> listenerConsumer) {
        this.connectionClosedConsumers.add(listenerConsumer);
        return this;
    }

    public Listener onContextCreated(Consumer<Context> contextConsumer) {
        this.contextCreatedConsumers.add(contextConsumer);
        return this;
    }

    public void schedule(Runnable task) {
        this.scheduledTasks.add(() -> {
            task.run();
            return true;
        });
    }

    public void schedule(BooleanSupplier task) {
        this.scheduledTasks.add(task);
    }

    private void listen() {
        if(!this.isConnected())return;

        this.connectionEstablishedConsumers.forEach(listenerConsumer -> listenerConsumer.accept(this));

        while(this.isConnected() && !this.socket.isClosed()) {
            int packetId = this.readPacketId();
            if(packetId == -1)continue;

            try {
                Packet returnedPacket = this.onPacketReceived(packetId, this.socketInputStream, this.getContext());
                if (returnedPacket != null) this.sendPacket(returnedPacket);
            } catch(Exception e) {
                e.printStackTrace();
            }

            this.scheduledTasks.removeIf(BooleanSupplier::getAsBoolean);
        }

        this.disconnect("Connection was closed by the host");
    }

    private int readPacketId() {
        try {return this.socketInputStream.readUnsignedByte();}
        catch(IOException e) {this.disconnect("Connection was closed by the host");}
        return -1;
    }

    public Packet onPacketReceived(int packetId, DataInputStream in, Context context) {
        Class<Packet> packetClass = PacketRegistry.getPacketClass(packetId);
        if(packetClass == null)return null;

        Packet packet;

        try {
            packet = packetClass.newInstance().read(new ByteBuffer(in));
        } catch(InstantiationException | IllegalAccessException | IOException e) {
            e.printStackTrace();
            return null;
        }

        Packet returnPacket = null;

        if(packet instanceof C2SMessage && context instanceof ServerContext) {
            returnPacket = ((C2SMessage)packet).onPacketReceived((ServerContext)context);
        } else if(packet instanceof S2CMessage && context instanceof ClientContext) {
            returnPacket = ((S2CMessage)packet).onPacketReceived((ClientContext)context);
        }

        return returnPacket;
    }

    public boolean isConnected() {
        return !this.shouldDisconnect;
    }

    public synchronized void disconnect() {
        this.disconnect("");
    }

    public synchronized void disconnect(String cause) {
        if(!this.isConnected())return;

        this.shouldDisconnect = true;

        try {this.socketInputStream.close();}
        catch(Exception e) {;}

        try {this.socketOutputStream.close();}
        catch(Exception e) {;}

        try {this.socket.close();}
        catch(Exception e) {;}

        this.connectionClosedConsumers.forEach(listenerConsumer -> listenerConsumer.accept(this, cause));
    }

    public void sendPacket(Packet packet) {
        if(this.shouldDisconnect)return;
        if(!packet.canSendFrom(this.side))return;

        try {
            this.socketOutputStream.writeByte(PacketRegistry.getPacketId(packet));
            packet.write(new ByteBuffer(this.socketOutputStream));
        } catch(IOException e) {
            this.disconnect("Connection was closed by the host");
        }
    }

    @Override
    public void run() {
        this.listen();
    }

}
