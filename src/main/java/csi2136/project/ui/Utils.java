package csi2136.project.ui;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

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

	public static String generateSalt() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		StringBuilder salt = new StringBuilder();
		Random random = new Random();

		for(int i = 0; i < 16; i++) {
			salt.append(chars.charAt(random.nextInt(chars.length())));
		}

		return salt.toString();
	}

	public static String hash(String value) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return value;
	}

}
