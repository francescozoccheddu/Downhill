
package com.francescoz.downhill.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.francescoz.downhill.Main;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.Locale.Formatter;

public class DesktopLauncher {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Locale.formatter = new Formatter() {

			@Override
			public String format(float value, int decimalCount) {
				return String.format("%." + decimalCount + "f", value);
			}
		};
		new LwjglApplication(new Main(), config);
	}
}
