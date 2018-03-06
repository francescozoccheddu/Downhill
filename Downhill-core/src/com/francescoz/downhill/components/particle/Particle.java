
package com.francescoz.downhill.components.particle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

final class Particle {

	private static final Array<Particle> POOL = new Array<Particle>(0);
	private static final int X = 0, Y = 1, SIZE = 2, ALPHA = 3;

	static Particle newParticle(Vector2 position, Vector2 initialVelocity, float acceleration, float initialSize, float finalScale,
			float initialOpacity, float life, float gravityScale) {
		Particle p = POOL.size > 0 ? POOL.pop() : new Particle();
		p.data[X] = position.x;
		p.data[Y] = position.y;
		p.initialVelocity.set(initialVelocity);
		p.finalVelocity.set(initialVelocity).scl(acceleration);
		p.initialSize = initialSize;
		p.finalSize = initialSize * finalScale;
		p.initialOpacity = initialOpacity;
		p.initialLife = p.life = life;
		p.gravityScale = gravityScale;
		return p;
	}

	private final float[] data;
	private float life, initialLife, initialOpacity, initialSize, finalSize, gravityScale;
	private final Vector2 initialVelocity, finalVelocity;

	private Particle() {
		data = new float[5];
		initialVelocity = new Vector2();
		finalVelocity = new Vector2();
	}

	void free() {
		POOL.add(this);
	}

	void pushData(float[] array, int offset) {
		offset *= 4;
		array[offset] = data[0];
		array[offset + 1] = data[1];
		array[offset + 2] = data[2];
		array[offset + 3] = data[3];
	}

	boolean update(float deltaTime, Vector2 gravity) {
		life -= deltaTime;
		if (life > 0) {
			float alpha = 1 - life / initialLife;
			float x = MathUtils.lerp(initialVelocity.x, finalVelocity.x, alpha) + gravity.x * gravityScale;
			float y = MathUtils.lerp(initialVelocity.y, finalVelocity.y, alpha) + gravity.y * gravityScale;
			data[X] += x * deltaTime;
			data[Y] += y * deltaTime;
			data[SIZE] = MathUtils.lerp(initialSize, finalSize, alpha);
			data[ALPHA] = initialOpacity * (1 - alpha);
			return true;
		}
		return false;
	}
}
