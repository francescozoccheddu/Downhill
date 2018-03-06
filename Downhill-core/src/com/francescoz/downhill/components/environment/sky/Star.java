
package com.francescoz.downhill.components.environment.sky;

import com.francescoz.downhill.components.wrappers.Range;
import com.francescoz.downhill.components.wrappers.simplex.SimplexAnimator;

abstract class Star {

	private float angle;
	private final SimplexAnimator animator;

	Star(Range duration, int seed) {
		animator = new SimplexAnimator(seed, 0.25f);
	}

	final void update(float deltaTime) {
		animator.update(deltaTime);
		float alpha = animator.get();
		angle += deltaTime * (1 - alpha / 2);
		updateTransform(alpha, angle);
	}

	protected abstract void updateTransform(float animatorAlpha, float angle);
}
