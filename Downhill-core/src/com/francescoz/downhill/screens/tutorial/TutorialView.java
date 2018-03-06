
package com.francescoz.downhill.screens.tutorial;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.car.input.CarInputHandler;
import com.francescoz.downhill.components.tween.Tween;
import com.francescoz.downhill.components.tween.TweenCallback;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.fonts.DynamicText;

class TutorialView extends View {

	private static final float COUNT_SCALE = 0.5f;
	private CarInputHandler inputHandler;
	protected final DynamicText bodyText;

	TutorialView(TutorialViews tutorialViews, int headLoc, int bodyLoc, int number) {
		super(tutorialViews);
		UIData ui = Data.getUIData();
		Locale loc = ui.getLocale();
		DynamicText ct = new DynamicText(ui.getNormalFont(), COUNT_SCALE, number + "/" + TutorialViews.VIEW_COUNT);
		DynamicText ht = new DynamicText(ui.getBoldFont(), HEAD_SCALE, loc.get(headLoc));
		bodyText = new DynamicText(ui.getNormalFont(), BODY_SCALE, loc.get(bodyLoc));
		column.add(bodyText, ht, ct);
	}

	@Override
	public void done() {
		if (done) return;
		super.done();
		bodyText.setText(Data.getUIData().getLocale().get(41));
	}

	@Override
	public void restart() {
		if (done) return;
		super.restart();
		column.setCallback(new TweenCallback() {

			@Override
			public void end(Tween tween) {
				tutorialViews.screen.car.setInputHandler(inputHandler);
			}
		});
	}

	void setInputHandler(CarInputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

}
