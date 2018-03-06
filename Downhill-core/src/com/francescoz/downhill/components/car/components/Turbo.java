
package com.francescoz.downhill.components.car.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.car.CarData;
import com.francescoz.downhill.components.car.utils.DrawableAnchoredBody;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.particle.ParticleBatch;
import com.francescoz.downhill.components.particle.SpotParticleEmitter;
import com.francescoz.downhill.components.physics.PhysicsWorld;
import com.francescoz.downhill.components.physics.PhysicsWorld.Filters;
import com.francescoz.downhill.components.wrappers.SmoothValue;
import com.francescoz.downhill.components.wrappers.Transform;

public final class Turbo extends Component {

	private static final float FORCE = 0.3f;
	private static final float BATTERY_CONSUMPTION = 0.3f;
	private static final Vector2 ANCHOR = new Vector2(-0.485f, 0.05f);
	private static final Vector2 POT_LOCAL_POINT = new Vector2(0, 0.2f);
	private static final Vector2 TEMP_VECTOR = new Vector2();
	private static final FixtureDef FIXTURE_DEF = Data.getPhysicsWorld().createFixtureDef();

	static {
		FIXTURE_DEF.density = 4;
		FIXTURE_DEF.friction = 0.6f;
		FIXTURE_DEF.restitution = 0.05f;
		Filters.CAR_BODY.set(FIXTURE_DEF);
	}

	private boolean enabled;
	private final Vector2 potLocalPoint;
	private final SpotParticleEmitter emitter;
	private final float scale;
	private final DrawableAnchoredBody drawableBody;
	private final Base base;
	private float power;
	public float lastPower;
	private final SmoothValue charge;

	public Turbo(Car car, float scale, ParticleBatch particleBatch) {
		super(car);
		base = car.base;
		this.scale = scale;
		float anchorY = Base.BOTTOM_HEIGHT + car.jetScale + ANCHOR.y;
		CarData data = Data.getCarData();
		drawableBody = new DrawableAnchoredBody(car.base.bottom.transform, data.turbo, scale, scale, ANCHOR.x, anchorY);
		potLocalPoint = new Vector2(POT_LOCAL_POINT).scl(scale).add(ANCHOR.x, anchorY);
		data.applyFixtures("Turbo", base.body, FIXTURE_DEF, ANCHOR.x, anchorY, scale, scale);
		emitter = new SpotParticleEmitter(particleBatch);
		emitter.initialSize.setByDevianceFactor(scale / 4f, scale / 2f, 0.5f);
		emitter.acceleration.set(0.1f, 0.25f);
		emitter.initialOpacity.setByDevianceFactor(0.5f, 1f, 0.5f);
		emitter.life.setByDevianceFactor(0.1f, 0.2f, 0.5f);
		emitter.velocity.setByDevianceFactor(0.025f, 0.05f, 0.5f);
		emitter.finalScale.set(2, 4);
		emitter.setSpreadAngle(90);
		emitter.particlesPerSec = 50;
		emitter.gravityScale = 0;
		power = 0;
		charge = new SmoothValue(2f);
		setEnabled(false);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void enableBody() {

	}

	@Override
	public void fixedUpdate() {
		charge.update(PhysicsWorld.TIMESTEP);
		if (enabled) {
			float charge = this.charge.get();
			float power = car.requireBatteryPower(scale * BATTERY_CONSUMPTION * PhysicsWorld.TIMESTEP * charge) * charge;
			lastPower = power * scale;
			this.power += power;
			TEMP_VECTOR.set(Vector2.X).scl(power * FORCE * scale).rotateRad(Transform.normalizeAngleRad(base.body.getAngle()));
			base.body.applyLinearImpulse(TEMP_VECTOR, base.body.getWorldPoint(potLocalPoint), true);
			base.body.applyLinearImpulse(TEMP_VECTOR, base.body.getWorldCenter(), true);
		}
		else lastPower = 0;
	}

	@Override
	public void interpolateTransform() {
	}

	@Override
	public void render(SolidShader g) {
		drawableBody.render(g);
	}

	public void setEnabled(boolean value) {
		enabled = value;
		charge.setNext(enabled ? 0 : 1);
	}

	@Override
	public void setGravityScale(float value) {

	}

	@Override
	public void update(float deltaTime) {
		if (power > 0) {
			Transform t = base.bottom.transform;
			float rotDeg = t.rotationDegrees;
			Vector3 pos = t.position;
			TEMP_VECTOR.set(potLocalPoint).rotate(rotDeg).add(pos.x, pos.y);
			emitter.setDirection(TEMP_VECTOR, rotDeg + 180);
			emitter.emit(power, power);
		}
		power = 0;
	}

	@Override
	public void updateTransform() {
	}

}
