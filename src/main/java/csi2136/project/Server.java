package csi2136.project;

import csi2136.project.init.InitNetwork;
import csi2136.project.init.InitUI;
import csi2136.project.net.listener.ServerListener;
import csi2136.project.ui.server.ServerFrame;

public class Server {

	protected ServerFrame frame;
	protected ServerListener listener;

	public Server() {
		this.frame = new ServerFrame(this);
	}

	public void start() {
		this.frame.setVisible(true);
	}

	public void createConnection(int port) {
		this.listener = new ServerListener(port);
		this.listener.onConnectionEstablished(l -> System.out.println("Established connection with client " + l.getAddress() + "."));
		this.listener.start();
	}

	public boolean isConnected() {
		return this.listener != null && this.listener.isConnected();
	}

	public static void main(String[] args) {
		InitNetwork.registerPackets();
		InitUI.registerLAF();

		Server server = new Server();
		server.start();
	}

}
