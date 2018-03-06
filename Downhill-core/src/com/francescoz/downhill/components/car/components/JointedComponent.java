
package com.francescoz.downhill.components.car.components;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.car.utils.DrawableBody;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.tween.RangeTween;
import com.francescoz.downhill.components.tween.Tween;
import com.francescoz.downhill.components.tween.TweenCallback;

abstract class JointedComponent extends Component {

	private static final Vector2 TEMP = new Vector2();
	private boolean disposable;
	protected final Base base;
	protected final RangeTween opacityTween;
	protected Joint joint;
	protected final DrawableBody drawableBody;
	protected final Vector2 baseLocalAnchor;
	protected final float scale;

	public JointedComponent(Car car, float scale, Mesh mesh, float localX, float localY) {
		super(car);
		this.scale = scale;
		base = car.base;
		opacityTween = new RangeTween();
		opacityTween.callback = new TweenCallback() {

			@Override
			public void end(Tween tween) {
				disposable = true;
			}
		};
		opacityTween.duration = 3;
		opacityTween.from = 1;
		opacityTween.to = 0;
		Body body = Data.getPhysicsWorld().createBody(BodyType.DynamicBody);
		body.setUserData(true);
		body.setGravityScale(0);
		body.setTransform(car.initialPosition.x + localX, car.initialPosition.y + localY, 0);
		drawableBody = new DrawableBody(body, mesh, scale, scale);
		baseLocalAnchor = new Vector2(localX, localY);
	}

	public JointedComponent(Car car, float scale, Mesh mesh, float localX, float localY, String bodyName, FixtureDef fixtureDef) {
		super(car);
		this.scale = scale;
		base = car.base;
		opacityTween = new RangeTween();
		opacityTween.callback = new TweenCallback() {

			@Override
			public void end(Tween tween) {
				disposable = true;
			}
		};
		opacityTween.duration = 3;
		opacityTween.from = 1;
		opacityTween.to = 0;
		Body body = Data.getPhysicsWorld().createBody(BodyType.DynamicBody);
		Data.getCarData().applyFixtures(bodyName, body, fixtureDef, 0, 0, scale, scale);
		body.setUserData(true);
		body.setGravityScale(0);
		body.setTransform(car.initialPosition.x + localX, car.initialPosition.y + localY, 0);
		drawableBody = new DrawableBody(body, mesh, scale, scale);
		baseLocalAnchor = new Vector2(localX, localY);
	}

	protected final boolean checkJointAngle(float maxAngle, float relaxFactor) {
		RevoluteJoint j = (RevoluteJoint) joint;
		float angle = j.getJointAngle();
		float relaxedAngle = angle * relaxFactor;
		if (relaxedAngle > j.getUpperLimit()) {
			j.setLimits(-relaxedAngle, relaxedAngle);
		}
		return Math.abs(angle) > maxAngle;
	}

	protected final boolean checkJointDistance(float maxDistance) {
		TEMP.set(drawableBody.body.getPosition());
		return TEMP.sub(base.body.getWorldPoint(baseLocalAnchor)).len() > maxDistance;
	}

	protected final void createJoint(boolean bodyA, JointDef definition) {
		if (bodyA) {
			definition.bodyA = drawableBody.body;
			definition.bodyB = base.body;
		}
		else {
			definition.bodyA = base.body;
			definition.bodyB = drawableBody.body;
		}
		joint = Data.getPhysicsWorld().createJoint(definition);
	}

	protected void detach() {
		if (isJointed()) {
			Data.getPhysicsWorld().destroy(joint);
			joint = null;
			opacityTween.start();
			drawableBody.body.setUserData(false);
		}
	}

	@Override
	public void dispose() {
		if (isJointed()) {
			Data.getPhysicsWorld().destroy(joint);
			joint = null;
		}
		opacityTween.kill();
		Data.getPhysicsWorld().destroy(drawableBody.body);
	}

	@Override
	public void enableBody() {
		drawableBody.body.setActive(true);
	}

	@Override
	public final void interpolateTransform() {
		drawableBody.interpolateTransform();
	}

	@Override
	public final boolean isDisposable() {
		return disposable;
	}

	protected final boolean isJointed() {
		return joint != null;
	}

	@Override
	public void render(SolidShader g) {
		g.setColorAlpha(opacityTween.get());
		drawableBody.render(g);
	}

	@Override
	public final void setGravityScale(float value) {
		drawableBody.body.setGravityScale(value);
	}

	@Override
	public void update(float deltaTime) {
		opacityTween.update(deltaTime);
	}

	@Override
	public final void updateTransform() {
		drawableBody.updateTransform();
	}

}
