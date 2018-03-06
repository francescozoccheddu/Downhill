
package com.francescoz.downhill.screens.tutorial;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.fonts.DynamicText;

class NoteView extends View {

	private static final float DURATION = 5;
	private float remaining;

	NoteView(TutorialViews tutorialViews, int headLoc, int bodyLoc1, int bodyLoc2) {
		super(tutorialViews);
		UIData ui = Data.getUIData();
		Locale loc = ui.getLocale();
		DynamicText ht = new DynamicText(ui.getBoldFont(), HEAD_SCALE, loc.get(headLoc));
		DynamicText bt1 = new DynamicText(ui.getNormalFont(), BODY_SCALE, loc.get(bodyLoc1));
		DynamicText bt2 = new DynamicText(ui.getNormalFont(), BODY_SCALE, loc.get(bodyLoc2));
		column.add(bt2, bt1, ht);
	}

	@Override
	public void restart() {
		super.restart();
		remaining = DURATION;
	}

	@Override
	public void update(float deltaTime, float x) {
		super.update(deltaTime, x);
		remaining -= deltaTime;
		if (remaining <= 0) {
			done();
		}
	}
}
