
package com.francescoz.downhill.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.camera.CameraHandler;
import com.francescoz.downhill.components.car.Car;

final class GameCamera implements CameraHandler {

	private static final Vector3 TRACKING_SPEED = new Vector3(4, 1, 0.5f);
	private static final float MIN_VIEW_WIDTH = 5;
	private static final float MAX_VIEW_WIDTH = 50;
	private static final float MIN_ZOOM = Camera.getCameraPositionZ(0, MIN_VIEW_WIDTH);
	private static final float MAX_ZOOM = Camera.getCameraPositionZ(0, MAX_VIEW_WIDTH);
	private static final float MAX_SPEED = 20;
	private final Car car;

	GameCamera(Car car) {
		this.car = car;
	}

	@Override
	public void setted(Vector3 position) {
	}

	@Override
	public void update(float deltaTime, Vector3 position) {
		Vector3 carPosition = car.getPosition();
		Vector2 carSpeed = car.getVelocity();
		float z = MathUtils.lerp(MIN_ZOOM, MAX_ZOOM, Math.min(carSpeed.len() / MAX_SPEED, 1));
		position.z = MathUtils.lerp(position.z, z, TRACKING_SPEED.z * deltaTime);
		if (Gdx.input.isKeyPressed(Keys.Z)) {
			position.z = 35;
		}
		position.y = MathUtils.lerp(position.y, carPosition.y, TRACKING_SPEED.y * deltaTime);
		float x;
		float halfWidth = Camera.getViewWidth(position.z) / 2;
		if (carSpeed.x > 0) {
			x = carPosition.x + halfWidth * Math.min(carSpeed.x / MAX_SPEED, 1);
		}
		else {
			x = carPosition.x - halfWidth * Math.min(-carSpeed.x / MAX_SPEED, 1);
		}
		position.x = MathUtils.lerp(position.x, x, TRACKING_SPEED.x * deltaTime);
	}

}
