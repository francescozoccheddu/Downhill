
package com.francescoz.downhill;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.Locale.Formatter;
import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		Locale.formatter = new Formatter() {

			@Override
			public String format(float value, int decimalCount) {
				return String.format("%." + decimalCount + "f", value);
			}
		};
		initialize(new Main(), config);
	}
}
