
package com.francescoz.downhill.components.particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.francescoz.downhill.Data;

public final class ParticleBatch {

	private final Array<Particle> particles;
	private final Color color;
	private final Vector2 gravity;

	public ParticleBatch(Color color, Vector2 gravity) {
		particles = new Array<Particle>(0);
		this.gravity = gravity;
		this.color = new Color(color);
	}

	void addParticle(Vector2 position, Vector2 initialVelocity, float acceleration, float initialSize, float finalSize,
			float initialOpacity, float life, float gravityScale) {
		particles.add(
				Particle.newParticle(position, initialVelocity, acceleration, initialSize, finalSize, initialOpacity, life, gravityScale));
	}

	public void dispose() {
		for (Particle p : particles)
			p.free();
	}

	public void render() {
		Data.getParticleData().render(particles, color);
	}

	public void update(float deltaTime) {
		Data.getParticleData().update(deltaTime, particles, gravity);
	}
}
