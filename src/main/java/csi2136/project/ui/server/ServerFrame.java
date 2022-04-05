package csi2136.project.ui.server;

import csi2136.project.Server;
import csi2136.project.core.Database;
import csi2136.project.ui.BaseFrame;

import javax.swing.*;

public class ServerFrame extends BaseFrame {

	public ServerFrame(Server server) {
		this.initialize();

		this.panels.add(new MainScreen(this, (url, user, password, port) -> {
			server.createConnection(port);

			server.getDatabase().connect(url, user, password, e -> {
				server.disconnect("Cannot connect to the database");
				e.printStackTrace();
			});

			if(server.getDatabase().isConnected()) {
				DatabaseScreen screen = this.getPanel(this.panelId + 1, DatabaseScreen.class);
				screen.use.setEnabled(true);
				screen.create.setEnabled(true);
				this.movePanel(1);
			}
		}));

		this.panels.add(new DatabaseScreen(this, (screen, name) -> {
			server.getDatabase().send(String.format("USE %s;", name), "SET FOREIGN_KEY_CHECKS=0;");

			if(server.getDatabase().isConnected()) {
				screen.use.setEnabled(false);
				screen.create.setEnabled(false);
			}
		}, (screen, name) -> {
			server.getDatabase().send(
				"SET FOREIGN_KEY_CHECKS=0;",
				String.format("DROP DATABASE IF EXISTS %s;", name),
				String.format("CREATE DATABASE %s;", name),
				String.format("USE %s;", name));

			Database.initialize(server.getDatabase());

			if(server.getDatabase().isConnected()) {
				screen.use.setEnabled(false);
				screen.create.setEnabled(false);
			}
		}));

		this.setPanel(0);
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
