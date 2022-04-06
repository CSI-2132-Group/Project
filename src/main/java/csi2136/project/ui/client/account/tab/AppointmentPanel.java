package csi2136.project.ui.client.account.tab;

import csi2136.project.core.Appointment;
import csi2136.project.ui.client.AccountScreen;

import javax.swing.*;
import java.awt.*;

public class AppointmentPanel extends JPanel {

	private AccountScreen screen;
	private Runnable onEdit;

	public AppointmentPanel(AccountScreen screen, Runnable onEdit) {
		this.screen = screen;
		this.onEdit = onEdit;
		this.setLayout(null);
	}

	public void updatePanel(int width) {
		if(this.screen.newAppointments == null)return;
		this.removeAll();

		int y = 0;
		boolean dark = false;

		for(Appointment appointment : this.screen.newAppointments) {
			AppointmentComponent c = new AppointmentComponent(this, appointment, () -> {
				this.screen.appointments.removeIf(e -> e.id == appointment.id);
				this.screen.newAppointments.removeIf(e -> e.id == appointment.id);
				this.onEdit.run();
				this.updatePanel(width);
			}, () -> {
				this.updatePanel(width);
				this.onEdit.run();
			}, dark = !dark);

			if(appointment.invoice == null || appointment.treatment == null) {
				y += c.setEditable(width, 200, y);
			} else {
				y += c.setStatic(width, 200, y);
			}
		}

		this.setPreferredSize(new Dimension(width, y));
		this.repaint();
	}

}
