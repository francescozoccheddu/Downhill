
package com.francescoz.downhill.components.car.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.physics.PhysicsWorld;
import com.francescoz.downhill.components.tween.RangeTween;
import com.francescoz.downhill.components.tween.Tween;
import com.francescoz.downhill.components.tween.TweenCallback;
import com.francescoz.downhill.components.wrappers.Transform;

final class ConeLight {

	private static final Vector2 TEMP = new Vector2();
	private static final Vector2 ANCHOR = new Vector2(0, 1f);

	private final Vector2 origin = new Vector2();
	private final Vector2 anchor = new Vector2(0, 0.1f);
	private float lastLength;
	private final RangeTween lengthTween;
	private final Headlight headlight;
	private final Transform transform;
	private final RayCastCallback rayCastCallback = new RayCastCallback() {

		private final short terrainBits = PhysicsWorld.Filters.TERRAIN.getCategory();

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			if (fixture.getFilterData().categoryBits == terrainBits) {
				lastLength = origin.dst(point) / headlight.scale;
				return 0;
			}
			return 1;
		}
	};

	ConeLight(Headlight headlight) {
		transform = new Transform();
		this.headlight = headlight;
		anchor.set(ANCHOR).scl(headlight.scale);
		transform.scale.set(headlight.scale, headlight.scale);
		lengthTween = new RangeTween();
		lengthTween.duration = 1 / 10f;
		lengthTween.callback = new TweenCallback() {

			@Override
			public void end(Tween t) {
				updateRay();
			}
		};
		updateRay();
	}

	void kill() {
		if (lengthTween != null) lengthTween.kill();
	}

	void render() {
		Data.getCarData().coneLightData.render(transform, lengthTween.get(), headlight.intensity);
	}

	void update(float deltaTime) {
		lengthTween.update(deltaTime);
		Transform headlightTransf = headlight.drawableBody.transform;
		Vector3 pos = headlightTransf.position;
		float rot = headlightTransf.rotationDegrees;
		origin.set(anchor).rotate(rot);
		origin.add(pos.x, pos.y);
		transform.position.set(origin, 0);
		transform.rotationDegrees = rot;
	}

	private void updateRay() {
		Transform headlightTransf = headlight.drawableBody.transform;
		Vector3 pos = headlightTransf.position;
		float rot = headlightTransf.rotationDegrees;
		origin.set(anchor).rotate(rot);
		origin.add(pos.x, pos.y);
		float absoluteRealLength = 0;
		ConeLightData data = Data.getCarData().coneLightData;
		for (int i = 0; i < data.rayCount; i++) {
			TEMP.set(Vector2.X).rotate(data.angles[i] + rot).scl(ConeLightData.DISTANCE * headlight.scale).add(origin);
			lastLength = ConeLightData.DISTANCE;
			Data.getPhysicsWorld().rayCast(rayCastCallback, origin, TEMP);
			absoluteRealLength += lastLength;
		}
		absoluteRealLength /= data.rayCount;
		kill();
		lengthTween.from = lengthTween.get();
		lengthTween.to = absoluteRealLength;
		lengthTween.start();
	}
}
