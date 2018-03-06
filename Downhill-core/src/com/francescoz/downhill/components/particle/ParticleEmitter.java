
package com.francescoz.downhill.components.particle;

import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.wrappers.FloatingRange;
import com.francescoz.downhill.components.wrappers.Range;

class ParticleEmitter {

	private static final Vector2 TEMP = new Vector2();
	private static final RandomXS128 RANDOM = new RandomXS128(0);
	public final FloatingRange velocity;
	public final Range acceleration;
	public final FloatingRange initialSize;
	public final Range finalScale;
	public final FloatingRange initialOpacity;
	public final FloatingRange life;
	public float particlesPerSec;
	public float gravityScale;
	private final ParticleBatch batch;

	ParticleEmitter(ParticleBatch batch) {
		velocity = new FloatingRange(1, 2, 0.3f);
		acceleration = new Range(0, 0.5f);
		initialSize = new FloatingRange(0.25f, 0.5f, 0.1f);
		finalScale = new Range(1.5f, 3f);
		initialOpacity = new FloatingRange(0.5f, 1, 0.2f);
		life = new FloatingRange(1, 4, 1);
		gravityScale = 1;
		this.batch = batch;
	}

	final void emit(Vector2 position, float amountPerSec, Range angle) {
		float count = amountPerSec * Data.getGraphics().getDeltaTime() * particlesPerSec;
		int floor = (int) count;
		count -= floor;
		if (RANDOM.nextFloat() <= count) {
			floor++;
		}
		emit(position, floor, angle);
	}

	final void emit(Vector2 position, float amountPerSec, Range angle, float intensity) {
		float count = amountPerSec * Data.getGraphics().getDeltaTime() * particlesPerSec;
		int floor = (int) count;
		count -= floor;
		if (RANDOM.nextFloat() <= count) {
			floor++;
		}
		emit(position, floor, angle, intensity);
	}

	final void emit(Vector2 position, int amount, Range angle) {
		for (int i = 0; i < amount; i++) {
			TEMP.set(Vector2.X).rotate(angle.get(RANDOM)).scl(velocity.fullRange.get(RANDOM));
			batch.addParticle(position, TEMP, acceleration.get(RANDOM), initialSize.fullRange.get(RANDOM), finalScale.get(RANDOM),
					initialOpacity.fullRange.get(RANDOM), life.fullRange.get(RANDOM), gravityScale);
		}
	}

	final void emit(Vector2 position, int amount, Range angle, float intensity) {
		for (int i = 0; i < amount; i++) {
			TEMP.set(Vector2.X).rotate(angle.get(RANDOM)).scl(velocity.setAlpha(intensity).get(RANDOM));
			batch.addParticle(position, TEMP, acceleration.get(RANDOM), initialSize.setAlpha(intensity).get(RANDOM), finalScale.get(RANDOM),
					initialOpacity.setAlpha(intensity).get(RANDOM), life.setAlpha(intensity).get(RANDOM), gravityScale);
		}
	}

}
