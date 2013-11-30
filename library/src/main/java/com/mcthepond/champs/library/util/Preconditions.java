package com.mcthepond.champs.library.util;


public class Preconditions {

	public static void checkNotNull(Object obj, String message) {
		if (obj == null) {
			throw new NullPointerException(message);
		}
	}

	public static void checkArgument(boolean bool, String message) {
		if (!bool) {
			throw new IllegalArgumentException(message);
		}
	}

}
