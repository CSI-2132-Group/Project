package csi2136.project.ui.client.account.tab;

import csi2136.project.core.Appointment;
import csi2136.project.ui.Utils;

import javax.swing.*;
import java.awt.*;

public class AppointmentComponent {

	private final AppointmentPanel panel;
	private final Appointment appointment;
	private final boolean dark;
	private Runnable onDelete;
	private Runnable onEdit;

	public AppointmentComponent(AppointmentPanel panel, Appointment appointment, Runnable onDelete, Runnable onEdit, boolean dark) {
		this.panel = panel;
		this.appointment = appointment;
		this.onDelete = onDelete;
		this.onEdit = onEdit;
		this.dark = dark;
	}
	
	public int setEditable(int width, int height, int y) {
		JLabel type = this.createLabel(this.appointment.type + " (" + this.appointment.patient.getFullName() + ")", true);
		type.setBounds(100, y, width, 130);
		this.panel.add(type);

		JLabel time = this.createLabel(this.appointment.getFullTime(), false);
		time.setBounds(100, y, width - 180, 130);
		time.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(time);

		JLabel branch1 = this.createLabel("Branch:", false);
		JLabel branch2 = this.createLabel(this.appointment.employee.branch.name, true);
		branch2.setHorizontalAlignment(SwingConstants.RIGHT);
		branch1.setBounds(50, y + 105, width / 2 - 120, 28);
		branch2.setBounds(50, y + 105, width / 2 - 120, 28);
		this.panel.add(branch1);
		this.panel.add(branch2);

		JTextField treatment = this.createField(this.appointment.treatment == null ? "N/A"
			: this.appointment.treatment.type, "Treatment...");
		treatment.setBounds(50, y + 105 + 38, width / 2 - 120, 28);
		this.panel.add(treatment);

		JTextField tooth = this.createField(this.appointment.treatment == null ? "N/A"
			: this.appointment.treatment.tooth, "Tooth...");
		tooth.setBounds(50, y + 105 + 38 * 2, width / 2 - 120, 28);
		this.panel.add(tooth);

		JTextField medication = this.createField(this.appointment.treatment == null ? "N/A"
			: this.appointment.treatment.medication, "Medication...");
		medication.setBounds(50, y + 105 + 3 * 38, width / 2 - 120, 28);
		this.panel.add(medication);

		JLabel comments1 = this.createLabel("Comments:", false);
		comments1.setBounds(50, y + 105 + 4 * 38, width / 2 - 120, 28);
		this.panel.add(comments1);

		JTextArea comments2 = new JTextArea(this.appointment.treatment == null ? "" : this.appointment.treatment.comments);
		comments2.setBounds(50, y + 105 + 5 * 38, width / 2 - 120, 28 * 5);
		comments2.setEditable(true);
		comments2.setLineWrap(true);
		comments2.setFont(new Font(comments2.getFont().getName(), Font.BOLD, 14));
		this.panel.add(comments2);

		//================================//

		JLabel invoice = this.createLabel("Invoice", true);
		invoice.setHorizontalAlignment(SwingConstants.CENTER);
		invoice.setBounds(width / 2 + 40, y + 105, width / 2 - 120, 28);
		this.panel.add(invoice);

		JTextField insuranceCharge = this.createField((this.appointment.invoice == null ? 0
			: this.appointment.invoice.insuranceCharge) + "", "Insurance Charge...");
		insuranceCharge.setBounds(width / 2 + 40, y + 105 + 38, width / 2 - 120, 28);
		this.panel.add(insuranceCharge);

		JTextField patientCharge = this.createField((this.appointment.invoice == null ? 0
			: this.appointment.invoice.patientCharge) + "", "Patient Charge...");
		patientCharge.setBounds(width / 2 + 40, y + 105 + 2 * 38, width / 2 - 120, 28);
		this.panel.add(patientCharge);

		JTextField discount = this.createField((this.appointment.invoice == null ? 0
			: this.appointment.invoice.discount) + "", "Discount...");
		discount.setBounds(width / 2 + 40, y + 105 + 3 * 38, width / 2 - 120, 28);
		this.panel.add(discount);

		JTextField penalty = this.createField((this.appointment.invoice == null ? 0
			: this.appointment.invoice.penalty) + "", "Penalty...");
		penalty.setBounds(width / 2 + 40, y + 105 + 4 * 38, width / 2 - 120, 28);
		this.panel.add(penalty);

		JComboBox<Appointment.Status> aStatus = this.createComboBox(Appointment.Status.values(), this.appointment.status);
		aStatus.setBounds(width / 2 + 40, y + 105 + 5 * 38, width / 2 - 120, 28);
		this.panel.add(aStatus);

		//===================================================================================================//

		JButton save = new JButton("Save");
		save.setFont(new Font(save.getFont().getName(),Font.PLAIN, 18));
		//edit.setBounds(50, y + 60 + 2 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		save.setBounds(50, y + 105 + 6 * 38 + 32 * 4, (width / 2 - 120) / 2, 38);
		this.panel.add(save);

		JButton delete = new JButton("Delete");
		delete.setFont(new Font(delete.getFont().getName(),Font.PLAIN, 18));
		//delete.setBounds(width / 2 + 40 + (width / 2 - 120) / 2 + 5, y + 60 + 2 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		delete.setBounds(width - 255, y + 105 + 6 * 38 + 32 * 4, (width / 2 - 120) / 2, 38);
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

		background.setBounds(0, y, width, 550);
		if(dark) this.panel.add(background);

		JComponent status = new JComponent() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.setColor(appointment.status.getColor());
				g.fillOval(50, y + 50, 30, 30);
			}
		};

		this.panel.add(status);

		save.addActionListener(e -> {
			boolean valid = true;

			try {
				insuranceCharge.setText(insuranceCharge.getText().trim());
				Integer.parseInt(insuranceCharge.getText());
			} catch(NumberFormatException ignored) {
				insuranceCharge.setForeground(Color.RED);
				valid = false;
			}

			try {
				patientCharge.setText(patientCharge.getText().trim());
				Integer.parseInt(patientCharge.getText());
			} catch(NumberFormatException ignored) {
				patientCharge.setForeground(Color.RED);
				valid = false;
			}

			try {
				discount.setText(discount.getText().trim());
				Integer.parseInt(discount.getText());
			} catch(NumberFormatException ignored) {
				discount.setForeground(Color.RED);
				valid = false;
			}

			try {
				penalty.setText(penalty.getText().trim());
				Integer.parseInt(penalty.getText());
			} catch(NumberFormatException ignored) {
				penalty.setForeground(Color.RED);
				valid = false;
			}

			if(valid) {
				if(this.appointment.treatment == null) {
					this.appointment.treatment = new Appointment.Treatment();
				}

				if(this.appointment.invoice == null) {
					this.appointment.invoice = new Appointment.Invoice(this.appointment);
				}

				this.appointment.treatment.type = treatment.getText();
				this.appointment.treatment.tooth = tooth.getText();
				this.appointment.treatment.medication = medication.getText();
				this.appointment.treatment.comments = comments2.getText();
				this.appointment.invoice.insuranceCharge = Integer.parseInt(insuranceCharge.getText());
				this.appointment.invoice.patientCharge = Integer.parseInt(patientCharge.getText());
				this.appointment.invoice.discount = Integer.parseInt(discount.getText());
				this.appointment.invoice.penalty = Integer.parseInt(penalty.getText());
				this.appointment.status = (Appointment.Status)aStatus.getSelectedItem();
				this.panel.remove(type);
				this.panel.remove(time);
				this.panel.remove(branch1);
				this.panel.remove(branch2);
				this.panel.remove(type);
				this.panel.remove(treatment);
				this.panel.remove(tooth);
				this.panel.remove(medication);
				this.panel.remove(comments1);
				this.panel.remove(comments2);
				this.panel.remove(invoice);
				this.panel.remove(insuranceCharge);
				this.panel.remove(patientCharge);
				this.panel.remove(discount);
				this.panel.remove(penalty);
				this.panel.remove(aStatus);
				this.panel.remove(save);
				this.panel.remove(delete);
				this.panel.remove(background);
				this.panel.remove(status);
				this.setStatic(width, height, y);
				this.panel.repaint();
			}
		});

		delete.addActionListener(e -> this.onDelete.run());
		return 550;
	}

	protected int setStatic(int width, int height, int y) {
		JLabel type = this.createLabel(this.appointment.type + " (" + this.appointment.patient.getFullName() + ")", true);
		type.setBounds(100, y, width, 130);
		this.panel.add(type);

		JLabel time = this.createLabel(this.appointment.getFullTime(), false);
		time.setBounds(100, y, width - 180, 130);
		time.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(time);

		JLabel branch1 = this.createLabel("Branch:", false);
		JLabel branch2 = this.createLabel(this.appointment.employee.branch.name, true);
		branch2.setHorizontalAlignment(SwingConstants.RIGHT);
		branch1.setBounds(50, y + 105, width / 2 - 120, 28);
		branch2.setBounds(50, y + 105, width / 2 - 120, 28);
		this.panel.add(branch1);
		this.panel.add(branch2);
		
		JLabel type1 = this.createLabel("Treatment:", false);
		JLabel type2 = this.createLabel(this.appointment.treatment.type, true);
		type2.setHorizontalAlignment(SwingConstants.RIGHT);
		type1.setBounds(50, y + 105 + 38, width / 2 - 120, 28);
		type2.setBounds(50, y + 105 + 38, width / 2 - 120, 28);
		this.panel.add(type1);
		this.panel.add(type2);

		JLabel tooth1 = this.createLabel("Target:", false);
		JLabel tooth2 = this.createLabel(this.appointment.treatment.tooth, true);
		tooth2.setHorizontalAlignment(SwingConstants.RIGHT);
		tooth1.setBounds(50, y + 105 + 38 * 2, width / 2 - 120, 28);
		tooth2.setBounds(50, y + 105 + 38 * 2, width / 2 - 120, 28);
		this.panel.add(tooth1);
		this.panel.add(tooth2);

		JLabel medication1 = this.createLabel("Medication:", false);
		JLabel medication2 = this.createLabel(this.appointment.treatment.medication, true);
		medication2.setHorizontalAlignment(SwingConstants.RIGHT);
		medication1.setBounds(50, y + 105 + 3 * 38, width / 2 - 120, 28);
		medication2.setBounds(50, y + 105 + 3 * 38, width / 2 - 120, 28);
		this.panel.add(medication1);
		this.panel.add(medication2);

		JLabel comments1 = this.createLabel("Comments:", false);
		comments1.setBounds(50, y + 105 + 4 * 38, width / 2 - 120, 28);
		this.panel.add(comments1);

		JTextArea comments2 = new JTextArea(this.appointment.treatment.comments);
		comments2.setBounds(50, y + 105 + 5 * 38, width / 2 - 120, 28 * 5);
		comments2.setEditable(true);
		comments2.setLineWrap(true);
		comments2.setFont(new Font(comments2.getFont().getName(), Font.BOLD, 14));
		this.panel.add(comments2);

		//================================//

		JLabel invoice = this.createLabel("Invoice", true);
		invoice.setHorizontalAlignment(SwingConstants.CENTER);
		invoice.setBounds(width / 2 + 40, y + 105, width / 2 - 120, 28);
		this.panel.add(invoice);

		JLabel insuranceCharge1 = this.createLabel("Insurance Charge:", true);
		JLabel insuranceCharge2 = this.createLabel(this.appointment.invoice.insuranceCharge + "$", false);
		insuranceCharge1.setBounds(width / 2 + 40, y + 105 + 38, width / 2 - 120, 28);
		insuranceCharge2.setBounds(width / 2 + 40, y + 105 + 38, width / 2 - 120, 28);
		insuranceCharge2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(insuranceCharge1);
		this.panel.add(insuranceCharge2);

		JLabel patientCharge1 = this.createLabel("Patient Charge:", true);
		JLabel patientCharge2 = this.createLabel(this.appointment.invoice.patientCharge + "$", false);
		patientCharge1.setBounds(width / 2 + 40, y + 105 + 2 * 38, width / 2 - 120, 28);
		patientCharge2.setBounds(width / 2 + 40, y + 105 + 2 * 38, width / 2 - 120, 28);
		patientCharge2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(patientCharge1);
		this.panel.add(patientCharge2);

		JLabel discount1 = this.createLabel("Discount:", true);
		JLabel discount2 = this.createLabel(this.appointment.invoice.discount + "$", false);
		discount1.setBounds(width / 2 + 40, y + 105 + 3 * 38, width / 2 - 120, 28);
		discount2.setBounds(width / 2 + 40, y + 105 + 3 * 38, width / 2 - 120, 28);
		discount2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(discount1);
		this.panel.add(discount2);

		JLabel penalty1 = this.createLabel("Penalty:", true);
		JLabel penalty2 = this.createLabel(this.appointment.invoice.penalty + "$", false);
		penalty1.setBounds(width / 2 + 40, y + 105 + 4 * 38, width / 2 - 120, 28);
		penalty2.setBounds(width / 2 + 40, y + 105 + 4 * 38, width / 2 - 120, 28);
		penalty2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.panel.add(penalty1);
		this.panel.add(penalty2);

		//===================================================================================================//

		JButton edit = new JButton("Edit");
		edit.setFont(new Font(edit.getFont().getName(),Font.PLAIN, 18));
		//edit.setBounds(50, y + 60 + 2 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		edit.setBounds(50, y + 105 + 6 * 38 + 32 * 4, (width / 2 - 120) / 2, 38);
		this.panel.add(edit);

		JButton delete = new JButton("Delete");
		delete.setFont(new Font(delete.getFont().getName(),Font.PLAIN, 18));
		//delete.setBounds(width / 2 + 40 + (width / 2 - 120) / 2 + 5, y + 60 + 2 * (height / 7 + 10), (width / 2 - 120) / 2, height / 7 + 10);
		delete.setBounds(width - 255, y + 105 + 6 * 38 + 32 * 4, (width / 2 - 120) / 2, 38);
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

		background.setBounds(0, y, width, 550);
		if(dark) this.panel.add(background);

		JComponent status = new JComponent() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.setColor(appointment.status.getColor());
				g.fillOval(50, y + 50, 30, 30);
			}
		};

		this.panel.add(status);

		edit.addActionListener(e -> {
			this.panel.remove(type);
			this.panel.remove(time);
			this.panel.remove(branch1);
			this.panel.remove(branch2);
			this.panel.remove(type1);
			this.panel.remove(type2);
			this.panel.remove(tooth1);
			this.panel.remove(tooth2);
			this.panel.remove(medication1);
			this.panel.remove(medication2);
			this.panel.remove(comments1);
			this.panel.remove(comments2);
			this.panel.remove(invoice);
			this.panel.remove(insuranceCharge1);
			this.panel.remove(insuranceCharge2);
			this.panel.remove(patientCharge1);
			this.panel.remove(patientCharge2);
			this.panel.remove(discount1);
			this.panel.remove(discount2);
			this.panel.remove(penalty1);
			this.panel.remove(penalty2);
			this.panel.remove(edit);
			this.panel.remove(delete);
			this.panel.remove(background);
			this.panel.remove(status);
			this.setEditable(width, height, y);
			this.panel.repaint();
		});

		delete.addActionListener(e -> this.onDelete.run());
		return 550;
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