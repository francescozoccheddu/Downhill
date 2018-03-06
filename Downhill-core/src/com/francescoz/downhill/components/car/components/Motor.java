
package com.francescoz.downhill.components.car.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.car.CarData;
import com.francescoz.downhill.components.car.utils.DrawableAnchoredBody;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.physics.PhysicsWorld;
import com.francescoz.downhill.components.wrappers.Range;
import com.francescoz.downhill.components.wrappers.SmoothValue;

public final class Motor extends Component {

	public static enum Status {
		BACKWARD, BRAKE, FORWARD, NONE
	}

	private static final float FORWARD_BATTERY_CONSUMPTION = 0.002f;
	private static final float BACKWARD_BATTERY_CONSUMPTION = 0.0015f;
	private static final float BRAKE_BATTERY_CONSUMPTION = 0.0005f;
	private static final float BACKWARD_SPEED_FACTOR = -0.75f;
	private static final Range SPEED_RANGE = new Range(40, 100);
	private static final Range TORQUE_RANGE = new Range(5, 40);
	private static final Vector2 ANCHOR = new Vector2(0.1f, 0.025f);
	private static final float SLEEP_TORQUE = 0.05f;
	private static final FixtureDef FIXTURE_DEF = Data.getPhysicsWorld().createFixtureDef();
	private static final float TURBO = 1;
	private static final float SLEEP_BRAKE_TORQUE = 0.2f;

	static {
		FIXTURE_DEF.density = 4;
		FIXTURE_DEF.friction = 0.6f;
		FIXTURE_DEF.restitution = 0.05f;
		PhysicsWorld.Filters.CAR_BODY.set(FIXTURE_DEF);
	}

	private final SmoothValue torque;
	private final float absoluteTorque;
	private final float absoluteSpeed;
	private Status status;
	private final DrawableAnchoredBody drawableBody;
	private boolean statusChanged;
	private float speed;
	private final boolean autoBrake;

	public Motor(Car car, float scale, float torqueToSpeed, boolean autoBrake) {
		super(car);
		float transY = Base.BOTTOM_HEIGHT + car.jetScale + ANCHOR.y;
		CarData data = Data.getCarData();
		drawableBody = new DrawableAnchoredBody(car.base.bottom.transform, data.motor, scale, scale, ANCHOR.x, transY);
		Base base = car.base;
		torque = new SmoothValue(0.25f);
		absoluteTorque = TORQUE_RANGE.get(1 - torqueToSpeed) * scale;
		absoluteSpeed = -SPEED_RANGE.get(torqueToSpeed) * scale;
		data.applyFixtures("Motor", base.body, FIXTURE_DEF, ANCHOR.x, transY, scale, scale);
		status = null;
		setStatus(Status.NONE);
		this.autoBrake = autoBrake;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void enableBody() {

	}

	@Override
	public void fixedUpdate() {
		if (statusChanged) {
			speed = 0;
			switch (status) {
				case BACKWARD:
					speed = absoluteSpeed * BACKWARD_SPEED_FACTOR;
					break;
				case FORWARD:
					speed = absoluteSpeed;
					break;
				case BRAKE:
					break;
				case NONE:
					break;
				default:
					break;
			}
			car.base.ledEnabled = status != Status.NONE;
			torque.setActual(0);
			statusChanged = false;
		}
		float requiredBattery = 0;
		switch (status) {
			case BACKWARD:
				torque.setNext(absoluteTorque);
				requiredBattery = BACKWARD_BATTERY_CONSUMPTION;
				break;
			case BRAKE:
				torque.setNext(absoluteTorque);
				requiredBattery = BRAKE_BATTERY_CONSUMPTION;
				break;
			case FORWARD:
				torque.setNext(absoluteTorque);
				requiredBattery = FORWARD_BATTERY_CONSUMPTION;
				break;
			case NONE:
				torque.setNext(autoBrake ? absoluteTorque * SLEEP_BRAKE_TORQUE : SLEEP_TORQUE);
				break;
			default:
				assert false;
		}
		car.setWheelSpeed(speed * (1 + car.getLastTurboPower() * TURBO));
		torque.update(PhysicsWorld.TIMESTEP);
		float finalTorque = (1 - car.getInputActivity()) * torque.get();
		float power = car.requireBatteryPower(requiredBattery * finalTorque * PhysicsWorld.TIMESTEP);
		car.setWheelTorque(power * finalTorque);
	}

	@Override
	public void interpolateTransform() {
	}

	@Override
	public void render(SolidShader g) {
		drawableBody.render(g);
	}

	@Override
	public void setGravityScale(float value) {

	}

	public void setStatus(Status status) {
		if (status != this.status) {
			statusChanged = true;
			this.status = status;
		}
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void updateTransform() {

	}

}
