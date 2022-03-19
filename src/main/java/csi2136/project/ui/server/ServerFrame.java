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

	/*
	public static JFrame create() {
		JFrame frame = new JFrame();
		//Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLayout(null);
		frame.setTitle("Server");
		frame.setSize(960, 540);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		int width = 960, height = 540, centerX = width / 2, centerY = height / 2;
		frame.setContentPane(new RegisterScreen(width, height));

		JLabel label = new JLabel("MySQL Server");
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 58));
		label.setBackground(new Color(0, 0, 0, 0));
		label.setFocusable(false);
		label.setOpaque(true);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, width, height / 3);
		frame.add(label);

		JTextField url = new JTextField("jdbc:mysql://localhost/");
		url.setBounds(centerX - 200, height / 3, 400, 40);
		frame.add(url);

		JTextField user = new JTextField("root");
		user.setBounds(centerX - 200, height / 3 + 60, 190, 40);
		frame.add(user);

		JTextField password = new JTextField("password");
		password.setBounds(centerX + 10, height / 3 + 60, 190, 40);
		frame.add(password);

		JButton confirm = new JButton("Continue >");
		confirm.setBounds(width - 130, height - 90, 100, 40);
		confirm.setMargin(new Insets(0,0,0,0));
		frame.add(confirm);

		JLabel result = new JLabel("");
		result.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
		result.setBackground(new Color(0, 0, 0, 0));
		result.setFocusable(false);
		result.setOpaque(true);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setVerticalAlignment(SwingConstants.CENTER);
		result.setBounds(centerX - 400, height / 3 + 120, 800, 40);
		frame.add(result);

		confirm.addActionListener(e -> {
			try {
				Connection con = DriverManager.getConnection(url.getText().trim(), user.getText().trim(), password.getText().trim());
				result.setForeground(Color.GREEN);
				result.setText("connection established");
				con.close();
			} catch(SQLException ex) {
				result.setForeground(Color.RED);
				result.setText(ex.getMessage());
				ex.printStackTrace();
			}

			frame.repaint();
		});

		return frame;
	}*/

}
