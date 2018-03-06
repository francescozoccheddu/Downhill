
package com.francescoz.downhill.components.car.utils;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.physics.box2d.Body;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.wrappers.Transform;

public final class DrawableBody {

	public final Transform transform;
	private final Mesh mesh;
	public final Body body;

	public DrawableBody(Body body, Mesh mesh, float scaleX, float scaleY) {
		transform = new Transform();
		transform.scale.set(scaleX, scaleY);
		this.mesh = mesh;
		this.body = body;
	}

	public void interpolateTransform() {
		Data.getPhysicsWorld().interpolateTransform(body, transform);
	}

	public void render(SolidShader g) {
		g.setTransform(transform);
		g.render(mesh);
	}

	public void updateTransform() {
		Data.getPhysicsWorld().updateTransform(body, transform);
	}
}
