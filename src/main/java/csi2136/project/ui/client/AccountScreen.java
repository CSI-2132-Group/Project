package csi2136.project.ui.client;

import csi2136.project.AccountType;
import csi2136.project.core.Appointment;
import csi2136.project.core.Employee;
import csi2136.project.core.Patient;
import csi2136.project.core.User;
import csi2136.project.net.packet.PacketC2SAppointments;
import csi2136.project.net.packet.PacketC2SEmployees;
import csi2136.project.net.packet.PacketC2SUser;
import csi2136.project.net.packet.PacketC2SUsers;
import csi2136.project.ui.client.account.PatientScreen;
import csi2136.project.ui.client.account.tab.AppointmentPanel;
import csi2136.project.ui.client.account.tab.EmployeePanel;
import csi2136.project.ui.client.account.tab.UserPanel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountScreen extends JTabbedPane {

	private final ClientFrame frame;

	public User user;
	public List<User> users;
	public List<Employee> employees;
	public List<Appointment> appointments;
	public List<User> newUsers;
	public List<Employee> newEmployees;
	public List<Appointment> newAppointments;
	public List<String> procedures;

	private ChangeListener cListener;

	public AccountScreen(ClientFrame frame) {
		this.frame = frame;
	}

	public void updateTabs() {
		if(this.user != null) {
			this.removeAll();
			this.removeChangeListener(this.cListener);

			this.newUsers = new ArrayList<>(this.users);
			this.newEmployees = new ArrayList<>(this.employees);
			this.newAppointments = new ArrayList<>(this.appointments);

			if(this.user.employee != null) {
				AccountType type = this.user.employee.type;

				if(type != AccountType.MANAGER) {
					this.newUsers = null;
				}
			} else {
				this.newUsers = null;
			}

			if(this.user.employee != null) {
				AccountType type = this.user.employee.type;

				if(type == AccountType.DENTIST || type == AccountType.RECEPTIONIST) {
					this.newEmployees = null;
				} else if(type == AccountType.MANAGER) {
					this.newEmployees.removeIf(employee -> employee.branch.id != this.user.employee.branch.id);
				}
			} else {
				this.newEmployees = null;
			}

			if(this.user.employee != null) {
				AccountType type = this.user.employee.type;

				if(type == AccountType.DENTIST) {
					this.newAppointments.removeIf(appointment -> appointment.employee.id != this.user.employee.id);
				} else if(type == AccountType.RECEPTIONIST || type == AccountType.MANAGER) {
					this.newAppointments.removeIf(appointment -> appointment.employee.branch.id != this.user.employee.branch.id);
				}
			} else {
				this.newAppointments = null;
			}

			//====================================================//

			if(this.newUsers != null) {
				UserPanel panel = new UserPanel(this,
					id -> {
						List<String> removed = id == null ? new ArrayList<>() : Collections.singletonList(id);
						this.frame.getClient().getListener().sendPacket(new PacketC2SUsers(this.users, removed));
					});
				panel.updatePanel(this.frame.getWidth());
				this.addTab("Users", new ETab(panel));
			}

			if(this.newEmployees != null) {
				EmployeePanel panel = new EmployeePanel(this,
					id -> {
						List<Integer> removed = id == null ? new ArrayList<>() : Collections.singletonList(id);
						this.frame.getClient().getListener().sendPacket(new PacketC2SEmployees(this.employees, removed));
					});
				panel.updatePanel(this.frame.getWidth());
				this.addTab("Employees", new ETab(panel));
			}

			if(this.newAppointments != null) {
				AppointmentPanel panel = new AppointmentPanel(this,
					id -> {
						List<Integer> removed = id == null ? new ArrayList<>() : Collections.singletonList(id);
						this.frame.getClient().getListener().sendPacket(new PacketC2SAppointments(this.appointments, removed));
					});
				panel.updatePanel(this.frame.getWidth());
				this.addTab("Appointments", new ETab(panel));
			}

			//====================================================//

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
		int i = this.getTabCount() - 1;
		this.insertTab(patient.getFullName(), null, new PTab(this, this.frame, patient, edit, i), null, i);
		this.setSelectedIndex(this.getTabCount() - 2);
	}

	public void removeTab(Patient patient) {
		for(int i = 0; i < this.getTabCount(); i++) {
			Component c = this.getComponentAt(i);

			if(c instanceof PTab && ((PTab)c).patient == patient) {
				this.setSelectedIndex(-1);
				this.removeTabAt(i);
				return;
			}
		}
	}

	public static class PTab extends JScrollPane {
		public final JPanel panel;
		public final Patient patient;

		public PTab(AccountScreen parent, ClientFrame frame, Patient patient, boolean edit, int i) {
			super(null, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			this.panel = new PatientScreen(frame, parent, patient, edit,
				() -> {
					frame.getClient().getListener().sendPacket(new PacketC2SUser(parent.user));
				}, () -> {
					parent.removeTab(patient);
					parent.user.patients.remove(patient);
				}, s -> {
					parent.setTitleAt(i, s);
				});

			this.patient = patient;
			this.setViewportView(this.panel);
			this.getVerticalScrollBar().setUnitIncrement(16);
		}
	}

	public static class ETab extends JScrollPane {
		public ETab(JPanel panel) {
			super(null, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			this.setViewportView(panel);
			this.getVerticalScrollBar().setUnitIncrement(16);
		}
	}

}
