
package com.francescoz.downhill.components.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public final class Camera {

	private static final PerspectiveCamera CAMERA = new PerspectiveCamera();
	private static float invertedAspectRatio;
	private static CameraHandler handler;
	private static final Vector2 TEMP_VECTOR_2 = new Vector2();

	public static void forceSetPosition(float x, float y, float z) {
		CAMERA.position.set(x, y, z);
	}

	public static float getCameraPositionZ(float viewLayerZ, float viewWidth) {
		return viewWidth / 2 + viewLayerZ;
	}

	public static float getLayerWidth(float layerZ) {
		float cameraZ = getZ();
		return (cameraZ - layerZ) / cameraZ;
	}

	public static float getLayerWidth(float layerZ, float cameraZ) {
		return (cameraZ - layerZ) / cameraZ;
	}

	public static float getLeftX(float layerZ) {
		return CAMERA.position.x - (CAMERA.position.z - layerZ);
	}

	public static Matrix4 getProjectionMatrix() {
		return CAMERA.combined;
	}

	public static float getRightX(float layerZ) {
		return CAMERA.position.x + (CAMERA.position.z - layerZ);
	}

	public static float getViewWidth(float distance) {
		return distance * 2;
	}

	private static float getZ() {
		return CAMERA.position.z;
	}

	public static void resize(int width, int height) {
		invertedAspectRatio = (height / (float) width);
		CAMERA.fieldOfView = (float) (2 * Math.atan(invertedAspectRatio)) * MathUtils.radiansToDegrees;
		CAMERA.viewportHeight = height;
		CAMERA.viewportWidth = width;
		CAMERA.near = 1;
		CAMERA.far = 1000000;
		CAMERA.update();
	}

	public static void setHandler(CameraHandler handler) {
		Camera.handler = handler;
		if (handler != null) handler.setted(CAMERA.position);
		CAMERA.update();
	}

	public static Vector2 unproject(float screenX, float screenY) {
		float h = Gdx.graphics.getHeight();
		float w = Gdx.graphics.getWidth();
		screenY = (2 * (h - screenY) / h - 1) * (h / w);
		screenX = 2 * screenX / w - 1;
		Vector3 p = CAMERA.position;
		TEMP_VECTOR_2.set(screenX, screenY).scl(getZ()).add(p.x, p.y);
		return TEMP_VECTOR_2;
	}

	public static void update(float deltaTime) {
		if (handler != null) handler.update(deltaTime, CAMERA.position);
		CAMERA.update();
	}
}
