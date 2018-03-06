
package com.francescoz.downhill.components.car.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.physics.PhysicsWorld;

public final class Wheel extends JointedComponent {

	private static final float SHAPE_SCALE = 0.95f;
	private static final FixtureDef FIXTURE_DEF = Data.getPhysicsWorld().createFixtureDef();
	private static final WheelJointDef JOINT_DEF = Data.getPhysicsWorld().createWheelJointDef();
	private static final float MAX_DISTANCE_FACTOR = 0.75f;
	static {
		FIXTURE_DEF.density = 1;
		FIXTURE_DEF.restitution = 0.05f;
		FIXTURE_DEF.friction = 0.8f;
		JOINT_DEF.enableMotor = true;
		JOINT_DEF.localAxisA.set(Vector2.Y);
		PhysicsWorld.Filters.CAR_WHEEL.set(FIXTURE_DEF);
	}

	public static void setSpeed(Iterable<Wheel> wheels, float value) {
		for (Wheel w : wheels) {
			w.setSpeed(value);
		}
	}

	public static void setTorque(Iterable<Wheel> wheels, float value) {
		for (Wheel w : wheels) {
			w.setTorque(value);
		}
	}

	public Wheel(Car car, float scale, int index, int count, float axisHz, float axisWidth, float axisHeight) {
		super(car, scale, Data.getCarData().wheel, MathUtils.lerp(-axisWidth / 2, axisWidth / 2, index / (float) (count - 1)),
				-(axisHeight - 0.5f) * scale);
		CircleShape shape = Data.getPhysicsWorld().getCircleShape();
		shape.setRadius(SHAPE_SCALE * scale / 2);
		FIXTURE_DEF.shape = shape;
		drawableBody.body.createFixture(FIXTURE_DEF);
		JOINT_DEF.localAnchorA.set(baseLocalAnchor);
		JOINT_DEF.frequencyHz = axisHz;
		createJoint(false, JOINT_DEF);
	}

	@Override
	protected void detach() {
		super.detach();
		car.removeWheel(this);
	}

	@Override
	public void dispose() {
		super.dispose();
		car.removeWheel(this);
	}

	@Override
	public void fixedUpdate() {
		if (isJointed()) {
			if (checkJointDistance(MAX_DISTANCE_FACTOR * scale)) detach();
			else {
			}
		}
	}

	private void setSpeed(float speed) {
		if (isJointed()) {
			((WheelJoint) joint).setMotorSpeed(speed);
		}
	}

	private void setTorque(float torque) {
		if (isJointed()) {
			((WheelJoint) joint).setMaxMotorTorque(torque);
		}
	}

}
