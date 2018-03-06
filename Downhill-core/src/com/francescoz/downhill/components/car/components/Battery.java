
package com.francescoz.downhill.components.car.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.physics.PhysicsWorld.Filters;
import com.francescoz.downhill.components.wrappers.Range;
import com.francescoz.downhill.components.wrappers.simplex.SimplexAnimator;

public final class Battery extends JointedComponent {

	private static final Vector2 ANCHOR = new Vector2(-0.15f, 0.01f);
	private static final Range ANIMATOR = new Range(0.5f, 1.5f);
	private static final float LOW_BATTERY_FACTOR = 50;
	private static final FixtureDef FIXTURE_DEF = Data.getPhysicsWorld().createFixtureDef();
	private static final RevoluteJointDef JOINT_DEF = Data.getPhysicsWorld().createRevoluteJointDef();
	private static final float JOINT_RELAX_FACTOR = 0.4f;
	private static final float MAX_ANGLE = 45 * MathUtils.degreesToRadians;
	private static final float MAX_DISTANCE_FACTOR = 2f;

	static {
		FIXTURE_DEF.density = 2;
		FIXTURE_DEF.restitution = 0.05f;
		FIXTURE_DEF.friction = 0.2f;
		JOINT_DEF.enableLimit = true;
		JOINT_DEF.referenceAngle = 0;
		JOINT_DEF.enableMotor = false;
		Filters.CAR_BODY.set(FIXTURE_DEF);
	}

	public static void addPower(Iterable<Battery> batteries, float amount) {
		for (Battery b : batteries) {
			amount -= b.addPower(amount);
		}
	}

	public static float requirePower(Iterable<Battery> batteries, float amount) {
		if (amount <= 0) return 0;
		float available = 0;
		for (Battery b : batteries) {
			available += b.getAnimatedPower();
		}
		float power = Math.min(1, available / (amount * LOW_BATTERY_FACTOR));
		amount *= power;
		for (Battery b : batteries) {
			amount -= b.addPower(-amount);
		}
		return power;
	}

	private final Color LEDcolor;
	private final SimplexAnimator animator;
	private float power;

	public Battery(Car car, float scale, int index) {
		super(car, scale, Data.getCarData().battery, ANCHOR.x + scale * index + scale / 2,
				Base.BOTTOM_HEIGHT + car.jetScale + ANCHOR.y, "Battery", FIXTURE_DEF);
		animator = new SimplexAnimator(0);
		animator.frequency = 5;
		LEDcolor = new Color(0, 0, 0, 1);
		power = scale;
		JOINT_DEF.localAnchorB.set(baseLocalAnchor);
		createJoint(true, JOINT_DEF);
	}

	private float addPower(float amount) {
		float old = power;
		power = MathUtils.clamp(power + amount, 0, scale);
		return Math.abs(power - old);
	}

	@Override
	protected void detach() {
		super.detach();
		car.removeBattery(this);
	}

	@Override
	public void dispose() {
		super.dispose();
		car.removeBattery(this);
	}

	@Override
	public void fixedUpdate() {
		if (isJointed()) {
			if (checkJointDistance(MAX_DISTANCE_FACTOR * scale) || checkJointAngle(MAX_ANGLE, JOINT_RELAX_FACTOR)) {
				detach();
			}
		}
	}

	private float getAnimatedPower() {
		return power * animator.get(ANIMATOR);
	}

	@Override
	public void render(SolidShader g) {
		super.render(g);
		if (isJointed()) {
			g.setColor(LEDcolor);
		}
		g.render(Data.getCarData().batteryLED);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		animator.update(deltaTime);
		if (isJointed()) {
			float col = 1 - power / scale;
			if (col < 0.5f) {
				LEDcolor.g = 1;
				LEDcolor.r = 2 * col;
			}
			else {
				LEDcolor.r = 1;
				LEDcolor.g = 1f - 2 * (col - 0.5f);
			}
		}
	}

}