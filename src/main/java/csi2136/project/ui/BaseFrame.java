package csi2136.project.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BaseFrame extends JFrame {

	protected List<Container> panels = new ArrayList<>();
	protected int panelId = 0;

	public BaseFrame() {

	}

	public <T extends Container> T getPanel(int i, Class<T> clazz) {
		return (T)this.panels.get(i);
	}

	public <T extends Container> T getPanel(Class<T> clazz) {
		for(int i = 0; i < this.panels.size(); i++) {
			Container panel = this.panels.get(i);

			if(clazz.isAssignableFrom(panel.getClass())) {
				return (T)panel;
			}
		}
		return null;
	}

	public BaseFrame setPanel(int i) {
		this.panelId = i;

		EventQueue.invokeLater(() -> {
			this.setContentPane(this.panels.get(this.panelId));
			this.revalidate();
			this.repaint();
		});

		return this;
	}

	public BaseFrame movePanel(int i) {
		return this.setPanel(Utils.clamp(this.panelId + i, 0, this.panels.size() - 1));
	}

	public Container getActivePanel() {
		return this.panels.get(this.panelId);
	}

}
