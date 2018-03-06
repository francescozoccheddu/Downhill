
package com.francescoz.downhill.components.environment.sky;

import com.francescoz.downhill.components.wrappers.Range;
import com.francescoz.downhill.components.wrappers.Transform;

final class SmallStar extends Star {

	private static final Range DURATION = new Range(0.5f, 1);
	final float x, y;
	float scale;
	float angle;
	private final Range sizeRange;
	private final float rotationSpeed;

	SmallStar(int seed, float rotationSpeed, Range size, float posX, float posY) {
		super(DURATION, seed);
		this.rotationSpeed = rotationSpeed;
		x = posX;
		y = posY;
		sizeRange = size;
	}

	@Override
	protected void updateTransform(float animatorAlpha, float angle) {
		this.angle = Transform.normalizeAngleDeg(angle * rotationSpeed);
		scale = sizeRange.get(animatorAlpha);
	}
}
