
package com.francescoz.downhill.screens.menu.views;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.ui.glyph.Glyph;

abstract class WidgetBody {

	private final int localeIndex;

	public WidgetBody(int localeIndex) {
		this.localeIndex = localeIndex;
	}

	public abstract void dispose();

	public abstract Glyph getBody();

	public String getHeader() {
		return Data.getUIData().getLocale().get(localeIndex);
	}

	public abstract void update(float deltaTime);

}
