package csi2136.project.ui.client;

import javax.swing.*;
import java.awt.*;

public class AuthScreen extends JPanel {

	public JLabel title;
	public JButton login;
	public JButton register;

	protected Runnable onLogin;
	protected Runnable onRegister;

	public AuthScreen(JFrame frame, Runnable onLogin, Runnable onRegister) {
		int width = frame.getWidth();
		int height = frame.getHeight();
		this.onLogin = onLogin;
		this.onRegister = onRegister;
		this.addTitle(width, height);
		this.addLoginFields(width, height);
		this.setLayout(null);
	}

	protected void addTitle(int width, int height) {
		this.title = new JLabel("Welcome");
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
		this.login = new JButton();
		this.login.setBounds(width / 2 - 100, height / 3, 200, 40);
		this.login.setText("Log In");
		this.login.addActionListener(e -> this.onLogin.run());
		this.add(this.login);

		this.register = new JButton();
		this.register.setBounds(width / 2 - 100, height / 3 + 60, 200, 40);
		this.register.setText("Register");
		this.register.addActionListener(e -> this.onRegister.run());
		this.add(this.register);
	}

}
