package csi2136.project;

import csi2136.project.init.InitNetwork;
import csi2136.project.init.InitUI;
import csi2136.project.net.context.ClientContext;
import csi2136.project.net.listener.Listener;
import csi2136.project.net.packet.PacketC2SHello;
import csi2136.project.net.util.NetAddress;
import csi2136.project.ui.client.ClientFrame;
import csi2136.project.ui.client.MainScreen;

public class Client {

	protected ClientFrame frame;
	protected Listener listener;

	public Client() {
		this.frame = new ClientFrame(this);
	}

	public ClientFrame getFrame() {
		return this.frame;
	}

	public Listener getListener() {
		return this.listener;
	}

	public void start() {
		this.frame.setVisible(true);
	}

	public boolean connectTo(NetAddress address) {
		if(this.listener != null && this.listener.isConnected()) {
			if(this.listener.getAddress().equals(address)) {
				return true;
			}

			this.listener.disconnect("Establishing new connection");
		}

		this.listener = new Listener()
			.onConnectionEstablished(l -> {
				l.sendPacket(new PacketC2SHello());
			})
			.onConnectionClosed((l, reason) -> {
				this.frame.getPanel(0, MainScreen.class).error.setText(reason);
				this.frame.setPanel(0);
				this.frame.repaint();
			}).connect(address)
			.onContextCreated(context -> {
				if(context instanceof ClientContext) {
					((ClientContext)context).client = this;
				}
			});

		this.listener.start();
		return this.isConnected();
	}

	public boolean isConnected() {
		return this.listener != null && this.listener.isConnected();
	}

	public static void main(String[] args) {
		InitNetwork.registerPackets();
		InitUI.registerLAF();

		Client client = new Client();
		client.start();
	}

}
