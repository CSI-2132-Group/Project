package csi2136.project.ui;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Utils {

	public static void setHint(JTextComponent c, String hint) {
		if(c.getText().isEmpty()) c.setText(hint);
		c.setToolTipText(hint);

		c.addFocusListener(new FocusAdapter() {
			@Override public void focusGained(FocusEvent e) { if(c.getText().equals(hint)) c.setText(""); }
			@Override public void focusLost(FocusEvent e) { if(c.getText().isEmpty()) c.setText(hint); }
		});
	}

	public static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}

}
