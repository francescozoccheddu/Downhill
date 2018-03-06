
package com.francescoz.downhill.components.tomb;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.physics.PhysicsWorld;

final class PieceData {

	private static final FixtureDef FIXTURE_DEF = Data.getPhysicsWorld().createFixtureDef();
	static {
		FIXTURE_DEF.density = 2;
		FIXTURE_DEF.friction = 0.8f;
		FIXTURE_DEF.restitution = 0.2f;
		PhysicsWorld.Filters.TOMB.set(FIXTURE_DEF);
	}
	final Mesh mesh;
	private final float[] shape;

	PieceData(Mesh mesh, float[] shape) {
		this.mesh = mesh;
		this.shape = shape;
	}

	public void dispose() {
		mesh.dispose();
	}

	public void setFixture(Body body) {
		PolygonShape polygonShape = Data.getPhysicsWorld().getPolygonShape();
		polygonShape.set(shape);
		FIXTURE_DEF.shape = polygonShape;
		body.createFixture(FIXTURE_DEF);
	}
}
