package csi2136.project.ui.client.account.tab;

import csi2136.project.core.Employee;
import csi2136.project.core.User;
import csi2136.project.ui.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class UserComponent {

	private final UserPanel panel;
	private final User user;
	private final List<Employee> employees;
	private final boolean dark;
	private Runnable onDelete;
	private Runnable onEdit;

	public UserComponent(UserPanel panel, User user, List<Employee> employees, Runnable onDelete, Runnable onEdit, boolean dark) {
		this.panel = panel;
		this.user = user;
		this.employees = employees;
		this.onDelete = onDelete;
		this.onEdit = onEdit;
		this.dark = dark;
	}
	
	public int setEditable(int width, int height, int y) {
		JLabel username1 = this.createLabel("Username:", true);
		JLabel username2 = this.createLabel(this.user.username, false);
		username1.setBounds(50, y + 50, width / 2 - 120, height / 7);
		username2.setBounds(50, y + 50, width / 2 - 120, height / 7);
		username2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(username1);
		this.panel.add(username2);

		JLabel insurance1 = this.createLabel("Insurance:", true);
		JLabel insurance2 = this.createLabel(this.user.insurance, false);
		insurance1.setBounds(50, y + 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		insurance2.setBounds(50, y + 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		insurance2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(insurance1);
		this.panel.add(insurance2);

		//===================================================================================================//

		List<String> list = this.employees.stream().map(Employee::getFullName).collect(Collectors.toList());
		list.add(0, "N/A");

		JComboBox<String> employee = new JComboBox<>(list.toArray(new String[0]));

		if(this.user.employee == null) {
			employee.setSelectedIndex(0);
		} else {
			employee.setSelectedItem(this.user.employee.getFullName());
		}

		employee.setBounds(width / 2 + 40, y + 50, width / 2 - 120, height / 7);
		this.panel.add(employee);

		JButton save = new JButton("Save");
		save.setFont(new Font(save.getFont().getName(), Font.PLAIN, 18));
		save.setBounds(50, y + 60 + 2 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		this.panel.add(save);

		JButton delete = new JButton("Delete");
		delete.setFont(new Font(delete.getFont().getName(),Font.PLAIN, 18));
		delete.setBounds(width / 2 + 40 + (width / 2 - 120) / 2 + 5, y + 60 + 2 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		this.panel.add(delete);

		JComponent background = new JComponent() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Color c = this.getBackground();
				g.setColor(new Color((int)(c.getRed() * 0.9), (int)(c.getGreen() * 0.9), (int)(c.getBlue() * 0.9)));
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
		};

		background.setBounds(0, y, width, 230);
		if(dark) this.panel.add(background);

		save.addActionListener(e -> {
			boolean valid = true;

			if(employee.getSelectedIndex() == -1) {
				valid = false;
			}

			if(valid) {
				String v = (String)employee.getSelectedItem();

				if(v.equals("N/A")) {
					this.user.employee = null;
				} else {
					this.user.employee = this.employees.stream()
						.filter(b -> b.getFullName().equals(v)).findAny().orElse(null);
				}

				this.panel.remove(username1);
				this.panel.remove(username2);
				this.panel.remove(insurance1);
				this.panel.remove(insurance2);
				this.panel.remove(employee);
				this.panel.remove(save);
				this.panel.remove(delete);
				this.panel.remove(background);
				this.setStatic(width, height, y);
				this.onEdit.run();
			}

			this.panel.repaint();
		});

		delete.addActionListener(e -> this.onDelete.run());
		return 230;
	}

	protected int setStatic(int width, int height, int y) {
		JLabel username1 = this.createLabel("Username:", true);
		JLabel username2 = this.createLabel(this.user.username, false);
		username1.setBounds(50, y + 50, width / 2 - 120, height / 7);
		username2.setBounds(50, y + 50, width / 2 - 120, height / 7);
		username2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(username1);
		this.panel.add(username2);

		JLabel insurance1 = this.createLabel("Insurance:", true);
		JLabel insurance2 = this.createLabel(this.user.insurance, false);
		insurance1.setBounds(50, y + 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		insurance2.setBounds(50, y + 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		insurance2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(insurance1);
		this.panel.add(insurance2);

		//===================================================================================================//

		JLabel employee1 = this.createLabel("Employee:", true);
		JLabel employee2 = this.createLabel(this.user.employee == null ? "N/A" : this.user.employee.getFullName(), false);
		employee1.setBounds(width / 2 + 40, y + 50, width / 2 - 120, height / 7);
		employee2.setBounds(width / 2 + 40, y + 50, width / 2 - 120, height / 7);
		employee2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(employee1);
		this.panel.add(employee2);

		JButton edit = new JButton("Edit");
		edit.setFont(new Font(edit.getFont().getName(),Font.PLAIN, 18));
		edit.setBounds(50, y + 60 + 2 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		this.panel.add(edit);

		JButton delete = new JButton("Delete");
		delete.setFont(new Font(delete.getFont().getName(),Font.PLAIN, 18));
		delete.setBounds(width / 2 + 40 + (width / 2 - 120) / 2 + 5, y + 60 + 2 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		this.panel.add(delete);

		JComponent background = new JComponent() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Color c = this.getBackground();
				g.setColor(new Color((int)(c.getRed() * 0.9), (int)(c.getGreen() * 0.9), (int)(c.getBlue() * 0.9)));
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
		};

		background.setBounds(0, y, width, 230);
		if(dark) this.panel.add(background);

		edit.addActionListener(e -> {
			this.panel.remove(username1);
			this.panel.remove(insurance1);
			this.panel.remove(employee1);
			this.panel.remove(username2);
			this.panel.remove(insurance2);
			this.panel.remove(employee2);
			this.panel.remove(edit);
			this.panel.remove(delete);
			this.panel.remove(background);
			this.setEditable(width, height, y);
			this.panel.repaint();
		});

		delete.addActionListener(e -> this.onDelete.run());
		return 230;
	}

	public JLabel createLabel(String text, boolean bold) {
		JLabel label = new JLabel(text);
		label.setFont(new Font(label.getFont().getName(), !bold ? Font.BOLD : Font.PLAIN, 22));
		label.setBackground(new Color(0, 0, 0, 0));
		label.setFocusable(false);
		label.setOpaque(true);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		return label;
	}

	public JTextField createField(String text, String hint) {
		JTextField field = new JTextField(text);
		Utils.setHint(field, hint);
		return field;
	}

	private <E> JComboBox<E> createComboBox(E[] values, E value) {
		JComboBox<E> comboBox = new JComboBox<>(values);
		comboBox.setSelectedItem(value);
		return comboBox;
	}


	
}