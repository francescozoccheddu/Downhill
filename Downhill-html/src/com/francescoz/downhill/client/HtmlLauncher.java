
package com.francescoz.downhill.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.francescoz.downhill.Main;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.Locale.Formatter;
import com.google.gwt.i18n.client.NumberFormat;

public class HtmlLauncher extends GwtApplication {

	@Override
	public ApplicationListener createApplicationListener() {
		Locale.formatter = new Formatter() {

			@Override
			public String format(float value, int decimalCount) {
				String t = ".";
				for (int i = 0; i < decimalCount; i++) {
					t += '#';
				}
				NumberFormat format = NumberFormat.getFormat(t);
				return format.format(value);
			}
		};
		return new Main();
	}

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(480, 320);
	}
}