
package com.francescoz.downhill.components.environment.dust;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.RandomXS128;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.wrappers.FloatingRange;
import com.francescoz.downhill.components.wrappers.Range;

public final class Dust {

	private static final float EXTRA_HEIGHT = 5;
	static final float HALF_WIDTH = Environment.HALF_WIDTH;
	private static final float MIN_Z = -150;
	private static final Range POSITION_Z = new Range(MIN_Z, 0);
	private static final FloatingRange FREQUENCY = new FloatingRange(0.25f, 1f, 0.25f);
	private static final FloatingRange SIZE = new FloatingRange(0.05f, 0.3f, 0.2f);
	private static final FloatingRange OPACITY = new FloatingRange(0.1f, 0.5f, 0.2f);
	private static final Range Y = new Range(0.5f, 1);
	private static final Range VELOCITY_X_SCALE = new Range(0.2f, 0.5f);
	private static final RandomXS128 RANDOM = new RandomXS128();

	final Range frequency;
	final Range size;
	final Range opacity;
	final Range y;
	final Range velocityX;
	private final float sizeY;
	private final float growth;
	private final DustData data;
	private final Particle[] particles;
	private final Color color;

	public Dust(DustData data, float wind, float sizeY, float growth, Color color, int seed) {
		RANDOM.setSeed(seed);
		float windForce = Math.abs(wind);
		int count = (int) Math.ceil(data.particleCount * Interpolation.exp5Out.apply(windForce));
		particles = new Particle[count];
		float indexFactor = 1f / (count - 1f);
		frequency = new Range(FREQUENCY.setAlpha(1 - windForce));
		size = new Range(SIZE.setAlpha(RANDOM.nextFloat()));
		opacity = new Range(OPACITY.setAlpha(RANDOM.nextFloat()));
		float maxY = Y.get(RANDOM);
		y = new Range(-maxY, maxY);
		velocityX = new Range(VELOCITY_X_SCALE.getMin() * wind, VELOCITY_X_SCALE.getMax() * wind);
		this.data = data;
		this.sizeY = sizeY;
		this.growth = growth;
		this.color = new Color(color);
		for (int i = 0; i < count; i++) {
			float zAlpha = i * indexFactor;
			particles[i] = new Particle(this, i, POSITION_Z.get(zAlpha));
		}
	}

	private float getTargetHeight(float x) {
		return sizeY + (1 + Math.abs(x) * growth);
	}

	public void render() {
		data.render(color, particles);
	}

	public void update(float deltaTime, float targetX) {
		float height = getTargetHeight(targetX) + EXTRA_HEIGHT;
		for (Particle particle : particles) {
			particle.update(targetX, deltaTime, height);
		}
	}
}
