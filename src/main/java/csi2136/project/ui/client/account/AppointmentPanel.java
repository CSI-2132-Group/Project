package csi2136.project.ui.client.account;

import csi2136.project.core.Appointment;

import javax.swing.*;
import java.awt.*;

public class AppointmentPanel extends JPanel {

	private final PatientPanel parent;
	private final Appointment appointment;

	public AppointmentPanel(PatientPanel parent, Appointment appointment, int width) {
        this.parent = parent;
		this.appointment = appointment;
		this.addSummary(width, false);
		this.setLayout(null);
	}

	public void addSummary(int width, boolean showAll) {
		boolean sizeSet = false;

		JLabel type = this.createLabel(appointment.type, true);
		type.setBounds(100, 0, width, 130);
		this.add(type);

		JLabel time = this.createLabel(appointment.getFullTime(), false);
		time.setBounds(100, 0, width - 180, 130);
		time.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(time);

		if(this.appointment.status == Appointment.Status.COMPLETED) {
			JButton moreOrLess = new JButton(showAll ? "Less" : "More");
			moreOrLess.setFont(new Font(moreOrLess.getFont().getName(), Font.PLAIN, 18));
			this.add(moreOrLess);

			if(showAll) {
				if(this.appointment.treatment != null) {
					JLabel dentist1 = this.createLabel("Dentist:", false);
					JLabel dentist2 = this.createLabel(this.appointment.employee.getFullName(), true);
					dentist2.setHorizontalAlignment(SwingConstants.RIGHT);
					dentist1.setBounds(50, 105, width / 2 - 120, 28);
					dentist2.setBounds(50, 105, width / 2 - 120, 28);
					this.add(dentist1);
					this.add(dentist2);

					JLabel type1 = this.createLabel("Treatment:", false);
					JLabel type2 = this.createLabel(this.appointment.treatment.type, true);
					type2.setHorizontalAlignment(SwingConstants.RIGHT);
					type1.setBounds(50, 105 + 38, width / 2 - 120, 28);
					type2.setBounds(50, 105 + 38, width / 2 - 120, 28);
					this.add(type1);
					this.add(type2);

					JLabel tooth1 = this.createLabel("Target:", false);
					JLabel tooth2 = this.createLabel(this.appointment.treatment.tooth, true);
					tooth2.setHorizontalAlignment(SwingConstants.RIGHT);
					tooth1.setBounds(50, 105 + 38 * 2, width / 2 - 120, 28);
					tooth2.setBounds(50, 105 + 38 * 2, width / 2 - 120, 28);
					this.add(tooth1);
					this.add(tooth2);

					JLabel medication1 = this.createLabel("Medication:", false);
					JLabel medication2 = this.createLabel(this.appointment.treatment.medication, true);
					medication2.setHorizontalAlignment(SwingConstants.RIGHT);
					medication1.setBounds(50, 105 + 3 * 38, width / 2 - 120, 28);
					medication2.setBounds(50, 105 + 3 * 38, width / 2 - 120, 28);
					this.add(medication1);
					this.add(medication2);

					JLabel comments1 = this.createLabel("Comments:", false);
					comments1.setBounds(50, 105 + 4 * 38, width / 2 - 120, 28);
					this.add(comments1);

					JTextArea comments2 = new JTextArea(this.appointment.treatment.comments);
					comments2.setBounds(50, 105 + 5 * 38, width / 2 - 120, 28 * 5);
					comments2.setEditable(true);
					comments2.setLineWrap(true);
					comments2.setFont(new Font(comments2.getFont().getName(), Font.BOLD, 14));
					this.add(comments2);
				}

				if(this.appointment.invoice != null) {
					JLabel invoice = this.createLabel("Invoice", true);
					invoice.setHorizontalAlignment(SwingConstants.CENTER);
					invoice.setBounds(width / 2 + 40, 105, width / 2 - 120, 28);
					this.add(invoice);

					JLabel insuranceCharge1 = this.createLabel("Insurance Charge:", true);
					JLabel insuranceCharge2 = this.createLabel(this.appointment.invoice.insuranceCharge + "$", false);
					insuranceCharge1.setBounds(width / 2 + 40, 105 + 38, width / 2 - 120, 28);
					insuranceCharge2.setBounds(width / 2 + 40, 105 + 38, width / 2 - 120, 28);
					insuranceCharge2.setHorizontalAlignment(SwingConstants.RIGHT);
					this.add(insuranceCharge1);
					this.add(insuranceCharge2);

					JLabel patientCharge1 = this.createLabel("Patient Charge:", true);
					JLabel patientCharge2 = this.createLabel(this.appointment.invoice.patientCharge + "$", false);
					patientCharge1.setBounds(width / 2 + 40, 105 + 2 * 38, width / 2 - 120, 28);
					patientCharge2.setBounds(width / 2 + 40, 105 + 2 * 38, width / 2 - 120, 28);
					patientCharge2.setHorizontalAlignment(SwingConstants.RIGHT);
					this.add(patientCharge1);
					this.add(patientCharge2);

					JLabel discount1 = this.createLabel("Discount:", true);
					JLabel discount2 = this.createLabel(this.appointment.invoice.discount + "$", false);
					discount1.setBounds(width / 2 + 40, 105 + 3 * 38, width / 2 - 120, 28);
					discount2.setBounds(width / 2 + 40, 105 + 3 * 38, width / 2 - 120, 28);
					discount2.setHorizontalAlignment(SwingConstants.RIGHT);
					this.add(discount1);
					this.add(discount2);

					JLabel penalty1 = this.createLabel("Penalty:", true);
					JLabel penalty2 = this.createLabel(this.appointment.invoice.penalty + "$", false);
					penalty1.setBounds(width / 2 + 40, 105 + 4 * 38, width / 2 - 120, 28);
					penalty2.setBounds(width / 2 + 40, 105 + 4 * 38, width / 2 - 120, 28);
					penalty2.setHorizontalAlignment(SwingConstants.RIGHT);
					this.add(penalty1);
					this.add(penalty2);
				}

				moreOrLess.setBounds(50, 105 + 6 * 38 + 32 * 4, (width / 2 - 120) / 2, 38);
				this.setPreferredSize(new Dimension(width, 105 + 6 * 38 + 28 * 4 + 38 + 50));
				sizeSet = true;
			} else {
				moreOrLess.setBounds(50, 105, (width / 2 - 120) / 2, 38);
			}

			moreOrLess.addActionListener(e -> {
				this.removeAll();
				this.addSummary(width, !showAll);
				this.parent.updateAppointments();
				this.repaint();
			});
		} else if(this.appointment.status == Appointment.Status.SCHEDULED) {
			JButton moreOrLess = new JButton(showAll ? "Less" : "More");
			moreOrLess.setFont(new Font(moreOrLess.getFont().getName(), Font.PLAIN, 18));
			this.add(moreOrLess);

			JButton cancel = new JButton("Cancel");
			cancel.setFont(new Font(cancel.getFont().getName(), Font.PLAIN, 18));
			this.add(cancel);

			if(showAll) {
				JLabel dentist1 = this.createLabel("Dentist:", false);
				JLabel dentist2 = this.createLabel(this.appointment.employee.getFullName(), true);
				dentist2.setHorizontalAlignment(SwingConstants.RIGHT);

				dentist1.setBounds(50, 105, width / 2 - 120, 28);
				dentist2.setBounds(50, 105, width / 2 - 120, 28);
				this.add(dentist1);
				this.add(dentist2);

				moreOrLess.setBounds(50, 105 + 28 * 2, (width / 2 - 120) / 2, 38);
				cancel.setBounds(width - 255, 105 + 28 * 2, (width / 2 - 120) / 2, 38);
				this.setPreferredSize(new Dimension(width, 105 + 28 + 38 + 50));
				sizeSet = true;
			} else {
				moreOrLess.setBounds(50, 105, (width / 2 - 120) / 2, 38);
				cancel.setBounds(width - 255, 105, (width / 2 - 120) / 2, 38);
			}

			moreOrLess.addActionListener(e -> {
				this.removeAll();
				this.addSummary(width, !showAll);
				this.parent.updateAppointments();
				this.repaint();
			});

			cancel.addActionListener(e -> {
				this.appointment.status = Appointment.Status.CANCELLED;
				this.remove(type);
				this.remove(time);
				this.remove(moreOrLess);
				this.remove(cancel);
				this.addSummary(width, showAll);
				this.parent.updateAppointments();
				this.repaint();
			});
		}

		if(!sizeSet) {
			this.setPreferredSize(new Dimension(width, this.appointment.status == Appointment.Status.CANCELLED ? 130 : 38 + 105 + 50));
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(this.appointment.status.getColor());
		g.fillOval(50, 50, 30, 30);
	}

	public JLabel createLabel(String text, boolean bold) {
		JLabel label = new JLabel(text);
		label.setFont(new Font(label.getFont().getName(), bold ? Font.BOLD : Font.PLAIN, 22));
		label.setBackground(new Color(0, 0, 0, 0));
		label.setFocusable(false);
		label.setOpaque(true);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		return label;
	}

}
