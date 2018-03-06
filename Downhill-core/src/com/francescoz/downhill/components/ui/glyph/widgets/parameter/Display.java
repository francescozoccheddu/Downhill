
package com.francescoz.downhill.components.ui.glyph.widgets.parameter;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.ui.glyph.fonts.DynamicText;
import com.francescoz.downhill.components.ui.glyph.widgets.Button;

abstract class Display extends Button {

	private static final float BORDER = 0.4f * Knob.SCALE;
	private static final float SCALE = Knob.SCALE;
	private final DynamicText text;

	public Display() {
		super(new DynamicText(Data.getUIData().getNormalFont(), SCALE), BORDER);
		text = (DynamicText) glyph;
	}

	public final void set(String text) {
		this.text.setText(text);
		// this.text.animate();
	}

	@Override
	public void updateAnimation(float deltaTime) {
		super.updateAnimation(deltaTime);
		text.updateAnimation(deltaTime);
	}

}
