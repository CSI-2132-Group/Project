package csi2136.project.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerFrame extends JFrame {

	public static ServerFrame create() {
		ServerFrame frame = new ServerFrame();
		//Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

		frame.setTitle("Server");
		frame.setSize(960, 540);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		frame.setContentPane(panel);

		int width = 960, height = 540, centerX = width / 2, centerY = height / 2;

		//================================================================================================//
		JLabel label = new JLabel("MySQL Server");
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 58));
		label.setBackground(new Color(0, 0, 0, 0));
		label.setFocusable(false);
		label.setOpaque(true);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, width, height / 3);
		panel.add(label);
		//================================================================================================//
		JTextField url = new JTextField("jdbc:mysql://localhost/");
		url.setBounds(centerX - 200, height / 3, 400, 40);
		panel.add(url);
		//================================================================================================//
		JTextField user = new JTextField("root");
		user.setBounds(centerX - 200, height / 3 + 60, 190, 40);
		panel.add(user);
		//================================================================================================//
		JTextField password = new JTextField("password");
		password.setBounds(centerX + 10, height / 3 + 60, 190, 40);
		panel.add(password);
		//================================================================================================//
		JButton confirm = new JButton("Continue >");
		confirm.setBounds(width - 130, height - 90, 100, 40);
		confirm.setMargin(new Insets(0,0,0,0));
		panel.add(confirm);
		//================================================================================================//
		JLabel result = new JLabel("");
		result.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
		result.setBackground(new Color(0, 0, 0, 0));
		result.setFocusable(false);
		result.setOpaque(true);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setVerticalAlignment(SwingConstants.CENTER);
		result.setBounds(centerX - 300, height / 3 + 120, 600, 40);
		panel.add(result);
		//================================================================================================//

		confirm.addActionListener(e -> {
			try {
				Connection con = DriverManager.getConnection(url.getText().trim(), user.getText().trim(), password.getText().trim());
				result.setForeground(Color.GREEN);
				result.setText("connection established");
				con.close();
			} catch(SQLException ex) {
				result.setForeground(Color.RED);
				result.setText(ex.getSQLState() + " : " + ex.getMessage());
			}
		});

		frame.setContentPane(panel);
		return frame;
	}

}
