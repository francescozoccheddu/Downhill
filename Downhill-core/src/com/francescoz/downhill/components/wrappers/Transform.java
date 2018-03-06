
package com.francescoz.downhill.components.wrappers;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public final class Transform {

	private static final double TWO_PI = Math.PI * 2;

	public static float normalizeAngleDeg(float angle) {
		return (float) (angle - 360 * Math.floor((angle + 180) / 360));
	}

	public static float normalizeAngleRad(float angle) {
		return (float) (angle - TWO_PI * Math.floor((angle + Math.PI) / TWO_PI));
	}

	public final Vector3 position;
	public final Vector2 scale;
	public float rotationDegrees;

	public Transform() {
		position = new Vector3();
		scale = new Vector2(1, 1);
		rotationDegrees = 0;
	}

	public void setMatrix(Matrix4 target) {
		target.setToTranslation(position).rotate(Vector3.Z, rotationDegrees).scale(scale.x, scale.y, 1);
	}
}
