
package com.francescoz.downhill.components.tween;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class Tween {

	private boolean enabled;
	private float alpha;
	public Interpolation easing;
	public TweenCallback callback;
	public float duration;

	public Tween() {
		easing = Interpolation.linear;
	}

	final float get(float from, float to) {
		return MathUtils.lerp(from, to, easing.apply(alpha));
	}

	public float getEasedAlpha() {
		return easing.apply(alpha);
	}

	public float getLinearAlpha() {
		return alpha;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public final void kill() {
		enabled = false;
	}

	public final void start() {
		enabled = true;
		alpha = 0;
	}

	public final void terminate() {
		enabled = false;
		alpha = 1;
	}

	public final void update(float deltaTime) {
		if (enabled) {
			alpha += deltaTime / duration;
			if (alpha >= 1) {
				enabled = false;
				alpha = 1;
				if (callback != null) callback.end(this);
			}
		}
	}
}
