package net.capecraft.protect.utils;

import org.bukkit.plugin.Plugin;

public class Utils {
	
	Plugin instance;
	
	public Utils(Plugin instance) {
		this.instance = instance;
	}

	public static String hexEncode(String textValue) {
		char[] chars = textValue.toCharArray();
		String hex = "#";
		int ix = 0;
		while (ix < chars.length) {
			try {
				hex = hex + Integer.toHexString((int) chars[ix]);
			} catch (Exception oops) {
				return ("#00");
			}
			ix = ix + 1;
		}
		return (hex);
	}
   
	public static String hexDecode(String hexValue) {       
		String textValue = "";
		if (hexValue.startsWith("#")) {
			int ix = 1;
			while (ix < hexValue.length()) {
				try {
					textValue = textValue + (char) Integer.parseInt(hexValue.substring(ix, (ix + 2)), 16);
				} catch (Exception oops) {
					return (" ");
				}               
				ix = ix + 2;
			}           
		}
		return (textValue);
	}
}
