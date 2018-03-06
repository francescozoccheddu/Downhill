
package com.francescoz.downhill.components.car;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.francescoz.downhill.components.car.components.Base;
import com.francescoz.downhill.components.car.components.Battery;
import com.francescoz.downhill.components.car.components.Component;
import com.francescoz.downhill.components.car.components.Headlight;
import com.francescoz.downhill.components.car.components.Jet;
import com.francescoz.downhill.components.car.components.Motor;
import com.francescoz.downhill.components.car.components.Turbo;
import com.francescoz.downhill.components.car.components.Wheel;
import com.francescoz.downhill.components.car.input.CarInputHandler;
import com.francescoz.downhill.components.car.input.CarInputProcessor;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.particle.ParticleBatch;
import com.francescoz.downhill.components.physics.Physics;
import com.francescoz.downhill.components.physics.PhysicsBody;
import com.francescoz.downhill.components.tween.Tween;
import com.francescoz.downhill.components.tween.TweenCallback;

public class Car implements PhysicsBody {

	private static final Array<Component> DISPOSABLES = new Array<Component>(0);
	private static final float WIDTH = 1;
	public final Vector2 initialPosition;
	private final Array<Component> components;
	private final Array<Battery> batteries;
	private final Array<Wheel> wheels;
	public final Base base;
	private final Turbo turbo;
	private final Motor motor;
	private final Headlight headlight;
	private final Jet rjet;
	private final Jet ljet;
	private final Color color;
	public final float jetScale;
	private final CarInputProcessor input;
	private boolean landed;
	private final Tween landing;
	private final float height;

	public Car(float x, float y, CarDefinition definition, Environment environment) {
		this(x, y, definition, environment, environment.getPhysics(), true, true, false);
	}

	public Car(float x, float y, CarDefinition definition, Environment environment, boolean turboEnabled, boolean jetEnabled,
			boolean autoBrake) {
		this(x, y, definition, environment, environment.getPhysics(), turboEnabled, jetEnabled, autoBrake);
	}

	private Car(float x, float y, CarDefinition def, Environment environment, Physics physics, boolean turboEnabled, boolean jetEnabled,
			boolean autoBrake) {
		input = new CarInputProcessor(this);
		float wheelScale = def.getWheelScale();
		height = wheelScale;
		initialPosition = new Vector2(x, environment.getGround().getMaxHeight(x, WIDTH) + wheelScale + y);
		color = new Color(environment.getGroundColor());
		ParticleBatch particleBatch = environment.getParticleBatch();
		components = new Array<Component>(0);
		batteries = new Array<Battery>(0);
		wheels = new Array<Wheel>(0);
		jetScale = def.jetScale.get();
		components.add(base = new Base(this, jetScale, color));
		if (turboEnabled) components.add(turbo = new Turbo(this, def.turboScale.get(), particleBatch));
		else turbo = null;
		components.add(motor = new Motor(this, def.motorScale.get(), def.motorTorqueToSpeed.get(), autoBrake));
		if (def.headlightScale.getAlphaFloat() > 0) components.add(headlight = new Headlight(this, def.headlightScale.get()));
		else headlight = null;
		if (jetEnabled) {
			components.add(rjet = new Jet(this, jetScale, false, particleBatch, environment));
			components.add(ljet = new Jet(this, jetScale, true, particleBatch, environment));
		}
		else rjet = ljet = null;
		int batteryCount = def.batteryCount.get();
		float batteryScale = def.getBatteryScale();
		for (int i = 0; i < batteryCount; i++) {
			batteries.add(new Battery(this, batteryScale, i));
		}
		components.addAll(batteries);
		int wheelCount = def.wheelCount.get();
		float axisHz = def.wheelAxisFrequency.get();
		float axisWidth = def.wheelAxisWidth.get();
		float axisHeight = def.wheelAxisHeight.get();
		for (int i = 0; i < wheelCount; i++) {
			wheels.add(new Wheel(this, wheelScale, i, wheelCount, axisHz, axisWidth, axisHeight));
		}
		components.addAll(wheels);
		if (jetEnabled) {
			landed = false;
			landing = new Tween();
			landing.duration = def.landingDuration.get();
			landing.callback = new TweenCallback() {

				@Override
				public void end(Tween tween) {
					land();

				}
			};
			landing.start();
		}
		else {
			landed = true;
			landing = null;
			for (Component c : components) {
				c.setGravityScale(1);
			}
		}
		physics.addBodies(this);
	}

	public final void addBatteryPower(float amount) {
		Battery.addPower(batteries, amount);
	}

	private void destroy(Component component) {
		if (component != null) component.dispose();
	}

	public final void dispose() {
		components.removeValue(base, true);
		for (Component c : components) {
			c.dispose();
		}
		destroy(base);
		components.clear();
		batteries.clear();
		wheels.clear();
	}

	public final void enablePhysics() {
		for (Component c : components) {
			c.enableBody();
		}
	}

	@Override
	public void fixedUpdate() {
		for (Component c : components) {
			c.fixedUpdate();
		}
	}

	public final float getHeight() {
		return height;
	}

	public final float getInputActivity() {
		return input.getActivity();
	}

	public final InputProcessor getInputProcessor() {
		return input;
	}

	public final float getLastTurboPower() {
		return turbo == null ? 0 : turbo.lastPower;
	}

	public final Vector3 getPosition() {
		return base.getPosition();
	}

	public final float getRotation() {
		return base.getRotation();
	}

	public final Vector2 getVelocity() {
		return base.getVelocity();
	}

	public final float getX() {
		return base.getPosition().x;
	}

	public final float getY() {
		return base.getPosition().y;
	}

	@Override
	public void interpolateTransform() {
		for (Component c : components) {
			c.interpolateTransform();
		}
	}

	public final void land() {
		if (landed) return;
		landing.kill();
		landed = true;
		for (Component c : components) {
			c.setGravityScale(1);
		}
	}

	public final void removeBattery(Battery battery) {
		batteries.removeValue(battery, true);
	}

	public final void removeWheel(Wheel wheel) {
		wheels.removeValue(wheel, true);
	}

	public final void render(SolidShader g) {
		for (Component c : components) {
			g.setColor(color);
			c.render(g);
		}
	}

	public final void renderLight(float deltaTime) {
		if (headlight != null) headlight.renderLight(deltaTime);
	}

	public float requireBatteryPower(float amount) {
		return Battery.requirePower(batteries, amount);
	}

	public final void resetInput() {
		input.reset();
	}

	public final void setInputHandler(CarInputHandler inputHandler) {
		input.setHandler(inputHandler);
		input.reset();
	}

	public final void setLeftjetBoost(float amount) {
		if (ljet != null) ljet.setBoost(amount);
	}

	public final void setMotorStatus(Motor.Status status) {
		motor.setStatus(status);
	}

	public final void setRightjetBoost(float amount) {
		if (rjet != null) rjet.setBoost(amount);
	}

	public final void setTurbo(boolean enabled) {
		if (turbo != null) turbo.setEnabled(enabled);
	}

	public final void setWheelSpeed(float value) {
		Wheel.setSpeed(wheels, value);
	}

	public final void setWheelTorque(float value) {
		Wheel.setTorque(wheels, value);
	}

	public final void update(float deltaTime) {
		input.update(deltaTime);
		if (!landed) {
			landing.update(deltaTime);
			float progress = landing.getLinearAlpha();
			for (Component c : components) {
				c.setGravityScale(progress);
			}
			rjet.fakeBoost(1 - progress);
			ljet.fakeBoost(1 - progress);
		}
		for (Component c : components) {
			c.update(deltaTime);
			if (c.isDisposable()) DISPOSABLES.add(c);
		}
		for (Component c : DISPOSABLES) {
			c.dispose();
			components.removeValue(c, true);
		}
		DISPOSABLES.clear();
	}

	@Override
	public void updateTransform() {
		for (Component c : components) {
			c.updateTransform();
		}
	}

}
