
package com.francescoz.downhill.components.car.input;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.francescoz.downhill.components.wrappers.SmoothValue;

final class Finger {

	private static final float DOUBLE_TAP_DELAY = 0.3f;
	private static final Interpolation INTERPOLATION = Interpolation.pow4Out;
	private float oldY;
	private float newY;
	private float doubleTapTime;
	private boolean justPressed, doubleTapped;
	private final SmoothValue deltaY;
	private final SmoothValue activity;

	public Finger() {
		deltaY = new SmoothValue(1 / 6f, 0, 0);
		activity = new SmoothValue(1 / 4f, 0, 0);
	}

	public float getActivity() {
		return activity.get();
	}

	public float getDeltaY() {
		return deltaY.get();
	}

	public boolean isDoubleTapped() {
		return doubleTapped;
	}

	public void press(float y) {
		newY = oldY = y;
		doubleTapped = false;
		justPressed = true;
	}

	public void release() {
		doubleTapped = false;
		newY = oldY = 0;
		deltaY.reach();
		activity.reach();
	}

	public void reset() {
		doubleTapTime = 0;
		newY = oldY = 0;
		doubleTapped = false;
		justPressed = false;
		deltaY.reach();
		activity.reach();
	}

	public void setY(float y) {
		oldY = newY;
		newY = y;
	}

	public void update(float deltaTime) {
		doubleTapTime -= deltaTime;
		deltaY.update(deltaTime);
		activity.update(deltaTime);
		if (justPressed) {
			justPressed = false;
			doubleTapped = doubleTapTime > 0;
			doubleTapTime = DOUBLE_TAP_DELAY;
		}
		float newDY = MathUtils.clamp(((newY - oldY) / (deltaTime * 60)), -1, 1);
		newDY = (newDY > 0 ? 1 : -1) * INTERPOLATION.apply(Math.abs(newDY));
		float oldDY = deltaY.get();
		float newAbsDY = Math.abs(newDY);
		float oldAbsDY = Math.abs(oldDY);
		if (newAbsDY > oldAbsDY) deltaY.setActual(newDY);
		if (newAbsDY > activity.get()) activity.setActual(newAbsDY);
	}
}
