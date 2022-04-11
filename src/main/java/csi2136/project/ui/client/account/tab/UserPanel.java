package csi2136.project.ui.client.account.tab;

import csi2136.project.core.User;
import csi2136.project.ui.client.AccountScreen;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class UserPanel extends JPanel {

	private AccountScreen screen;
	private Consumer<String> onEdit;

	public UserPanel(AccountScreen screen, Consumer<String> onEdit) {
		this.screen = screen;
		this.onEdit = onEdit;
		this.setLayout(null);
	}

	public void updatePanel(int width) {
		if(this.screen.newUsers == null)return;
		this.removeAll();

		int y = 0;
		boolean dark = false;

		for(User user : this.screen.newUsers) {
			UserComponent c = new UserComponent(this, user, this.screen.employees, () -> {
				this.screen.users.removeIf(e -> e.username.equals(user.username));
				this.screen.newUsers.removeIf(e -> e.username.equals(user.username));
				this.onEdit.accept(null);
				this.updatePanel(width);
			}, () -> this.onEdit.accept(user.username), dark = !dark);
			y += c.setStatic(width, 200, y);
		}

		this.setPreferredSize(new Dimension(width, y));
		this.repaint();
	}

}
