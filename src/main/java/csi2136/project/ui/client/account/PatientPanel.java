package csi2136.project.ui.client.account;

import csi2136.project.core.*;
import csi2136.project.ui.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientPanel extends JPanel {

	protected final JFrame frame;
	protected final Patient patient;
	private final List<Employee> employees;
	private final List<String> procedures;
	protected final Runnable onEdit;
	protected final Runnable onDelete;

	public PatientPanel(JFrame frame, Patient patient, List<Employee> employees, List<String> procedures, boolean edit, Runnable onEdit, Runnable onDelete) {
		this.frame = frame;
		this.patient = patient;
		this.employees = employees;
		this.procedures = procedures;
		this.onEdit = onEdit;
		this.onDelete = onDelete;
		this.setLayout(null);

		if(edit) {
			this.setEditable(frame.getWidth(), 200);
		} else {
			this.setStatic(frame.getWidth(), 200);
		}

		this.addAppointments(frame.getWidth());
	}

	private void addAppointments(int width) {
		int y = 200 + 180;
		boolean dark = false;

		for(Appointment appointment : this.patient.appointments) {
			AppointmentPanel panel = new AppointmentPanel(this, appointment, width);
			panel.setBounds(0, y, width, (int)panel.getPreferredSize().getHeight());
			y += panel.getPreferredSize().getHeight();

			if(dark) {
				Color c = this.getBackground();
				c = new Color((int)(c.getRed() * 0.9), (int)(c.getGreen() * 0.9), (int)(c.getBlue() * 0.9));
				panel.setBackground(c);
			} else {
				panel.setBackground(this.getBackground());
			}

			dark = !dark;
			this.add(panel);
		}

		int i = this.createNewButton(width, y);
		this.setPreferredSize(new Dimension(width, y + i));
	}

	public int createNewButton(int width, int y) {
		JButton add = new JButton("New...");
		add.setFont(new Font(add.getFont().getName(), Font.PLAIN, 18));
		add.setBounds(width - 255, y, (width / 2 - 120) / 2, 38);
		this.add(add);

		add.addActionListener(e -> {
			JDialog dialog = new JDialog(this.frame, "Create Appointment");
			dialog.setLayout(new GridLayout(7, 1));

			JComboBox<String> emp = new JComboBox<>(this.employees.stream().map(Employee::getFullName).toArray(String[]::new));
			emp.setSelectedIndex(0);

			JTextField date = this.createField("", "Date...");
			JTextField startTime = this.createField("", "Start Time...");
			JTextField endTime = this.createField("", "End Time...");
			JComboBox<String> procedure = new JComboBox<>(this.procedures.toArray(new String[0]));

			JButton book = new JButton("Book");
			book.setFont(new Font(add.getFont().getName(), Font.PLAIN, 18));

			JButton close = new JButton("Close");
			close.setFont(new Font(add.getFont().getName(), Font.PLAIN, 18));

			book.addActionListener(a -> {
				Employee target = this.employees.stream()
					.filter(employee -> employee.getFullName().equals(emp.getSelectedItem()))
					.findFirst().orElse(null);
				if(target == null)return;

				Appointment appointment = new Appointment(this.patient);
				appointment.employee = target;
				appointment.treatment = null;
				appointment.date = date.getText();
				appointment.startTime = startTime.getText();
				appointment.endTime = endTime.getText();
				appointment.status = Appointment.Status.SCHEDULED;
				appointment.type = (String)procedure.getSelectedItem();
				this.patient.appointments.add(appointment);
				this.addAppointments(width);
				this.onEdit.run();
				dialog.dispose();
			});

			close.addActionListener(a -> {
				dialog.dispose();
			});

			dialog.add(emp);
			dialog.add(date);
			dialog.add(startTime);
			dialog.add(endTime);
			dialog.add(procedure);
			dialog.add(book);
			dialog.add(close);
			dialog.setSize(300, 300);
			dialog.setLocation(200, 200);
			dialog.setVisible(true);
		});

		return 10 + 38 + 10;
	}

	public void updateAppointments() {
		List<AppointmentPanel> panels = new ArrayList<>();

		for(int i = this.getComponents().length - 1; i >= 0; i--) {
			Component c = this.getComponent(i);

			if(c instanceof AppointmentPanel) {
				panels.add((AppointmentPanel)c);
				this.remove(i);
			} else if(c instanceof JButton) {
				if(((JButton)c).getText().equals("New...")) {
					this.remove(i);
				}
			}
		}

		Collections.reverse(panels);
		int y = 200 + 180, width = (int)this.getPreferredSize().getWidth();
		boolean dark = false;

		for(AppointmentPanel panel : panels) {
			panel.setBounds(0, y, width, (int)panel.getPreferredSize().getHeight());
			y += panel.getPreferredSize().getHeight();

			if(dark) {
				Color c = this.getBackground();
				c = new Color((int)(c.getRed() * 0.9), (int)(c.getGreen() * 0.9), (int)(c.getBlue() * 0.9));
				panel.setBackground(c);
			} else {
				panel.setBackground(this.getBackground());
			}

			dark = !dark;
			this.add(panel);
		}

		int i = this.createNewButton(width, y);
		this.setPreferredSize(new Dimension(width, y + i));
		this.repaint();
	}

	public void addAppointment(Appointment appointment) {
		AppointmentPanel panel = new AppointmentPanel(this, appointment, (int)this.getPreferredSize().getWidth());
		this.add(panel, 0);
		this.updateAppointments();
	}

	protected void setEditable(int width, int height) {
		JTextField firstName = this.createField(this.patient.firstName, "First Name...");
		firstName.setBounds(50, 50, width / 2 - 120, height / 7);
		this.add(firstName);

		JTextField middleName = this.createField(this.patient.middleName, "Middle Name...");
		middleName.setBounds(50, 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		this.add(middleName);

		JTextField lastName = this.createField(this.patient.lastName, "Last Name...");
		lastName.setBounds(50, 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.add(lastName);

		JTextField ssn = this.createField(this.patient.ssn, "SSN...");
		ssn.setBounds(50, 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.add(ssn);

		JTextField birthDate = this.createField(this.patient.birthDate, "Date of Birth...");
		birthDate.setBounds(50, 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.add(birthDate);

		JComboBox<Gender> gender = this.createComboBox(Gender.values(), this.patient.gender);
		gender.setBounds(50, 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.add(gender);

		JTextField houseNumber = this.createField(String.valueOf(this.patient.houseNumber), "House Number...");
		houseNumber.setBounds(width / 2 + 40, 50, width / 2 - 120, height / 7);
		this.add(houseNumber);

		JTextField street = this.createField(this.patient.street, "Street...");
		street.setBounds(width / 2 + 40, 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		this.add(street);

		JTextField city = this.createField(this.patient.city, "City...");
		city.setBounds(width / 2 + 40, 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.add(city);

		JComboBox<Province> province = this.createComboBox(Province.values(), this.patient.province);
		province.setBounds(width / 2 + 40, 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.add(province);

		JTextField email = this.createField(this.patient.email, "Email...");
		email.setBounds(width / 2 + 40, 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.add(email);

		JTextField phoneNumber = this.createField(this.patient.phoneNumber, "Phone Number...");
		phoneNumber.setBounds(width / 2 + 40, 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		this.add(phoneNumber);

		JButton save = new JButton("Save");
		save.setFont(new Font(save.getFont().getName(), Font.PLAIN, 18));
		save.setBounds(50, 60 + 6 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		this.add(save);

		JButton delete = new JButton("Delete");
		delete.setFont(new Font(delete.getFont().getName(),Font.PLAIN, 18));
		delete.setBounds(width / 2 + 40 + (width / 2 - 120) / 2 + 5, 60 + 6 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		this.add(delete);

		JComponent background = new JComponent() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Color c = this.getBackground();
				g.setColor(new Color((int)(c.getRed() * 0.9), (int)(c.getGreen() * 0.9), (int)(c.getBlue() * 0.9)));
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
		};

		background.setBounds(0, 0, width, 200 + 180);
		this.add(background);

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

			if(firstName.getText().equals("First Name...")) { firstName.setForeground(Color.RED); valid = false; }
			if(middleName.getText().equals("Middle Name...")) { middleName.setForeground(Color.RED); valid = false; }
			if(lastName.getText().equals("Last Name...")) { lastName.setForeground(Color.RED); valid = false; }
			if(birthDate.getText().equals("Date of Birth...")) { birthDate.setForeground(Color.RED); valid = false; }
			if(houseNumber.getText().equals("0")) { houseNumber.setForeground(Color.RED); valid = false; }
			if(street.getText().equals("Street...")) { street.setForeground(Color.RED); valid = false; }
			if(city.getText().equals("City...")) { city.setForeground(Color.RED); valid = false; }
			if(email.getText().equals("Email...")) { email.setForeground(Color.RED); valid = false; }
			if(phoneNumber.getText().equals("Phone Number...")) { phoneNumber.setForeground(Color.RED); valid = false; }

			if(gender.getSelectedIndex() == -1 || province.getSelectedIndex()  == -1) {
				valid = false;
			}

			if(valid) {
				this.patient.firstName = firstName.getText();
				this.patient.middleName = middleName.getText();
				this.patient.lastName = lastName.getText();
				this.patient.ssn = ssn.getText();
				this.patient.birthDate = birthDate.getText();
				this.patient.gender = (Gender)gender.getSelectedItem();
				this.patient.houseNumber = Integer.parseInt(houseNumber.getText());
				this.patient.street = street.getText();
				this.patient.city = city.getText();
				this.patient.province = (Province)province.getSelectedItem();
				this.patient.email = email.getText();
				this.patient.phoneNumber = phoneNumber.getText();
				this.remove(firstName);
				this.remove(middleName);
				this.remove(lastName);
				this.remove(ssn);
				this.remove(birthDate);
				this.remove(gender);
				this.remove(houseNumber);
				this.remove(street);
				this.remove(city);
				this.remove(province);
				this.remove(email);
				this.remove(phoneNumber);
				this.remove(save);
				this.remove(delete);
				this.remove(background);
				this.setStatic(width, height);
				this.onEdit.run();
			}

			this.repaint();
		});

		delete.addActionListener(e -> this.onDelete.run());
	}

	protected void setStatic(int width, int height) {
		JLabel firstName1 = this.createLabel("First Name:", true);
		JLabel firstName2 = this.createLabel(this.patient.firstName, false);
		firstName1.setBounds(50, 50, width / 2 - 120, height / 7);
		firstName2.setBounds(50, 50, width / 2 - 120, height / 7);
		firstName2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(firstName1);
		this.add(firstName2);

		JLabel middleName1 = this.createLabel("Middle Name:", true);
		JLabel middleName2 = this.createLabel(this.patient.middleName, false);
		middleName1.setBounds(50, 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		middleName2.setBounds(50, 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		middleName2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(middleName1);
		this.add(middleName2);

		JLabel lastName1 = this.createLabel("Last Name:", true);
		JLabel lastName2 = this.createLabel(this.patient.lastName, false);
		lastName1.setBounds(50, 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		lastName2.setBounds(50, 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		lastName2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(lastName1);
		this.add(lastName2);

		JLabel ssn1 = this.createLabel("SSN:", true);
		JLabel ssn2 = this.createLabel(this.patient.ssn, false);
		ssn1.setBounds(50, 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		ssn2.setBounds(50, 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		ssn2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(ssn1);
		this.add(ssn2);

		JLabel birthDate1 = this.createLabel("Date of Birth:", true);
		JLabel birthDate2 = this.createLabel(this.patient.birthDate, false);
		birthDate1.setBounds(50, 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		birthDate2.setBounds(50, 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		birthDate2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(birthDate1);
		this.add(birthDate2);

		JLabel gender1 = this.createLabel("Gender:", true);
		JLabel gender2 = this.createLabel(this.patient.gender.getName(), false);
		gender1.setBounds(50, 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		gender2.setBounds(50, 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		gender2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(gender1);
		this.add(gender2);

		//===================================================================================================//

		JLabel houseNumber1 = this.createLabel("House Number:", true);
		JLabel houseNumber2 = this.createLabel(String.valueOf(this.patient.houseNumber), false);
		houseNumber1.setBounds(width / 2 + 40, 50, width / 2 - 120, height / 7);
		houseNumber2.setBounds(width / 2 + 40, 50, width / 2 - 120, height / 7);
		houseNumber2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(houseNumber1);
		this.add(houseNumber2);

		JLabel street1 = this.createLabel("Street:", true);
		JLabel street2 = this.createLabel(this.patient.street, false);
		street1.setBounds(width / 2 + 40, 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		street2.setBounds(width / 2 + 40, 50 + (height / 7 + 10), width / 2 - 120, height / 7);
		street2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(street1);
		this.add(street2);

		JLabel city1 = this.createLabel("City:", true);
		JLabel city2 = this.createLabel(this.patient.city, false);
		city1.setBounds(width / 2 + 40, 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		city2.setBounds(width / 2 + 40, 50 + 2 * (height / 7 + 10), width / 2 - 120, height / 7);
		city2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(city1);
		this.add(city2);

		JLabel province1 = this.createLabel("Province:", true);
		JLabel province2 = this.createLabel(this.patient.province.getName(), false);
		province1.setBounds(width / 2 + 40, 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		province2.setBounds(width / 2 + 40, 50 + 3 * (height / 7 + 10), width / 2 - 120, height / 7);
		province2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(province1);
		this.add(province2);

		JLabel email1 = this.createLabel("Email:", true);
		JLabel email2 = this.createLabel(this.patient.email, false);
		email1.setBounds(width / 2 + 40, 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		email2.setBounds(width / 2 + 40, 50 + 4 * (height / 7 + 10), width / 2 - 120, height / 7);
		email2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(email1);
		this.add(email2);

		JLabel phoneNumber1 = this.createLabel("Phone Number:", true);
		JLabel phoneNumber2 = this.createLabel(this.patient.phoneNumber, false);
		phoneNumber1.setBounds(width / 2 + 40, 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		phoneNumber2.setBounds(width / 2 + 40, 50 + 5 * (height / 7 + 10), width / 2 - 120, height / 7);
		phoneNumber2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(phoneNumber1);
		this.add(phoneNumber2);

		JButton edit = new JButton("Edit");
		edit.setFont(new Font(edit.getFont().getName(),Font.PLAIN, 18));
		edit.setBounds(50, 60 + 6 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		this.add(edit);

		JButton delete = new JButton("Delete");
		delete.setFont(new Font(delete.getFont().getName(),Font.PLAIN, 18));
		delete.setBounds(width / 2 + 40 + (width / 2 - 120) / 2 + 5, 60 + 6 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		this.add(delete);

		JComponent background = new JComponent() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Color c = this.getBackground();
				g.setColor(new Color((int)(c.getRed() * 0.9), (int)(c.getGreen() * 0.9), (int)(c.getBlue() * 0.9)));
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
		};

		background.setBounds(0, 0, width, 200 + 180);
		this.add(background);

		edit.addActionListener(e -> {
			this.remove(firstName1);
			this.remove(middleName1);
			this.remove(lastName1);
			this.remove(ssn1);
			this.remove(birthDate1);
			this.remove(gender1);
			this.remove(houseNumber1);
			this.remove(street1);
			this.remove(city1);
			this.remove(province1);
			this.remove(email1);
			this.remove(phoneNumber1);
			this.remove(firstName2);
			this.remove(middleName2);
			this.remove(lastName2);
			this.remove(ssn2);
			this.remove(birthDate2);
			this.remove(gender2);
			this.remove(houseNumber2);
			this.remove(street2);
			this.remove(city2);
			this.remove(province2);
			this.remove(email2);
			this.remove(phoneNumber2);
			this.remove(edit);
			this.remove(delete);
			this.remove(background);
			this.setEditable(width, height);
			this.repaint();
		});

		delete.addActionListener(e -> this.onDelete.run());
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
