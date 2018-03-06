
package com.francescoz.downhill.components.car.utils;

import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.wrappers.Transform;

public final class DrawableAnchoredBody {

	private final Mesh mesh;
	private final Transform transform;
	private final Transform targetTransform;

	public DrawableAnchoredBody(Transform target, Mesh mesh, float localX, float localY) {
		this(target, mesh, 1, 1, localX, localY);
	}

	public DrawableAnchoredBody(Transform target, Mesh mesh, float scaleX, float scaleY, float localX, float localY) {
		this.mesh = mesh;
		targetTransform = target;
		transform = new Transform();
		transform.scale.set(scaleX, scaleY);
		transform.position.set(localX, localY, 0);
	}

	public void render(SolidShader g) {
		g.setPreTransformEnabled(true);
		g.setPreTransform(transform);
		g.setTransform(targetTransform);
		g.render(mesh);
		g.setPreTransformEnabled(false);
	}

}
