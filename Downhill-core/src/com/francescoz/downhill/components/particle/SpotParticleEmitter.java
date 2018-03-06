
package com.francescoz.downhill.components.particle;

import com.badlogic.gdx.math.Vector2;
import com.francescoz.downhill.components.wrappers.Range;

public final class SpotParticleEmitter extends ParticleEmitter {

	private final Range angle;
	private final Vector2 position;
	private float rotation;
	private float spreadAngle;

	public SpotParticleEmitter(ParticleBatch batch) {
		super(batch);
		angle = new Range(0, 0);
		position = new Vector2();
		spreadAngle = 90;
		update();
	}

	public void emit(float amountPerSec, float intensity) {
		super.emit(position, amountPerSec, angle, intensity);
	}

	public void setDirection(Vector2 position, float angleDegrees) {
		this.position.set(position);
		rotation = angleDegrees;
		update();
	}

	public void setSpreadAngle(float angleDegrees) {
		spreadAngle = angleDegrees / 2f;
		update();
	}

	private void update() {
		angle.set(rotation - spreadAngle, rotation + spreadAngle);
	}
}
