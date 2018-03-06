
package com.francescoz.downhill.components.wrappers;

import com.badlogic.gdx.math.MathUtils;

public final class SmoothValue {

	private float actual;
	private float next;
	private final float duration;

	public SmoothValue(float duration) {
		this(duration, 0, 0);
	}

	public SmoothValue(float duration, float actual, float next) {
		this.duration = Math.max(duration, 0);
		this.actual = actual;
		this.next = next;
	}

	public void addActual(float value) {
		actual += value;
	}

	public void addNext(float amount) {
		next += amount;
	}

	public void clampActual(float min, float max) {
		actual = MathUtils.clamp(actual, min, max);
	}

	public float get() {
		return actual;
	}

	public float getNext() {
		return next;
	}

	public void reach() {
		actual = next;
	}

	public void setActual(float value) {
		actual = value;
	}

	public void setBoth(float value) {
		actual = next = value;
	}

	public void setNext(float value) {
		next = value;
	}

	public void update(float deltaTime) {
		actual = MathUtils.lerp(actual, next, Math.min(deltaTime / duration, 1));
	}
}
