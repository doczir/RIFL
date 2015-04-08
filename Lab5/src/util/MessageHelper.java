package util;

import java.util.HashMap;
import java.util.Map;

public class MessageHelper {

	private static Map<Class<?>, String> mapping = new HashMap<>();

	public static void map(Class<?> clazz, String address) {
		mapping.put(clazz, address);
	}

	public static String getActorAddress(Class<?> clazz) {
		return mapping.get(clazz);
	}

}
