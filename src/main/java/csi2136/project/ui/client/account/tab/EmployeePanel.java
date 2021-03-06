package csi2136.project.ui.client.account.tab;

import csi2136.project.core.Employee;
import csi2136.project.ui.client.AccountScreen;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class EmployeePanel extends JPanel {

	private AccountScreen screen;
	private Consumer<Integer> onEdit;

	public EmployeePanel(AccountScreen screen, Consumer<Integer> onEdit) {
		this.screen = screen;
		this.onEdit = onEdit;
		this.setLayout(null);
	}

	public void updatePanel(int width) {
		if(this.screen.newEmployees == null)return;
		this.removeAll();

		int y = 0;
		boolean dark = false;

		for(Employee employee : this.screen.newEmployees) {
			EmployeeComponent c = new EmployeeComponent(this, employee, () -> {
				this.screen.employees.removeIf(e -> e.id == employee.id);
				this.screen.newEmployees.removeIf(e -> e.id == employee.id);
				this.onEdit.accept(employee.id);
				this.updatePanel(width);
			}, () -> this.onEdit.accept(null), dark = !dark);

			y += c.setStatic(width, 200, y);
		}

		this.setPreferredSize(new Dimension(width, y));
		this.repaint();
	}

}
