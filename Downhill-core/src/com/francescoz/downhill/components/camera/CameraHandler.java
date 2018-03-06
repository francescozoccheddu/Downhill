
package com.francescoz.downhill.components.camera;

import com.badlogic.gdx.math.Vector3;

public interface CameraHandler {

	public void setted(Vector3 position);

	public void update(float deltaTime, Vector3 position);
}
