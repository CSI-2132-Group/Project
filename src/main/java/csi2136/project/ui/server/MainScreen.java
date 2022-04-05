package csi2136.project.ui.server;

import csi2136.project.ui.Utils;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JPanel {

	public JLabel title;
	public JTextField url;
	public JTextField username;
	public JTextField password;
	public JTextField port;
	public JLabel error;
	public JButton connect;

	protected ConnectAction onConnect;

	public MainScreen(JFrame frame, ConnectAction onConnect) {
		this.onConnect = onConnect;
		this.addTitle(frame.getWidth(), frame.getHeight());
		this.addServerFields(frame.getWidth(), frame.getHeight());
		this.addErrorText(frame.getWidth(), frame.getHeight());
		this.addStateButtons(frame.getWidth(), frame.getHeight());
		this.setLayout(null);
		this.validateInput();
	}

	protected void addTitle(int width, int height) {
		this.title = new JLabel("Create Server");
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

	protected void addServerFields(int width, int height) {
		this.url = new JTextField("jdbc:mysql://localhost/");
		this.url.setBounds(width / 2 - 200, height / 3, 400, 40);
		Utils.setHint(this.url, "db url...");
		this.add(this.url);

		this.username = new JTextField("root");
		this.username.setBounds(width / 2 - 200, height / 3 + 60, 190, 40);
		Utils.setHint(this.username, "db username...");
		this.add(this.username);

		this.password = new JTextField("");
		this.password.setBounds(width / 2 + 10, height / 3 + 60, 190, 40);
		Utils.setHint(this.password, "db password...");
		this.add(this.password);

		this.port = new JTextField("8888");
		this.port.setBounds(width / 2 - 200, height / 3 + 120, 400, 40);
		Utils.setHint(this.port, "host port...");
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
		this.error.setBounds(0, height / 3 + 160, width, 60);
		this.add(this.error);
	}

	private void addStateButtons(int width, int height) {
		this.connect = new JButton("Connect >");
		this.connect.setBounds(width - 130, height - 90, 100, 40);
		this.connect.addActionListener(e -> this.onConnect.onConnect(this.url.getText(), this.username.getText(),
			this.password.getText(), Integer.parseInt(this.port.getText())));
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
		void onConnect(String url, String user, String password, int port);
	}

}
