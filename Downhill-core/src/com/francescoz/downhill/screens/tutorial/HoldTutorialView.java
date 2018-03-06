
package com.francescoz.downhill.screens.tutorial;

import com.francescoz.downhill.Data;

class HoldTutorialView extends TutorialView {

	private static final float DURATION = 1;
	private float hold;
	private boolean pressed;
	private boolean reached;
	private final int bodyLoc;

	public HoldTutorialView(TutorialViews tutorialViews, int headLoc, int bodyLoc, int number) {
		super(tutorialViews, headLoc, bodyLoc, number);
		this.bodyLoc = bodyLoc;
		hold = DURATION;
	}

	void pressed() {
		if (!reached && !pressed) {
			pressed = true;
			hold = DURATION;
			bodyText.setText(Data.getUIData().getLocale().get(39));
		}
	}

	void released() {
		if (pressed) {
			pressed = false;
			if (reached) {
				done();
			}
			else {
				bodyText.setText(Data.getUIData().getLocale().get(bodyLoc));
			}
		}
	}

	@Override
	public void update(float deltaTime, float carX) {
		if (pressed) {
			if (!reached) {
				hold -= deltaTime;
				if (hold <= 0) {
					reached = true;
					bodyText.setText(Data.getUIData().getLocale().get(40));
				}
			}
		}
		super.update(deltaTime, carX);
	}

}
