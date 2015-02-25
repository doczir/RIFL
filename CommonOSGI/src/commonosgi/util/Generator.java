package commonosgi.util;

import java.util.Date;
import java.util.Random;

public class Generator {

	private static String characters = "abcdefghijklmnopqrstuvwxyz";

	public static String generateString(Random rng, int length) {
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}

	public static Date generateDate(Random rng, long end, long begin) {
		long diff = end - begin + 1;
		long date = begin + (long) (Math.random() * diff);
		return new Date(date);
	}
	
	public static boolean generateBoolean() {
		double d = Math.random();
		
		if (d >= 0.5f) {
			return true;
		} else {
			return false;
		}
	}
}
