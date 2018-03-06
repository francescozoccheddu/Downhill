
package com.francescoz.downhill.components.tomb;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.francescoz.downhill.Data;

final class PhysicsPiece extends Piece {

	private final Body body;

	PhysicsPiece(PieceData data, Vector2 position, boolean isStatic) {
		super(data.mesh, position);
		body = Data.getPhysicsWorld().createBody(isStatic ? BodyType.StaticBody : BodyType.DynamicBody, position);
		data.setFixture(body);
	}

	void dispose() {
		Data.getPhysicsWorld().destroy(body);
	}

	void enable() {
		body.setActive(true);
	}

	void interpolateTransform() {
		Data.getPhysicsWorld().interpolateTransform(body, transform);
	}

	void updateTransform() {
		Data.getPhysicsWorld().updateTransform(body, transform);
	}

}
