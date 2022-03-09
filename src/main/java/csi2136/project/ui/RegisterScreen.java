package csi2136.project.ui;

import csi2136.project.AccountType;

import javax.swing.*;
import java.awt.*;

public class RegisterScreen extends JPanel {

	protected JLabel title;
	protected JTextField username;
	protected JTextField password;
	protected JTextField ssn;
	protected JComboBox<AccountType> type;
	protected JButton back, confirm;

	protected RegisterScreen(int width, int height) {
		this.addTitle(width, height);
		this.addRegisterFields(width, height);
		this.addStateButtons(width, height);
		this.setLayout(null);
	}

	protected void addTitle(int width, int height) {
		this.title = new JLabel("Register");
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

	protected void addRegisterFields(int width, int height) {
		this.username = new JTextField();
		this.username.setBounds(width / 2 - 100, height / 3 - 20, 200, 40);
		SwingUtils.setHint(this.username, "username...");
		this.add(this.username);

		this.password = new JTextField();
		this.password.setBounds(width / 2 - 100, height / 3 + 40, 200, 40);
		SwingUtils.setHint(this.password, "password...");
		this.add(this.password);

		this.ssn = new JTextField();
		this.ssn.setBounds(width / 2 - 100, height / 3 + 100, 200, 40);
		SwingUtils.setHint(this.ssn, "ssn (leave blank for none)...");
		this.add(this.ssn);

		this.type = new JComboBox<>(AccountType.values());
		this.type.setBounds(width / 2 - 100, height / 3 + 160, 200, 40);
		this.add(this.type);
	}

	private void addStateButtons(int width, int height) {
		this.back = new JButton("< Return");
		this.back.setBounds(15, height - 90, 100, 40);
		this.add(this.back);

		this.confirm = new JButton("Continue >");
		this.confirm.setBounds(width - 130, height - 90, 100, 40);
		this.add(this.confirm);
	}
	
}
