package csi2136.project.ui.client;

import csi2136.project.net.util.NetAddress;
import csi2136.project.ui.Utils;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JPanel {

	public JLabel title;
	public JTextField ipAddress;
	public JTextField port;
	public JLabel error;
	public JButton connect;

	protected ConnectAction action;

	public MainScreen(JFrame frame, ConnectAction onConnect) {
		this.action = onConnect;
		this.addTitle(frame.getWidth(), frame.getHeight());
		this.addConnectFields(frame.getWidth(), frame.getHeight());
		this.addErrorText(frame.getWidth(), frame.getHeight());
		this.addStateButtons(frame.getWidth(), frame.getHeight());
		this.setLayout(null);
		this.validateInput();
	}

	protected void addTitle(int width, int height) {
		this.title = new JLabel("Connect to Server");
		this.title.setFont(new Font(this.title.getFont().getName(), Font.PLAIN, 58));
		this.title.setBackground(new Color(0, 0, 0, 0));
		this.title.setFocusable(false);
		this.title.setOpaque(true);
		this.title.setForeground(Color.WHITE);
		this.title.setHorizontalAlignment(SwingConstants.CENTER);
		this.title.setVerticalAlignment(SwingConstants.CENTER);
		this.title.setBounds(0, 0, width, height / 3);
		this.add(this.title);
	}

	protected void addConnectFields(int width, int height) {
		this.ipAddress = new JTextField();
		this.ipAddress.setBounds(width / 2 - 100, height / 3, 200, 40);
		Utils.setHint(this.ipAddress, "ip address...");
		this.ipAddress.setText("localhost");
		this.ipAddress.addCaretListener(e -> this.validateInput());
		this.add(this.ipAddress);

		this.port = new JTextField();
		this.port.setBounds(width / 2 - 100, height / 3 + 60, 200, 40);
		Utils.setHint(this.port, "port...");
		this.port.setText("8888");
		this.port.addCaretListener(e -> this.validateInput());
		this.add(this.port);
	}

	private void addErrorText(int width, int height) {
		this.error = new JLabel("");
		this.error.setFont(new Font(this.title.getFont().getName(), Font.PLAIN, 20));
		this.error.setBackground(new Color(0, 0, 0, 0));
		this.error.setFocusable(false);
		this.error.setOpaque(true);
		this.error.setForeground(Color.RED);
		this.error.setHorizontalAlignment(SwingConstants.CENTER);
		this.error.setVerticalAlignment(SwingConstants.CENTER);
		this.error.setBounds(0, height / 3 + 120, width, 60);
		this.add(this.error);
	}

	protected void addStateButtons(int width, int height) {
		this.connect = new JButton("Connect >");
		this.connect.setBounds(width - 130, height - 90, 100, 40);

		this.connect.addActionListener(e -> {
			this.action.onConnect(this, new NetAddress(this.ipAddress.getText(), Integer.parseInt(this.port.getText())));
		});

		this.add(this.connect);
	}

	protected void validateInput() {
		try {
			Integer.parseInt(this.port.getText().trim());
			this.connect.setEnabled(true);
		} catch(NumberFormatException e) {
			this.connect.setEnabled(false);
		}
	}

	@FunctionalInterface
	public interface ConnectAction {
		void onConnect(MainScreen screen, NetAddress address);
	}

}
