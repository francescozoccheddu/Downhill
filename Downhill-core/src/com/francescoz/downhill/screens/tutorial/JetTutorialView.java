
package com.francescoz.downhill.screens.tutorial;

import com.francescoz.downhill.Data;

class JetTutorialView extends TutorialView {

	private static final float DURATION = 2;
	private float duration;

	JetTutorialView(TutorialViews tutorialViews, int number) {
		super(tutorialViews, 48, 49, number);
	}

	void boost(float absAmount) {
		if (done) return;
		if (absAmount > 0.4f) {
			done();
		}
		if (duration <= 0 && absAmount > 0.05f) {
			bodyText.setText(Data.getUIData().getLocale().get(50));
			duration = DURATION;
		}
	}

	@Override
	public void update(float deltaTime, float x) {
		if (duration > 0) {
			duration -= deltaTime;
			if (duration <= 0) {
				bodyText.setText(Data.getUIData().getLocale().get(49));
			}
		}
		super.update(deltaTime, x);
	}

}
