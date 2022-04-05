package csi2136.project.ui.server;

import csi2136.project.ui.Utils;

import javax.swing.*;
import java.awt.*;

public class DatabaseScreen extends JPanel {

	public JLabel title;
	public JTextField name;
	public JButton use;
	public JButton create;

	protected UseAction onUse;
	protected CreateAction onCreate;

	public DatabaseScreen(JFrame frame, UseAction onUse, CreateAction onCreate) {
		this.onUse = onUse;
		this.onCreate = onCreate;
		this.addTitle(frame.getWidth(), frame.getHeight());
		this.addServerFields(frame.getWidth(), frame.getHeight());
		this.addStateButtons(frame.getWidth(), frame.getHeight());
		this.setLayout(null);
	}

	protected void addTitle(int width, int height) {
		this.title = new JLabel("Create Database");
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
		this.name = new JTextField("");
		this.name.setBounds(width / 2 - 200, height / 3, 400, 40);
		Utils.setHint(this.name, "db name...");
		this.add(this.name);
	}


	private void addStateButtons(int width, int height) {
		this.use = new JButton("Use >");
		this.use.setBounds(width - 250, height - 90, 100, 40);
		this.use.addActionListener(e -> this.onUse.onUse(this, this.name.getText()));
		this.add(this.use);

		this.create = new JButton("Create >");
		this.create.setBounds(width - 130, height - 90, 100, 40);
		this.create.addActionListener(e -> this.onCreate.onCreate(this, this.name.getText()));
		this.add(this.create);
	}

	@FunctionalInterface
	public interface UseAction {
		void onUse(DatabaseScreen screen, String name);
	}

	@FunctionalInterface
	public interface CreateAction {
		void onCreate(DatabaseScreen screen, String name);
	}

}
