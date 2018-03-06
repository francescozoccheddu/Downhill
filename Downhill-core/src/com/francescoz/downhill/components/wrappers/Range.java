
package com.francescoz.downhill.components.wrappers;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.RandomXS128;

public final class Range {

	private static Range choose(float alpha, Range[] ranges) {
		float span = 0;
		for (Range r : ranges) {
			span += r.getSpan();
		}
		alpha *= span;
		span = 0;
		for (Range r : ranges) {
			float actualSpan = r.getSpan();
			span += actualSpan;
			if (alpha <= span) { return r; }
		}
		return null;
	}

	public static Range choose(RandomXS128 random, Range[] ranges) {
		return choose(random.nextFloat(), ranges);
	}

	public static float loop(float value, float min, float max) {
		float range = max - min;
		while (value < min) {
			value += range;
		}
		while (value > max) {
			value -= range;
		}
		return value;
	}

	float a, b;

	public Range() {
		a = b = 0;
	}

	public Range(float a, float b) {
		set(a, b);
	}

	public Range(Range clone) {
		set(clone);
	}

	public float get(float alpha) {
		return a + b * alpha;
	}

	private float get(float alpha, Interpolation interpolation) {
		return a + b * interpolation.apply(alpha);
	}

	public float get(RandomXS128 random) {
		return get(random.nextFloat());
	}

	public float get(RandomXS128 random, Interpolation interpolation) {
		return get(random.nextFloat(), interpolation);
	}

	public float getAlpha(float value) {
		return (value - a) / b;
	}

	public float getMax() {
		return a + b;
	}

	public float getMin() {
		return a;
	}

	public int getRounded(float alpha) {
		return Math.round(get(alpha));
	}

	public int getRounded(RandomXS128 random) {
		return Math.round(get(random));
	}

	public float getRoundedMax() {
		return Math.round(getMax());
	}

	public float getRoundedMin() {
		return Math.round(getMin());
	}

	public float getSpan() {
		return Math.abs(b);
	}

	public void set(float a, float b) {
		if (a < b) {
			this.a = a;
			this.b = b - a;
		}
		else {
			this.a = b;
			this.b = a - b;
		}
	}

	private void set(Range clone) {
		a = clone.a;
		b = clone.b;
	}

}
