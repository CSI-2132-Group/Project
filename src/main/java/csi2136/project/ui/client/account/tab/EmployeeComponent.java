package csi2136.project.ui.client.account.tab;

import csi2136.project.AccountType;
import csi2136.project.core.Employee;
import csi2136.project.core.Gender;
import csi2136.project.core.Province;
import csi2136.project.ui.Utils;

import javax.swing.*;
import java.awt.*;

public class EmployeeComponent {

	private final EmployeePanel panel;
	private final Employee employee;
	private final boolean dark;
	private Runnable onDelete;
	private Runnable onEdit;

	public EmployeeComponent(EmployeePanel panel, Employee employee, Runnable onDelete, Runnable onEdit, boolean dark) {
		this.panel = panel;
		this.employee = employee;
		this.onDelete = onDelete;
		this.onEdit = onEdit;
		this.dark = dark;
	}
	
	public int setEditable(int width, int height, int y) {
		JTextField firstName = this.createField(this.employee.firstName, "First Name...");
		firstName.setBounds(50, y + 50, width / 2 - 120, height / 7);
		this.panel.add(firstName);

		JTextField middleName = this.createField(this.employee.middleName, "Middle Name...");
		middleName.setBounds(50, y + 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(middleName);

		JTextField lastName = this.createField(this.employee.lastName, "Last Name...");
		lastName.setBounds(50, y + 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(lastName);

		JTextField ssn = this.createField(this.employee.ssn, "SSN...");
		ssn.setBounds(50, y + 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(ssn);

		JTextField birthDate = this.createField(this.employee.birthDate, "Date of Birth...");
		birthDate.setBounds(50, y + 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(birthDate);

		JComboBox<Gender> gender = this.createComboBox(Gender.values(), this.employee.gender);
		gender.setBounds(50, y + 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(gender);

		JComboBox<AccountType> type = this.createComboBox(AccountType.values(), this.employee.type);
		type.setBounds(50, y + 50 + 6 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(type);

		JTextField houseNumber = this.createField(String.valueOf(this.employee.houseNumber), "House Number...");
		houseNumber.setBounds(width / 2 + 40, y + 50, width / 2 - 120, height / 7);
		this.panel.add(houseNumber);

		JTextField street = this.createField(this.employee.street, "Street...");
		street.setBounds(width / 2 + 40, y + 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(street);

		JTextField city = this.createField(this.employee.city, "City...");
		city.setBounds(width / 2 + 40, y + 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(city);

		JComboBox<Province> province = this.createComboBox(Province.values(), this.employee.province);
		province.setBounds(width / 2 + 40, y + 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(province);

		JTextField email = this.createField(this.employee.email, "Email...");
		email.setBounds(width / 2 + 40, y + 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(email);

		JTextField phoneNumber = this.createField(this.employee.phoneNumber, "Phone Number...");
		phoneNumber.setBounds(width / 2 + 40, y + 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(phoneNumber);

		JTextField salary = this.createField(String.valueOf(this.employee.salary), "Salary...");
		salary.setBounds(width / 2 + 40, y + 50 + 6 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.panel.add(salary);

		JButton save = new JButton("Save");
		save.setFont(new Font(save.getFont().getName(), Font.PLAIN, 18));
		save.setBounds(50, y + 60 + 7 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		this.panel.add(save);

		JButton delete = new JButton("Delete");
		delete.setFont(new Font(delete.getFont().getName(),Font.PLAIN, 18));
		delete.setBounds(width / 2 + 40 + (width / 2 - 120) / 2 + 5, y + 60 + 7 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
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

		background.setBounds(0, y, width, 420);
		if(dark) this.panel.add(background);

		save.addActionListener(e -> {
			boolean valid = true;

			try {
				ssn.setText(ssn.getText().trim());
				int v = Integer.parseInt(ssn.getText());

				if(v < 0 || v > 999999999) {
					ssn.setForeground(Color.RED);
					valid = false;
				}
			} catch(NumberFormatException ignored) {
				ssn.setForeground(Color.RED);
				valid = false;
			}

			try {
				houseNumber.setText(houseNumber.getText().trim());
				int v = Integer.parseInt(houseNumber.getText());

				if(v < 0 || v > 99999) {
					houseNumber.setForeground(Color.RED);
					valid = false;
				}
			} catch(NumberFormatException ignored) {
				houseNumber.setForeground(Color.RED);
				valid = false;
			}

			try {
				salary.setText(salary.getText().trim());
				int v = Integer.parseInt(salary.getText());

				if(v <= 0) {
					salary.setForeground(Color.RED);
					valid = false;
				}
			} catch(NumberFormatException ignored) {
				salary.setForeground(Color.RED);
				valid = false;
			}

			if(firstName.getText().equals("First Name...")) { firstName.setForeground(Color.RED); valid = false; }
			if(middleName.getText().equals("Middle Name...")) { middleName.setForeground(Color.RED); valid = false; }
			if(lastName.getText().equals("Last Name...")) { lastName.setForeground(Color.RED); valid = false; }
			if(birthDate.getText().equals("Date of Birth...")) { birthDate.setForeground(Color.RED); valid = false; }
			if(houseNumber.getText().equals("0")) { houseNumber.setForeground(Color.RED); valid = false; }
			if(street.getText().equals("Street...")) { street.setForeground(Color.RED); valid = false; }
			if(city.getText().equals("City...")) { city.setForeground(Color.RED); valid = false; }
			if(email.getText().equals("Email...")) { email.setForeground(Color.RED); valid = false; }
			if(phoneNumber.getText().equals("Phone Number...")) { phoneNumber.setForeground(Color.RED); valid = false; }

			if(gender.getSelectedIndex() == -1 || province.getSelectedIndex()  == -1 || type.getSelectedIndex() == -1) {
				valid = false;
			}

			if(valid) {
				this.employee.firstName = firstName.getText();
				this.employee.middleName = middleName.getText();
				this.employee.lastName = lastName.getText();
				this.employee.ssn = ssn.getText();
				this.employee.birthDate = birthDate.getText();
				this.employee.gender = (Gender)gender.getSelectedItem();
				this.employee.houseNumber = Integer.parseInt(houseNumber.getText());
				this.employee.street = street.getText();
				this.employee.city = city.getText();
				this.employee.province = (Province)province.getSelectedItem();
				this.employee.email = email.getText();
				this.employee.phoneNumber = phoneNumber.getText();
				this.employee.salary = Integer.parseInt(salary.getText());
				this.employee.type = (AccountType)type.getSelectedItem();
				this.panel.remove(firstName);
				this.panel.remove(middleName);
				this.panel.remove(lastName);
				this.panel.remove(ssn);
				this.panel.remove(birthDate);
				this.panel.remove(gender);
				this.panel.remove(houseNumber);
				this.panel.remove(street);
				this.panel.remove(city);
				this.panel.remove(province);
				this.panel.remove(email);
				this.panel.remove(phoneNumber);
				this.panel.remove(save);
				this.panel.remove(delete);
				this.panel.remove(background);
				this.panel.remove(salary);
				this.panel.remove(type);
				this.setStatic(width, height, y);
				this.onEdit.run();
			}

			this.panel.repaint();
		});

		delete.addActionListener(e -> this.onDelete.run());
		return 420;
	}

	protected int setStatic(int width, int height, int y) {
		JLabel firstName1 = this.createLabel("First Name:", true);
		JLabel firstName2 = this.createLabel(this.employee.firstName, false);
		firstName1.setBounds(50, y + 50, width / 2 - 120, height / 7);
		firstName2.setBounds(50, y + 50, width / 2 - 120, height / 7);
		firstName2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(firstName1);
		this.panel.add(firstName2);

		JLabel middleName1 = this.createLabel("Middle Name:", true);
		JLabel middleName2 = this.createLabel(this.employee.middleName, false);
		middleName1.setBounds(50, y + 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		middleName2.setBounds(50, y + 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		middleName2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(middleName1);
		this.panel.add(middleName2);

		JLabel lastName1 = this.createLabel("Last Name:", true);
		JLabel lastName2 = this.createLabel(this.employee.lastName, false);
		lastName1.setBounds(50, y + 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		lastName2.setBounds(50, y + 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		lastName2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(lastName1);
		this.panel.add(lastName2);

		JLabel ssn1 = this.createLabel("SSN:", true);
		JLabel ssn2 = this.createLabel(this.employee.ssn, false);
		ssn1.setBounds(50, y + 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		ssn2.setBounds(50, y + 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		ssn2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(ssn1);
		this.panel.add(ssn2);

		JLabel birthDate1 = this.createLabel("Date of Birth:", true);
		JLabel birthDate2 = this.createLabel(this.employee.birthDate, false);
		birthDate1.setBounds(50, y + 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		birthDate2.setBounds(50, y + 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		birthDate2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(birthDate1);
		this.panel.add(birthDate2);

		JLabel gender1 = this.createLabel("Gender:", true);
		JLabel gender2 = this.createLabel(this.employee.gender.getName(), false);
		gender1.setBounds(50, y + 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		gender2.setBounds(50, y + 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		gender2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(gender1);
		this.panel.add(gender2);

		JLabel type1 = this.createLabel("Type:", true);
		JLabel type2 = this.createLabel(this.employee.type.name(), false);
		type1.setBounds(50, y + 50 + 6 * (height / 7 + 10), width / 2 - 120, height / 7);
		type2.setBounds(50, y + 50 + 6 * (height / 7 + 10), width / 2 - 120, height / 7);
		type2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(type1);
		this.panel.add(type2);

		//===================================================================================================//

		JLabel houseNumber1 = this.createLabel("House Number:", true);
		JLabel houseNumber2 = this.createLabel(String.valueOf(this.employee.houseNumber), false);
		houseNumber1.setBounds(width / 2 + 40, y + 50, width / 2 - 120, height / 7);
		houseNumber2.setBounds(width / 2 + 40, y + 50, width / 2 - 120, height / 7);
		houseNumber2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(houseNumber1);
		this.panel.add(houseNumber2);

		JLabel street1 = this.createLabel("Street:", true);
		JLabel street2 = this.createLabel(this.employee.street, false);
		street1.setBounds(width / 2 + 40, y + 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		street2.setBounds(width / 2 + 40, y + 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		street2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(street1);
		this.panel.add(street2);

		JLabel city1 = this.createLabel("City:", true);
		JLabel city2 = this.createLabel(this.employee.city, false);
		city1.setBounds(width / 2 + 40, y + 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		city2.setBounds(width / 2 + 40, y + 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		city2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(city1);
		this.panel.add(city2);

		JLabel province1 = this.createLabel("Province:", true);
		JLabel province2 = this.createLabel(this.employee.province.getName(), false);
		province1.setBounds(width / 2 + 40, y + 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		province2.setBounds(width / 2 + 40, y + 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		province2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(province1);
		this.panel.add(province2);

		JLabel email1 = this.createLabel("Email:", true);
		JLabel email2 = this.createLabel(this.employee.email, false);
		email1.setBounds(width / 2 + 40, y + 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		email2.setBounds(width / 2 + 40, y + 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		email2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(email1);
		this.panel.add(email2);

		JLabel phoneNumber1 = this.createLabel("Phone Number:", true);
		JLabel phoneNumber2 = this.createLabel(this.employee.phoneNumber, false);
		phoneNumber1.setBounds(width / 2 + 40, y + 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		phoneNumber2.setBounds(width / 2 + 40, y + 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		phoneNumber2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(phoneNumber1);
		this.panel.add(phoneNumber2);

		JLabel salary1 = this.createLabel("Salary:", true);
		JLabel salary2 = this.createLabel(String.valueOf(this.employee.salary), false);
		salary1.setBounds(width / 2 + 40, y + 50 + 6 * (height / 7 + 10), width / 2 - 120, height / 7);
		salary2.setBounds(width / 2 + 40, y + 50 + 6 * (height / 7 + 10), width / 2 - 120, height / 7);
		salary2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(salary1);
		this.panel.add(salary2);

		JButton edit = new JButton("Edit");
		edit.setFont(new Font(edit.getFont().getName(),Font.PLAIN, 18));
		edit.setBounds(50, y + 60 + 7 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		this.panel.add(edit);

		JButton delete = new JButton("Delete");
		delete.setFont(new Font(delete.getFont().getName(),Font.PLAIN, 18));
		delete.setBounds(width / 2 + 40 + (width / 2 - 120) / 2 + 5, y + 60 + 7 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
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

		background.setBounds(0, y, width, 420);
		if(dark) this.panel.add(background);

		edit.addActionListener(e -> {
			this.panel.remove(firstName1);
			this.panel.remove(middleName1);
			this.panel.remove(lastName1);
			this.panel.remove(ssn1);
			this.panel.remove(birthDate1);
			this.panel.remove(gender1);
			this.panel.remove(houseNumber1);
			this.panel.remove(street1);
			this.panel.remove(city1);
			this.panel.remove(province1);
			this.panel.remove(email1);
			this.panel.remove(phoneNumber1);
			this.panel.remove(firstName2);
			this.panel.remove(middleName2);
			this.panel.remove(lastName2);
			this.panel.remove(ssn2);
			this.panel.remove(birthDate2);
			this.panel.remove(gender2);
			this.panel.remove(houseNumber2);
			this.panel.remove(street2);
			this.panel.remove(city2);
			this.panel.remove(province2);
			this.panel.remove(email2);
			this.panel.remove(phoneNumber2);
			this.panel.remove(salary1);
			this.panel.remove(salary2);
			this.panel.remove(type1);
			this.panel.remove(type2);
			this.panel.remove(edit);
			this.panel.remove(delete);
			this.panel.remove(background);
			this.setEditable(width, height, y);
			this.panel.repaint();
		});

		delete.addActionListener(e -> {
			this.onDelete.run();
		});

		return 420;
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