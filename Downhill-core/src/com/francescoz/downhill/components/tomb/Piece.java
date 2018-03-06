
package com.francescoz.downhill.components.tomb;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.wrappers.Transform;

class Piece {

	private final Mesh mesh;
	protected final Transform transform;

	Piece(Mesh mesh, Vector2 position) {
		this.mesh = mesh;
		transform = new Transform();
		transform.position.set(position, 0);
	}

	final void render(SolidShader graphics) {
		graphics.setTransform(transform);
		graphics.render(mesh);
	}

}
