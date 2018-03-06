
package com.francescoz.downhill.components.car.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.car.CarData;
import com.francescoz.downhill.components.car.utils.DrawableAnchoredBody;
import com.francescoz.downhill.components.car.utils.DrawableBody;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.physics.PhysicsWorld.Filters;

public final class Base extends Component {

	static final float BOTTOM_HEIGHT = 0.02f;
	private static final int LED_ON = 1;
	private static final int LED_OFF = 0;
	private static final float BATTERY_CONSUMPTION = 0.00005f;
	private static final FixtureDef FIXTURE_DEF = Data.getPhysicsWorld().createFixtureDef();

	static {
		FIXTURE_DEF.density = 2;
		FIXTURE_DEF.friction = 0.6f;
		FIXTURE_DEF.restitution = 0.05f;
		Filters.CAR_BODY.set(FIXTURE_DEF);
	}

	final Body body;
	private final Color[] ledColor;
	private float ledCounter;
	boolean ledEnabled;
	final DrawableBody bottom;
	private final DrawableAnchoredBody pad;
	private final DrawableAnchoredBody base;
	private final DrawableAnchoredBody baseLED;

	public Base(Car car, float jetScale, Color color) {
		super(car);
		boolean jetEnabled = jetScale > 0;
		ledColor = new Color[2];
		ledColor[LED_OFF] = new Color(Color.RED).lerp(color, 0.8f);
		ledColor[LED_OFF].a = 0.75f;
		ledColor[LED_ON] = new Color(Color.RED);
		body = Data.getPhysicsWorld().createBody(BodyType.DynamicBody);
		body.setUserData(true);
		ledCounter = 0;
		ledEnabled = false;
		body.setGravityScale(0);
		body.setTransform(car.initialPosition, 0);
		CarData data = Data.getCarData();
		bottom = new DrawableBody(body, data.bottom, 1, 1);
		pad = jetEnabled ? new DrawableAnchoredBody(bottom.transform, data.quad, 1, jetScale, 0, BOTTOM_HEIGHT) : null;
		base = new DrawableAnchoredBody(bottom.transform, data.base, 0, jetScale + BOTTOM_HEIGHT);
		baseLED = new DrawableAnchoredBody(bottom.transform, data.baseLED, 0, jetScale + BOTTOM_HEIGHT);
		data.applyFixtures("Bottom", body, FIXTURE_DEF);
		if (jetEnabled) data.applyFixtures("Quad", body, FIXTURE_DEF, 0, BOTTOM_HEIGHT, 1, jetScale);
		data.applyFixtures("Base", body, FIXTURE_DEF, 0, BOTTOM_HEIGHT + jetScale, 1, 1);
	}

	@Override
	public void dispose() {
		Data.getPhysicsWorld().destroy(body);
	}

	@Override
	public void enableBody() {
		bottom.body.setActive(true);
	}

	@Override
	public void fixedUpdate() {
	}

	public Vector3 getPosition() {
		return bottom.transform.position;
	}

	public float getRotation() {
		return bottom.transform.rotationDegrees;
	}

	public Vector2 getVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public void interpolateTransform() {
		bottom.interpolateTransform();
	}

	@Override
	public void render(SolidShader g) {
		bottom.render(g);
		base.render(g);
		if (pad != null) pad.render(g);
		if (!ledEnabled && ledCounter < 0.5f) {
			g.setColor(ledColor[LED_OFF]);
		}
		else {
			g.setColor(ledColor[LED_ON]);
		}
		baseLED.render(g);
	}

	@Override
	public void setGravityScale(float value) {
		body.setGravityScale(value);
	}

	@Override
	public void update(float deltaTime) {
		float power = car.requireBatteryPower(BATTERY_CONSUMPTION * deltaTime);
		ledCounter = (ledCounter + deltaTime) % 1;
		ledColor[LED_ON].set(Color.RED);
		if (power < 1) {
			ledColor[LED_ON].lerp(ledColor[LED_OFF], 1 - power);
		}
	}

	@Override
	public void updateTransform() {
		bottom.updateTransform();
	}
}
