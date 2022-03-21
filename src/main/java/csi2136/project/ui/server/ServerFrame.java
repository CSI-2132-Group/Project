package csi2136.project.ui.server;

import csi2136.project.Server;
import csi2136.project.ui.BaseFrame;

import javax.swing.*;

public class ServerFrame extends BaseFrame {

	public ServerFrame(Server server) {
		this.initialize();

		this.panels.add(new MainScreen(this, (url, user, password, port) -> server.createConnection(port)));

		this.setContentPane(this.panels.get(this.panelId));
	}

	private void initialize() {
		this.setTitle("Server");
		this.setSize(960, 540);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setResizable(false);
	}

}
