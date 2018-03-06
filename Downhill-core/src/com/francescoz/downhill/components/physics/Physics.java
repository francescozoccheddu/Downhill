
package com.francescoz.downhill.components.physics;

import com.badlogic.gdx.utils.Array;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.particle.ParticleBatch;

public final class Physics {

	private final Array<PhysicsBody> bodies;

	public Physics(float gravityX, float gravityY, ParticleBatch particleBatch) {
		PhysicsWorld physicsWorld = Data.getPhysicsWorld();
		physicsWorld.setGravity(gravityX, gravityY);
		physicsWorld.resetCounters();
		physicsWorld.setContactResolver(new ContactResolver(particleBatch));
		bodies = new Array<PhysicsBody>(0);
	}

	public void addBodies(PhysicsBody... bodies) {
		this.bodies.addAll(bodies);
	}

	public void update(float deltaTime) {
		Data.getPhysicsWorld().update(deltaTime, bodies);
	}

}
