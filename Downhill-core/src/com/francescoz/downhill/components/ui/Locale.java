
package com.francescoz.downhill.components.ui;

import java.io.BufferedReader;
import java.io.IOException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public final class Locale {

	public static interface Formatter {

		String format(float value, int decimalCount);
	}

	public static Formatter formatter;

	private static final String DEFAULT_LANGUAGE = "en";

	public static String format(float value, int decimalCount) {
		return formatter.format(value, decimalCount);
	}

	public static String formatMeters(float value) {
		float abs = Math.abs(value);
		if (abs < 100) return format(value, 1) + "m";
		if (abs < 1000) return ((int) value) + "m";
		return format(value / 1000f, 2) + "km";
	}

	private final String[] strings;

	public Locale() {
		String systemLanguage = java.util.Locale.getDefault().toString().split("_", 2)[0];
		FileHandle file = Gdx.files.internal("locale/" + systemLanguage);
		if (!file.exists()) file = Gdx.files.internal("locale/" + DEFAULT_LANGUAGE);
		BufferedReader reader = new BufferedReader(file.reader());
		Array<String> array = new Array<String>(0);
		try {
			String nextLine = reader.readLine();
			while (nextLine != null) {
				array.add(nextLine);
				nextLine = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to read '" + file.path() + "' file");
		}
		strings = array.toArray(String.class);
	}

	public String get(int index) {
		return strings[index];
	}

}
