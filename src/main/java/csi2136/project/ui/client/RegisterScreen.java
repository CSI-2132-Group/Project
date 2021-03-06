package csi2136.project.ui.client;

import csi2136.project.AccountType;
import csi2136.project.ui.Utils;

import javax.swing.*;
import java.awt.*;

public class RegisterScreen extends JPanel {

	public JLabel title;
	public JTextField username;
	public JTextField password;
	public JTextField insurance;
	public JComboBox<AccountType> type;
	public JLabel error;
	public JButton back, confirm;

	protected Runnable onBack;
	protected ConfirmAction onConfirm;

	public RegisterScreen(JFrame frame, Runnable onBack, ConfirmAction onConfirm) {
		int width = frame.getWidth();
		int height = frame.getHeight();
		this.onBack = onBack;
		this.onConfirm = onConfirm;
		this.addTitle(width, height);
		this.addRegisterFields(width, height);
		this.addErrorText(width, height);
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
		Utils.setHint(this.username, "username...");
		this.add(this.username);

		this.password = new JTextField();
		this.password.setBounds(width / 2 - 100, height / 3 + 40, 200, 40);
		Utils.setHint(this.password, "password...");
		this.add(this.password);

		this.insurance = new JTextField();
		this.insurance.setBounds(width / 2 - 100, height / 3 + 100, 200, 40);
		Utils.setHint(this.insurance, "insurance (leave blank for none)...");
		this.add(this.insurance);

		this.type = new JComboBox<>(AccountType.values());
		this.type.setBounds(width / 2 - 100, height / 3 + 160, 200, 40);
		this.add(this.type);
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
		this.error.setBounds(0, height / 3 + 220, width, 60);
		this.add(this.error);
	}

	private void addStateButtons(int width, int height) {
		this.back = new JButton("< Return");
		this.back.setBounds(15, height - 90, 100, 40);
		this.back.addActionListener(e -> this.onBack.run());
		this.add(this.back);

		this.confirm = new JButton("Continue >");
		this.confirm.setBounds(width - 130, height - 90, 100, 40);

		this.confirm.addActionListener(e -> this.onConfirm.onConfirm(
			this.username.getText(), this.password.getText(),
			this.insurance.getText().equals("insurance (leave blank for none)...") ? "" : this.insurance.getText(),
			(AccountType)this.type.getModel().getSelectedItem()));

		this.add(this.confirm);
	}

	@FunctionalInterface
	public interface ConfirmAction {
		void onConfirm(String username, String password, String insurance, AccountType type);
	}
	
}
