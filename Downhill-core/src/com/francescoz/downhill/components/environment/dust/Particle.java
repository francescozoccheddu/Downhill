
package com.francescoz.downhill.components.environment.dust;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.wrappers.simplex.SimplexAnimatorFM;

final class Particle {

	private static final int X = 0, Y = 1, Z = 2, SIZE = 3, OPACITY = 4;
	private final RandomXS128 random;
	private final SimplexAnimatorFM yAnimator;
	private final SimplexAnimatorFM sizeAnimator;
	private final SimplexAnimatorFM opacityAnimator;
	private final Dust dust;
	private final float[] data = new float[5];
	private final float layerScale;
	private float baseY;
	private final float layerHalfWidth;

	public Particle(Dust dust, int seed, float positionZ) {
		random = new RandomXS128(seed);
		yAnimator = new SimplexAnimatorFM(seed, dust.frequency, 0.1f);
		sizeAnimator = new SimplexAnimatorFM(seed + 1, dust.frequency, 0.1f);
		opacityAnimator = new SimplexAnimatorFM(seed + 2, dust.frequency, 0.1f);
		data[Z] = positionZ;
		this.dust = dust;
		layerScale = Camera.getLayerWidth(positionZ, Environment.CAMERA_MAX_Z);
		layerHalfWidth = Dust.HALF_WIDTH * layerScale;
		start(MathUtils.lerp(-layerHalfWidth, layerHalfWidth, random.nextFloat()));
	}

	public void pushData(float[] vertices, int offset) {
		int i = offset * 5;
		for (int j = 0; j < 5; j++) {
			vertices[i + j] = data[j];
		}
	}

	private void start(float x) {
		yAnimator.restart();
		sizeAnimator.restart();
		opacityAnimator.restart();
		data[X] = x;
		baseY = (random.nextFloat() * 2 - 1) * layerScale;
		data[OPACITY] = data[SIZE] = 0;
	}

	public void update(float targetX, float deltaTime, float scaleY) {
		yAnimator.update(deltaTime);
		sizeAnimator.update(deltaTime);
		opacityAnimator.update(deltaTime);
		float sizeAlpha = sizeAnimator.get();
		float velocity = dust.velocityX.get(sizeAlpha);
		data[X] += velocity;
		float x = data[X];
		float r = targetX + layerHalfWidth;
		float l = targetX - layerHalfWidth;
		if ((x > r)) {
			start(l);
		}
		else if ((x < l)) {
			start(r);
		}
		data[Y] = baseY * scaleY + yAnimator.get(dust.y);
		data[SIZE] = dust.size.get(sizeAlpha);
		data[OPACITY] = opacityAnimator.get(dust.opacity);
	}

}
