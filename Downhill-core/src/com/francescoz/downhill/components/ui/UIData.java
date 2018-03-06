
package com.francescoz.downhill.components.ui;

import com.francescoz.downhill.components.ui.glyph.fonts.FontData;

public final class UIData {

	private final FontData normalFont;
	private final FontData boldFont;
	private final Locale locale;
	private final Icons icons;

	public UIData() {
		normalFont = new FontData("roboto_normal");
		boldFont = new FontData("roboto_black");
		locale = new Locale();
		icons = new Icons();
	}

	public void dispose() {
		normalFont.dispose();
		boldFont.dispose();
		icons.dispose();

	}

	public FontData getBoldFont() {
		return boldFont;
	}

	public Icons getIcons() {
		return icons;
	}

	public Locale getLocale() {
		return locale;
	}

	public FontData getNormalFont() {
		return normalFont;
	}

}
