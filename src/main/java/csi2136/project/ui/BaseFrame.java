package csi2136.project.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BaseFrame extends JFrame {

	protected List<JPanel> panels = new ArrayList<>();
	protected int panelId = 0;

	public BaseFrame() {

	}

	public <T extends JPanel> T getPanel(int i, Class<T> clazz) {
		return (T)this.panels.get(i);
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

}
