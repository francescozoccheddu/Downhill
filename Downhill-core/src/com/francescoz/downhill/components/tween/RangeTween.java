
package com.francescoz.downhill.components.tween;

public final class RangeTween extends Tween {

	public float from, to;

	public RangeTween() {
	}

	public float get() {
		return get(from, to);
	}

	public void startTarget(float target) {
		from = get();
		to = target;
		start();
	}

}
