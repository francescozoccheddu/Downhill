
package com.francescoz.downhill.components.physics;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.francescoz.downhill.components.particle.ParticleBatch;
import com.francescoz.downhill.components.particle.SpotParticleEmitter;
import com.francescoz.downhill.components.particle.SpreadParticleEmitter;
import com.francescoz.downhill.components.powerup.Powerup;

final class ContactResolver implements ContactListener {

	private static final Vector2 TEMP = new Vector2();
	private static final short COMPONENTS_GROUND_MASK = (short) (PhysicsWorld.Filters.CAR_BODY.getCategory()
			| PhysicsWorld.Filters.TERRAIN.getCategory());
	private static final short WHEELS_GROUND_MASK = (short) (PhysicsWorld.Filters.CAR_WHEEL.getCategory()
			| PhysicsWorld.Filters.TERRAIN.getCategory());
	private static final short COIN_MASK = PhysicsWorld.Filters.POWERUP.getCategory();
	private final SpreadParticleEmitter componentsEmitter;
	private final SpotParticleEmitter wheelsEmitter;

	ContactResolver(ParticleBatch particleBatch) {
		componentsEmitter = new SpreadParticleEmitter(particleBatch);
		wheelsEmitter = new SpotParticleEmitter(particleBatch);
		SpreadParticleEmitter c = componentsEmitter;
		c.acceleration.set(0, 0.25f);
		c.angle.set(0, 180);
		c.finalScale.set(5, 10);
		c.particlesPerSec = 40;
		c.initialOpacity.setByDevianceFactor(0.75f, 1f, 0.25f);
		c.initialSize.setByDevianceFactor(0.1f, 0.2f, 0.25f);
		c.life.set(0.5f, 1.5f, 0.25f);
		c.velocity.setByDevianceFactor(0.2f, 2, 0.25f);
		c.gravityScale = 2;
		SpotParticleEmitter w = wheelsEmitter;
		w.acceleration.set(0, 0.25f);
		w.finalScale.set(5, 10);
		w.particlesPerSec = 40;
		w.initialOpacity.setByDevianceFactor(0.25f, 0.75f, 0.25f);
		w.initialSize.setByDevianceFactor(0.05f, 0.1f, 0.25f);
		w.life.set(0.25f, 0.75f, 0.25f);
		w.velocity.setByDevianceFactor(0.5f, 3, 0.25f);
		w.setSpreadAngle(90);
		w.gravityScale = 2;
	}

	@Override
	public void beginContact(Contact contact) {
		Powerup c = null;
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		Body bA = a.getBody();
		Body bB = b.getBody();
		if (a.getFilterData().categoryBits == COIN_MASK) {
			c = (Powerup) bA.getUserData();
			if (!((Boolean) bB.getUserData())) return;

		}
		else if (b.getFilterData().categoryBits == COIN_MASK) {
			c = (Powerup) bB.getUserData();
			if (!((Boolean) bA.getUserData())) return;

		}
		else return;
		c.collect();
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		short mask = (short) (contact.getFixtureA().getFilterData().categoryBits | contact.getFixtureB().getFilterData().categoryBits);
		if (mask == WHEELS_GROUND_MASK) {
			Vector2 point = contact.getWorldManifold().getPoints()[0];
			float velocity = TEMP.set(contact.getFixtureA().getBody().getLinearVelocityFromWorldPoint(point))
					.sub(contact.getFixtureB().getBody().getLinearVelocityFromWorldPoint(point)).len();
			float angle = TEMP.angle();
			float friction = 1 - contact.getFriction();
			float amount = Math.min(friction * velocity, 1);
			wheelsEmitter.setDirection(point, angle + 180);
			wheelsEmitter.emit(amount, amount);
		}
		else if (mask == COMPONENTS_GROUND_MASK) {
			Vector2 point = contact.getWorldManifold().getPoints()[0];
			float velocity = TEMP.set(contact.getFixtureA().getBody().getLinearVelocityFromWorldPoint(point))
					.sub(contact.getFixtureB().getBody().getLinearVelocityFromWorldPoint(point)).len2();
			float friction = 1f / (contact.getFriction() + 1f);
			float amount = Math.min(impulse.getNormalImpulses()[0] * friction * velocity, 1);
			componentsEmitter.emit(point, Interpolation.pow2Out.apply(amount), Interpolation.exp10Out.apply(amount));
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

}
