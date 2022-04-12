package csi2136.project.ui.client.account.tab;

import csi2136.project.core.Appointment;
import csi2136.project.ui.client.AccountScreen;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class AppointmentPanel extends JPanel {

	private AccountScreen screen;
	private Consumer<Integer> onEdit;

	public AppointmentPanel(AccountScreen screen, Consumer<Integer> onEdit) {
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
				this.onEdit.accept(appointment.id);
				this.updatePanel(width);
			}, () -> this.onEdit.accept(null), dark = !dark);

			if(appointment.invoice == null || appointment.treatment == null) {
				y += c.setEditable(width, 200, y);
			} else {
				y += c.setStatic(width, 200, y);
			}
		}

		if(!this.getPreferredSize().equals(new Dimension(width, y)))  {
			this.setPreferredSize(new Dimension(width, y));
		}

		this.repaint();
	}

}
