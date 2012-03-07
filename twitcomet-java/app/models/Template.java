package models;

import java.util.HashMap;
import java.util.Map;

public class Template {
	
	private static final Template instance = new Template();
	private final Map<String, Object> data = new HashMap<String, Object>();
	
	private Template() { }
	
	public static Object get(String key) {
		return instance.data.get(key);
	}
	
	public static Object getOrElse(String key, Object defaut) {
		return has(key) ? key : defaut;
	}
	
	public static void put(String key, Object obj) {
		instance.data.put(key, obj);
	}
	
	public static boolean has(String key) {
		return instance.data.containsKey(key);
	}

}
