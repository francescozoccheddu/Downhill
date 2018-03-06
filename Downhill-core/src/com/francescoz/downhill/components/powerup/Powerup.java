
package com.francescoz.downhill.components.powerup;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.tween.RangeTween;
import com.francescoz.downhill.components.tween.Tween;
import com.francescoz.downhill.components.tween.TweenCallback;
import com.francescoz.downhill.components.wrappers.Transform;

public final class Powerup {

	private static enum Status {
		IDLE, ENABLED, DYING, DEAD
	}

	private static final Array<Powerup> POOL = new Array<Powerup>(0);
	private static final float LIFE = 5;
	private static final float ANIMATION_SPEED = 180;
	private static final float ANIMATION_ANGLE = 10;

	static Powerup newPowerup(Vector2 position, PowerupSpawner spawner) {
		Powerup p = POOL.size > 0 ? POOL.pop() : new Powerup();
		p.set(position, spawner);
		return p;
	}

	private final Body body;
	private final RangeTween scaleTween;
	private final RangeTween powerTween;
	private final RangeTween opacityTween;
	final Transform transform;
	private final TweenCallback destroyCallback;
	private final TweenCallback enableCallback;
	private final TweenCallback deadCallback;
	private Status status;
	private float x;
	private PowerupSpawner spawner;

	private Powerup() {
		destroyCallback = new TweenCallback() {

			@Override
			public void end(Tween tween) {
				disable();
			}
		};
		deadCallback = new TweenCallback() {

			@Override
			public void end(Tween tween) {
				die();
			}
		};
		enableCallback = new TweenCallback() {

			@Override
			public void end(Tween tween) {
				scaleTween.callback = null;
				powerTween.duration = LIFE;
				powerTween.from = 1;
				powerTween.to = 0;
				powerTween.easing = Interpolation.pow2In;
				powerTween.callback = deadCallback;
				powerTween.start();
			}
		};
		transform = new Transform();
		body = Data.getPhysicsWorld().createBody(BodyType.StaticBody);
		Data.getPowerupData().setFixture(body);
		body.setUserData(this);
		scaleTween = new RangeTween();
		opacityTween = new RangeTween();
		powerTween = new RangeTween();
		scaleTween.easing = opacityTween.easing = Interpolation.pow2Out;
		scaleTween.duration = opacityTween.duration = 0.5f;
	}

	public float collect() {
		if (status == Status.ENABLED) {
			float power = powerTween.get();
			spawner.collect(power);
			die();
			opacityTween.from = 0.5f;
			return power;
		}
		return 0;
	}

	private void die() {
		if (status == Status.ENABLED) {
			status = Status.DYING;
			powerTween.duration = 0.25f;
			powerTween.callback = destroyCallback;
			powerTween.startTarget(0);
			powerTween.easing = Interpolation.pow2Out;
			scaleTween.startTarget(0.85f);
			opacityTween.startTarget(0);
		}
	}

	private void disable() {
		status = Status.DEAD;
		body.setActive(false);
		body.setTransform(Vector2.Zero, 0);
		scaleTween.kill();
		powerTween.kill();
		opacityTween.kill();
	}

	void enable() {
		if (status == Status.IDLE) {
			status = Status.ENABLED;
			body.setActive(true);
			scaleTween.callback = enableCallback;
			scaleTween.startTarget(1);
			opacityTween.startTarget(0.75f);
		}
	}

	void free() {
		disable();
		POOL.add(this);
	}

	float getOpacity() {
		return opacityTween.get();
	}

	float getPower() {
		return powerTween.get();
	}

	float getX() {
		return x;
	}

	public boolean isDisposable() {
		return status == Status.DEAD;
	}

	private void set(Vector2 position, PowerupSpawner spawner) {
		this.spawner = spawner;
		status = Status.IDLE;
		transform.position.set(position, 0);
		powerTween.from = powerTween.to = 1;
		scaleTween.from = scaleTween.to = 0.75f;
		opacityTween.from = opacityTween.to = 0.25f;
		body.setTransform(position, 0);
		x = position.x;
	}

	void update(float deltaTime, float animation) {
		opacityTween.update(deltaTime);
		scaleTween.update(deltaTime);
		powerTween.update(deltaTime);
		if (status == Status.DEAD) return;
		float scaleXY = scaleTween.get() * PowerupData.SCALE;
		transform.scale.set(scaleXY, scaleXY);
		transform.rotationDegrees = MathUtils.sinDeg(animation * ANIMATION_SPEED) * ANIMATION_ANGLE;
		Vector3 pos = transform.position;
		body.setTransform(pos.x, pos.y, transform.rotationDegrees);
	}

}
