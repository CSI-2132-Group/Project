package csi2136.project;

import csi2136.project.core.Database;
import csi2136.project.init.InitNetwork;
import csi2136.project.init.InitUI;
import csi2136.project.net.context.ServerContext;
import csi2136.project.net.listener.ServerListener;
import csi2136.project.ui.server.MainScreen;
import csi2136.project.ui.server.ServerFrame;

import java.sql.SQLException;

public class Server {

	protected ServerFrame frame;
	protected ServerListener listener;
	protected Database database = new Database(this);

	public Server() {
		this.frame = new ServerFrame(this);
	}

	public ServerFrame getFrame() {
		return this.frame;
	}

	public ServerListener getListener() {
		return this.listener;
	}

	public Database getDatabase() {
		return this.database;
	}

	public void start() {
		this.frame.setVisible(true);
	}

	public void disconnect(String reason) {
		if(!this.isConnected()) return;
		this.listener.disconnect();
		this.frame.getPanel(0, MainScreen.class).error.setText(reason);
		this.frame.setPanel(0);
		this.frame.repaint();

		try {
			this.database.getConnection().close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void createConnection(int port) {
		this.listener = new ServerListener(port);

		this.listener.onConnectionEstablished(l -> {
			System.out.println("Established connection with client " + l.getAddress() + ".");
		});

		this.listener.onContextCreated(context -> {
			if(context instanceof ServerContext) {
				((ServerContext)context).server = this;
			}
		});

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
