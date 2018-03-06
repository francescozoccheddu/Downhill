
package com.francescoz.downhill.components.wrappers;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public final class FloatingRange {

	public final Range fullRange;
	public final Range choosenRange;
	private float deviance;

	public FloatingRange() {
		this(new Range(0, 0), 0);
	}

	public FloatingRange(float a, float b, float deviance) {
		this(new Range(a, b), deviance);
	}

	private FloatingRange(Range fullRange, float deviance) {
		this.fullRange = fullRange;
		setDeviance(deviance);
		choosenRange = new Range(fullRange);
	}

	public float getDeviance() {
		return deviance;
	}

	public void set(float a, float b, float deviance) {
		fullRange.set(a, b);
		setDeviance(deviance);
	}

	public Range setAlpha(float alpha) {
		choosenRange.a = MathUtils.lerp(fullRange.a, fullRange.a + fullRange.b - deviance, alpha);
		choosenRange.b = deviance;
		return choosenRange;
	}

	public Range setAlpha(float alpha, Interpolation interpolation) {
		return setAlpha(interpolation.apply(alpha));
	}

	public void setByDevianceFactor(float a, float b, float devianceFactor) {
		fullRange.set(a, b);
		setDevianceFactor(devianceFactor);
	}

	public void setDeviance(float deviance) {
		this.deviance = MathUtils.clamp(deviance, 0, fullRange.getSpan());
	}

	public void setDevianceFactor(float devianceAlpha) {
		deviance = MathUtils.clamp(devianceAlpha, 0, 1) * fullRange.getSpan();
	}

}
