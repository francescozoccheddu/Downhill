
package com.francescoz.downhill.components.environment.terrain;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.physics.PhysicsWorld;

public final class SolidTerrainData extends TerrainData {

	private final Body body;
	private final FixtureDef fixtureDef;
	private final Fixture fixtures[];

	public SolidTerrainData(int segments) {
		super(segments);
		fixtureDef = Data.getPhysicsWorld().createFixtureDef();
		fixtureDef.friction = 0.6f;
		fixtureDef.restitution = 0.05f;
		PhysicsWorld.Filters.TERRAIN.set(fixtureDef);
		fixtures = new Fixture[segments];
		body = Data.getPhysicsWorld().createBody(BodyType.StaticBody);
		body.setActive(true);
	}

	@Override
	public void dispose() {
		super.dispose();
		Data.getPhysicsWorld().destroy(body);
	}

	public void set(float friction, float restitution) {
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
	}

	@Override
	public void setRange(int startIndex, float[] verts) {
		int count = verts.length / 8;
		EdgeShape shape = Data.getPhysicsWorld().getEdgeShape();
		fixtureDef.shape = shape;
		int firstPassCount = Math.min(segments, startIndex + count) - startIndex;
		int secondPassCount = count - firstPassCount;
		int v = 0;
		if (firstPassCount > 0) {
			mesh.updateVertices(startIndex * 8, verts, 0, firstPassCount * 8);
			for (int i = startIndex; i < startIndex + firstPassCount; i++) {
				shape.set(verts[v], verts[v + 1], verts[v + 4], verts[v + 5]);
				if (fixtures[i] != null) {
					body.destroyFixture(fixtures[i]);
				}
				fixtures[i] = body.createFixture(fixtureDef);
				v += 8;
			}
		}
		if (secondPassCount > 0) {
			mesh.updateVertices(0, verts, firstPassCount * 8, secondPassCount * 8);
			for (int i = 0; i < secondPassCount; i++) {
				shape.set(verts[v], verts[v + 1], verts[v + 4], verts[v + 5]);
				if (fixtures[i] != null) {
					body.destroyFixture(fixtures[i]);
				}
				fixtures[i] = body.createFixture(fixtureDef);
				v += 8;
			}
		}
	}

}
