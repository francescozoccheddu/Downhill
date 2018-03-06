
package com.francescoz.downhill.components.car.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.environment.terrain.Terrain;
import com.francescoz.downhill.components.particle.ParticleBatch;
import com.francescoz.downhill.components.particle.SpotParticleEmitter;
import com.francescoz.downhill.components.physics.PhysicsWorld;
import com.francescoz.downhill.components.physics.PhysicsWorld.Filters;
import com.francescoz.downhill.components.wrappers.SmoothValue;
import com.francescoz.downhill.components.wrappers.Transform;

public final class Jet extends JointedComponent {

	private static final float BATTERY_CONSUMPTION = 0.005f;
	private static final float MAX_HEIGHT = 2f;
	private static final float BOOST_FACTOR = -1f;
	private static final FixtureDef FIXTURE_DEF = Data.getPhysicsWorld().createFixtureDef();
	private static final RevoluteJointDef JOINT_DEF = Data.getPhysicsWorld().createRevoluteJointDef();
	private static final float JOINT_RELAX_FACTOR = 0.75f;
	private static final float MAX_ANGLE = 70 * MathUtils.degreesToRadians;
	private static final Vector2 POT_LOCAL_POINT = new Vector2(0.375f, 0);
	private static final Vector2 TEMP_VECTOR = new Vector2();
	private static final float MAX_DISTANCE_FACTOR = 2;
	private static final float MIN_BOOST = 0.05f;
	private static final float SUSTAIN = 1 / 6f;
	private static final float RADIUS = 0.5f;

	static {
		FIXTURE_DEF.density = 1;
		FIXTURE_DEF.restitution = 0.05f;
		FIXTURE_DEF.friction = 0.2f;
		Filters.CAR_BODY.set(FIXTURE_DEF);
		JOINT_DEF.enableLimit = true;
		JOINT_DEF.referenceAngle = 0;
		JOINT_DEF.enableMotor = false;
	}

	private final Vector2 potLocalPoint;
	private final SpotParticleEmitter emitter;
	private final SmoothValue upBoost, downBoost;
	private float upEmitterBoost, downEmitterBoost;
	private final Terrain ground;

	public Jet(Car car, float scale, boolean inverted, ParticleBatch particleBatch, Environment environment) {
		super(car, scale, Data.getCarData().jet, (inverted ? -0.5f : 0.5f), Base.BOTTOM_HEIGHT + scale / 2);
		float absScale = this.scale;
		potLocalPoint = new Vector2(POT_LOCAL_POINT).scl(absScale);
		if (inverted) {
			potLocalPoint.x *= -1;
			baseLocalAnchor.x *= -1;
			drawableBody.transform.scale.x = -drawableBody.transform.scale.x;
		}
		emitter = new SpotParticleEmitter(particleBatch);
		emitter.acceleration.set(0.25f, 0.5f);
		emitter.initialOpacity.setByDevianceFactor(0.75f, 1, 0.5f);
		emitter.life.set(0.05f, 0.15f, 0.1f);
		emitter.velocity.setByDevianceFactor(1, 2, 0.75f);
		emitter.finalScale.set(3, 6);
		emitter.setSpreadAngle(90);
		emitter.particlesPerSec = 300;
		emitter.gravityScale = 0.1f;
		emitter.initialSize.setByDevianceFactor(absScale / 6f, absScale / 3f, 0.5f);
		float anchorY = Base.BOTTOM_HEIGHT + absScale / 2;
		Data.getCarData().applyFixtures("jet", drawableBody.body, FIXTURE_DEF, 0, 0, inverted ? -scale : scale, scale);
		baseLocalAnchor.set((inverted ? -0.5f : 0.5f), anchorY);
		JOINT_DEF.localAnchorB.set(baseLocalAnchor);
		createJoint(true, JOINT_DEF);
		ground = environment.getGround();
		upBoost = new SmoothValue(SUSTAIN, 0, 0);
		downBoost = new SmoothValue(SUSTAIN, 0, 0);
	}

	public void fakeBoost(float amount) {
		downEmitterBoost += amount;
	}

	@Override
	public void fixedUpdate() {
		if (isJointed()) {
			upBoost.update(PhysicsWorld.TIMESTEP);
			downBoost.update(PhysicsWorld.TIMESTEP);
			if (checkJointDistance(MAX_DISTANCE_FACTOR * scale) || checkJointAngle(MAX_ANGLE, JOINT_RELAX_FACTOR)) {
				detach();
			}
			else {
				Vector2 point = base.body.getWorldPoint(baseLocalAnchor);
				float height = point.y - ground.getMaxHeight(point.x, RADIUS);
				float airFactor = 1 - Math.min(height / MAX_HEIGHT, 1);
				float b = upBoost.get();
				if (b > MIN_BOOST) {
					float boost = b * airFactor;
					float power = car.requireBatteryPower(boost * scale * BATTERY_CONSUMPTION);
					upEmitterBoost += boost * power;
					TEMP_VECTOR.set(0, power * boost * scale * BOOST_FACTOR)
							.rotateRad(Transform.normalizeAngleRad(drawableBody.body.getAngle()));
					drawableBody.body.applyLinearImpulse(TEMP_VECTOR, point, true);
					base.body.applyLinearImpulse(TEMP_VECTOR, point, true);
				}
				b = downBoost.get();
				if (b > MIN_BOOST) {
					float boost = b * airFactor;
					float power = car.requireBatteryPower(boost * scale * BATTERY_CONSUMPTION);
					downEmitterBoost += boost * power;
					TEMP_VECTOR.set(0, power * -boost * scale * BOOST_FACTOR)
							.rotateRad(Transform.normalizeAngleRad(drawableBody.body.getAngle()));
					drawableBody.body.applyLinearImpulse(TEMP_VECTOR, point, true);
					base.body.applyLinearImpulse(TEMP_VECTOR, point, true);
				}
			}
		}
	}

	public void setBoost(float amount) {
		if (amount > 0) upBoost.addActual(amount);
		else downBoost.addActual(-amount);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (isJointed()) {
			if (upEmitterBoost + downEmitterBoost > 0) {
				Transform t = drawableBody.transform;
				float rot = t.rotationDegrees;
				Vector3 pos = t.position;
				TEMP_VECTOR.set(potLocalPoint).rotate(rot).add(pos.x, pos.y);
				if (upEmitterBoost > 0) {
					emitter.setDirection(TEMP_VECTOR, rot + 90);
					emitter.emit(upEmitterBoost, upEmitterBoost);
				}
				if (downEmitterBoost > 0) {
					emitter.setDirection(TEMP_VECTOR, rot - 90);
					emitter.emit(downEmitterBoost, downEmitterBoost);
				}
			}
		}
		upEmitterBoost = downEmitterBoost = 0;
	}

}
