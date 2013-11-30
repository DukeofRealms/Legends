package com.mcthepond.champs.library.util;

import java.io.File;
import java.io.IOException;

import com.mcthepond.champs.library.configuration.file.YamlConfiguration;

public class ChampsUtil {

	public static void setup(File file) {
		if (file.exists()) return;
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {}
	}
	
	public static String formatTime(int minutes) {
		String time = "";
		int hours = minutes / 60;
		if (hours > 0) {
			int days = hours / 24;
			if (days > 0) {
				time += days + "d ";
			}
			time += (hours % 24) + "h ";
		}
		time += (minutes % 60) + "m";
		return time;
	}
	
	public static String toString(String[] arg, int startIndex) {
		String s = "";
		for (int i = startIndex; i < arg.length; i++) {
			String s1 = arg[i];
			s += s1.trim() + " ";
		}
		return s.trim();
	}
	
	public static String toString(String[] arg, int startIndex, boolean colorize) {
		if (colorize) {
			return colorize(toString(arg, startIndex));
		}
		return toString(arg, startIndex);
	}
	
	public static String colorize(String s) {
		String o = "";
		for (int i = 0; i < s.length(); i++) {
			if (i == (s.length() - 1)) {
				o+= Character.toString(s.charAt(i));
				break;
			}
			if (Character.toString(s.charAt(i)).equalsIgnoreCase("&") && isColorCode(Character.toString(s.charAt(i + 1)))) {
				o += "�";
			} else {
				o += Character.toString(s.charAt(i));
			}
		}
		return o;
	}

	public static boolean isColorCode(String s) {
		if (s.equalsIgnoreCase("a") || s.equalsIgnoreCase("b") || s.equalsIgnoreCase("c") || s.equalsIgnoreCase("d") || s.equalsIgnoreCase("e") || s.equalsIgnoreCase("f") || s.equalsIgnoreCase("k") || s.equalsIgnoreCase("l") || s.equalsIgnoreCase("m") || s.equalsIgnoreCase("n") || s.equalsIgnoreCase("o") || s.equalsIgnoreCase("0") || s.equalsIgnoreCase("1") || s.equalsIgnoreCase("2") || s.equalsIgnoreCase("3") || s.equalsIgnoreCase("4") || s.equalsIgnoreCase("5") || s.equalsIgnoreCase("6") || s.equalsIgnoreCase("7") || s.equalsIgnoreCase("8") || s.equalsIgnoreCase("9")) {
			return true;
		}
		return false;
	}

	public static void save(YamlConfiguration config, File file) {
		try {
			config.save(file);
		} catch (Exception e) {}
	}

}
