package csi2136.project.ui.client;

import csi2136.project.core.Employee;
import csi2136.project.core.Patient;
import csi2136.project.core.User;
import csi2136.project.net.packet.PacketC2SUser;
import csi2136.project.ui.client.account.PatientPanel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

public class AccountScreen extends JTabbedPane {

	private final ClientFrame frame;

	public User user;
	public List<Employee> employees;
	public List<String> procedures;

	private ChangeListener cListener;

	public AccountScreen(ClientFrame frame) {
		this.frame = frame;
	}

	public void updateTabs() {
		if(this.user != null) {
			this.removeAll();
			this.removeChangeListener(this.cListener);

			this.addTab("+", new JComponent() {});
			this.setSelectedIndex(-1);

			for(Patient patient : this.user.patients) {
				this.addTab(patient, false);
			}

			this.addChangeListener(this.cListener = e -> this.onTabChanged(this.getSelectedIndex()));
			this.repaint();
		}
	}

	private void onTabChanged(int newIndex) {
		if(newIndex == this.getTabCount() - 1) {
			Patient patient = new Patient(this.user);
			user.patients.add(patient);
			this.addTab(patient, true);
		}
	}

	public void addTab(Patient patient, boolean edit) {
		this.setSelectedIndex(-1);
		this.insertTab(patient.getFullName(), null, new Tab(this, this.frame, patient, edit), null, this.getTabCount() - 1);
		this.setSelectedIndex(this.getTabCount() - 2);
	}

	public void removeTab(Patient patient) {
		for(int i = 0; i < this.getTabCount(); i++) {
			Component c = this.getComponentAt(i);

			if(c instanceof Tab && ((Tab)c).patient == patient) {
				this.setSelectedIndex(-1);
				this.removeTabAt(i);
				return;
			}
		}
	}

	public static class Tab extends JScrollPane {
		public final JPanel panel;
		public final Patient patient;

		public Tab(AccountScreen parent, ClientFrame frame, Patient patient, boolean edit) {
			super(null, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			this.panel = new PatientPanel(frame, patient, parent.employees, parent.procedures, edit,
				() -> {
					frame.getClient().getListener().sendPacket(new PacketC2SUser(parent.user));
				},
				() -> {
					parent.removeTab(patient);
					parent.user.patients.remove(patient);
				});

			this.patient = patient;
			this.setViewportView(this.panel);
			this.getVerticalScrollBar().setUnitIncrement(16);
		}
	}

}
