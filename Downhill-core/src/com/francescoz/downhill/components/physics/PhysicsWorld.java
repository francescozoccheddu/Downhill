
package com.francescoz.downhill.components.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Array;
import com.francescoz.downhill.components.wrappers.Transform;

public final class PhysicsWorld {

	public static abstract class Filters {

		public static final FilterBits TERRAIN = new FilterBits();
		public static final FilterBits CAR_BODY = new FilterBits();
		public static final FilterBits CAR_WHEEL = new FilterBits();
		public static final FilterBits POWERUP = new FilterBits();
		public static final FilterBits TOMB = new FilterBits();

		static {
			TERRAIN.addCollider(CAR_BODY, CAR_WHEEL, TOMB);
			CAR_BODY.addCollider(TERRAIN, POWERUP);
			CAR_WHEEL.addCollider(TERRAIN, POWERUP);
			POWERUP.addCollider(CAR_BODY, CAR_WHEEL);
			TOMB.addCollider(TERRAIN, TOMB);
		}

		private Filters() {

		}
	}

	private static final float STEPS_PER_SECOND = 60;
	public static final float TIMESTEP = 1 / STEPS_PER_SECOND;
	private static final Vector3 TEMP3 = new Vector3();
	private static final Vector2 TEMP = new Vector2();
	private static final int VELOCITY_ITERATIONS = 3;
	private static final int POSITION_ITERATIONS = 8;
	private static final BodyDef BODY_DEF = new BodyDef();
	static {
		BODY_DEF.active = false;
		BODY_DEF.linearDamping = 0.1f;
		BODY_DEF.angularDamping = 0.1f;
	}
	private final World world;
	private float interpolationAlpha;
	private float accumulator;
	private final EdgeShape edgeShape;
	private final CircleShape circleShape;
	private final PolygonShape polygonShape;

	public PhysicsWorld() {
		world = new World(Vector2.Zero, true);
		world.setAutoClearForces(true);
		circleShape = new CircleShape();
		edgeShape = new EdgeShape();
		polygonShape = new PolygonShape();
	}

	public Body createBody(BodyType bodyType) {
		return createBody(bodyType, Vector2.Zero);
	}

	public Body createBody(BodyType bodyType, Vector2 position) {
		BODY_DEF.type = bodyType;
		BODY_DEF.position.set(position);
		return world.createBody(BODY_DEF);
	}

	public FixtureDef createFixtureDef() {
		return new FixtureDef();
	}

	public Joint createJoint(JointDef jointDef) {
		return world.createJoint(jointDef);
	}

	public RevoluteJointDef createRevoluteJointDef() {
		return new RevoluteJointDef();
	}

	public WheelJointDef createWheelJointDef() {
		return new WheelJointDef();
	}

	public void destroy(Body body) {
		world.destroyBody(body);
	}

	public void destroy(Joint joint) {
		world.destroyJoint(joint);
	}

	public void dispose() {
		world.dispose();
	}

	public CircleShape getCircleShape() {
		return circleShape;
	}

	public EdgeShape getEdgeShape() {
		return edgeShape;
	}

	public PolygonShape getPolygonShape() {
		return polygonShape;
	}

	public void interpolateTransform(Body body, Transform transform) {
		TEMP3.set(body.getPosition(), 0);
		transform.position.lerp(TEMP3, interpolationAlpha);
		transform.rotationDegrees = MathUtils.lerpAngleDeg(transform.rotationDegrees,
				Transform.normalizeAngleDeg(body.getAngle() * MathUtils.radiansToDegrees), interpolationAlpha);
	}

	public void rayCast(RayCastCallback callback, Vector2 point1, Vector2 point2) {
		world.rayCast(callback, point1, point2);
	}

	void resetCounters() {
		accumulator = 0;
		interpolationAlpha = 1;
	}

	void setContactResolver(ContactResolver contactResolver) {
		world.setContactListener(contactResolver);
	}

	void setGravity(float x, float y) {
		TEMP.x = x;
		TEMP.y = y;
		world.setGravity(TEMP);
	}

	void update(float deltaTime, Array<PhysicsBody> bodies) {
		accumulator += deltaTime;
		while (accumulator >= TIMESTEP) {
			accumulator -= TIMESTEP;
			if (accumulator < TIMESTEP) {
				for (PhysicsBody b : bodies) {
					b.updateTransform();
				}
			}
			for (PhysicsBody b : bodies) {
				b.fixedUpdate();
			}
			world.step(TIMESTEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
		interpolationAlpha = accumulator / TIMESTEP;
		for (PhysicsBody b : bodies) {
			b.interpolateTransform();
		}
	}

	public void updateTransform(Body body, Transform transform) {
		transform.position.set(body.getPosition(), 0);
		transform.rotationDegrees = Transform.normalizeAngleDeg(body.getAngle() * MathUtils.radiansToDegrees);
	}

}