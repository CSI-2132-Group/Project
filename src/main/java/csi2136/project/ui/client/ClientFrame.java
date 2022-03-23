package csi2136.project.ui.client;

import csi2136.project.Client;
import csi2136.project.net.packet.PacketC2SLogin;
import csi2136.project.ui.BaseFrame;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends BaseFrame {


	public ClientFrame(Client client) {
		this.initialize();

		this.panels.add(new MainScreen(this, (screen, address) -> {
			screen.error.setText("");
			screen.repaint();

			EventQueue.invokeLater(() -> {
				if(client.connectTo(address)) {
					this.movePanel(1);
				}
			});
		}));

		this.panels.add(new AuthScreen(this, () -> this.movePanel(1), () -> this.movePanel(2)));

		this.panels.add(new LoginScreen(this, () -> this.movePanel(-1), (username, password) -> {
			client.getListener().sendPacket(new PacketC2SLogin(username, password));
			//Move to the next screen
		}));

		this.panels.add(new RegisterScreen(this, () -> this.movePanel(-2), (username, password, ssn, type) -> {

		}));

		this.setContentPane(this.panels.get(this.panelId));
	}

	private void initialize() {
		this.setTitle("Client");
		this.setSize(960, 540);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setResizable(false);
	}

}
