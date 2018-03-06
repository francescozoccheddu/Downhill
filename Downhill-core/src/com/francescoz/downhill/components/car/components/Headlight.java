
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

public final class Headlight extends JointedComponent {

	private static final float BATTERY_CONSUMPTION = 0.02f;
	private static final int LIGHT_ON = 1;
	private static final int LIGHT_OFF = 0;
	private static final Vector2 ANCHOR = new Vector2(0.425f, 0.025f);
	private static final float JOINT_RELAX_FACTOR = 0.5f;
	private static final RevoluteJointDef JOINT_DEF = Data.getPhysicsWorld().createRevoluteJointDef();
	private static final float MAX_ANGLE = 60 * MathUtils.degreesToRadians;
	private static final float MAX_DISTANCE_FACTOR = 2f;
	private static final FixtureDef FIXTURE_DEF = new FixtureDef();

	static {
		JOINT_DEF.enableLimit = true;
		JOINT_DEF.referenceAngle = 0;
		JOINT_DEF.enableMotor = false;
		FIXTURE_DEF.density = 1;
		FIXTURE_DEF.restitution = 0.05f;
		FIXTURE_DEF.friction = 0.2f;
		Filters.CAR_BODY.set(FIXTURE_DEF);
	}

	float intensity;
	private final Color[] ledColor;

	private final ConeLight coneLight;

	public Headlight(Car car, float scale) {
		super(car, scale, Data.getCarData().headlight, ANCHOR.x, Base.BOTTOM_HEIGHT + car.jetScale + ANCHOR.y, "Headlight",
				FIXTURE_DEF);
		ledColor = new Color[2];
		JOINT_DEF.localAnchorB.set(baseLocalAnchor);
		createJoint(true, JOINT_DEF);
		ledColor[LIGHT_OFF] = new Color(Color.YELLOW).lerp(Color.CHARTREUSE, 0.75f);
		ledColor[LIGHT_OFF].a = 0.5f;
		ledColor[LIGHT_ON] = new Color(Color.YELLOW);
		coneLight = new ConeLight(this);
	}

	@Override
	protected void detach() {
		super.detach();
		intensity = 0;
		coneLight.kill();
	}

	@Override
	public void dispose() {
		super.dispose();
		intensity = 0;
		coneLight.kill();
	}

	@Override
	public void fixedUpdate() {
		if (isJointed()) {
			if (checkJointDistance(MAX_DISTANCE_FACTOR * scale) || checkJointAngle(MAX_ANGLE, JOINT_RELAX_FACTOR)) {
				detach();
			}
		}
	}

	public boolean isLightOn() {
		return intensity > 0;
	}

	@Override
	public void render(SolidShader g) {
		super.render(g);
		if (isJointed()) g.setColor(ledColor[LIGHT_ON]);
		else g.setColor(ledColor[LIGHT_OFF], opacityTween.get());
		g.render(Data.getCarData().headlightLED);
	}

	public void renderLight(float deltaTime) {
		if (isJointed()) {
			coneLight.update(deltaTime);
			coneLight.render();
		}
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (isJointed()) {
			intensity = car.requireBatteryPower(BATTERY_CONSUMPTION * scale * Data.getGraphics().getDeltaTime());
		}
		ledColor[LIGHT_ON].set(Color.YELLOW).lerp(ledColor[LIGHT_OFF], 1 - intensity);
	}
}
