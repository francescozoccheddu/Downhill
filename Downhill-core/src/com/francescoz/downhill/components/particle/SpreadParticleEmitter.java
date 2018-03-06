
package com.francescoz.downhill.components.particle;

import com.badlogic.gdx.math.Vector2;
import com.francescoz.downhill.components.wrappers.Range;

public final class SpreadParticleEmitter extends ParticleEmitter {

	public final Range angle;

	public SpreadParticleEmitter(ParticleBatch batch) {
		super(batch);
		angle = new Range(0, 360);
	}

	public void emit(Vector2 position, float amountPerSec, float intensity) {
		super.emit(position, amountPerSec, angle, intensity);
	}

}
