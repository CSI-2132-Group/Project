package csi2136.project.ui.client;

import csi2136.project.ui.Utils;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JPanel {

	public JLabel title;
	public JTextField username;
	public JTextField password;
	public JButton back,confirm;

	protected Runnable onBack;
	protected ConfirmAction onConfirm;
	public LoginScreen(JFrame frame, Runnable onBack, ConfirmAction onConfirm) {
		this.onBack = onBack;
		this.onConfirm = onConfirm;
		this.addTitle(frame.getWidth(), frame.getHeight());
		this.addLoginFields(frame.getWidth(), frame.getHeight());
		this.addStateButtons(frame.getWidth(), frame.getHeight());
		this.setLayout(null);
	}

	protected void addTitle(int width, int height) {
		this.title = new JLabel("Log In");
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

	protected void addLoginFields(int width, int height) {
		this.username = new JTextField();
		this.username.setBounds(width / 2 - 100, height / 3, 200, 40);
		Utils.setHint(this.username, "username...");
		this.add(this.username);

		this.password = new JTextField();
		this.password.setBounds(width / 2 - 100, height / 3 + 60, 200, 40);
		Utils.setHint(this.password, "password...");
		this.add(this.password);
	}

	private void addStateButtons(int width, int height) {
		this.back = new JButton("< Return");
		this.back.setBounds(15, height - 90, 100, 40);
		this.back.addActionListener(e -> this.onBack.run());
		this.add(this.back);

		this.confirm = new JButton("Continue >");
		this.confirm.setBounds(width - 130, height - 90, 100, 40);
		this.confirm.addActionListener(e -> this.onConfirm.onConfirm(this.username.getText(), this.password.getText()));
		this.add(this.confirm);
	}

	@FunctionalInterface
	public interface ConfirmAction {
		void onConfirm(String username, String password);
	}

}
