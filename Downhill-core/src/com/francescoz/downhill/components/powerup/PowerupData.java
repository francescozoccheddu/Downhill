
package com.francescoz.downhill.components.powerup;

import java.util.Map;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.loaders.MeshDefinition;
import com.francescoz.downhill.components.loaders.MeshLoader;
import com.francescoz.downhill.components.loaders.ShapeLoader;
import com.francescoz.downhill.components.physics.PhysicsWorld;
import com.francescoz.downhill.components.wrappers.Transform;

public final class PowerupData {

	static final float SCALE = 0.6f;
	private static final float INSIDE_ANCHOR_X = -0.425f;
	private static final FixtureDef FIXTURE_DEF = Data.getPhysicsWorld().createFixtureDef();
	static {
		FIXTURE_DEF.density = 0;
		FIXTURE_DEF.isSensor = true;
		PhysicsWorld.Filters.POWERUP.set(FIXTURE_DEF);
	}
	private final Mesh outsideMesh;
	private final Mesh insideMesh;
	private final float[] shape;

	public PowerupData() {
		Map<String, MeshDefinition> meshMap = MeshLoader.load("powerup");
		Map<String, float[]> shapeMap = ShapeLoader.load("powerup");
		ShapeLoader.transformShapes(shapeMap, SCALE, SCALE, 0, 0);
		outsideMesh = meshMap.get("Outside").getMesh(true);
		insideMesh = meshMap.get("Inside").getMesh(true);
		shape = shapeMap.get("Hitbox");
	}

	public void dispose() {
		outsideMesh.dispose();
		insideMesh.dispose();
	}

	private void render(SolidShader g, float power) {
		g.setPreTransformEnabled(false);
		g.render(outsideMesh);
		g.setPreTransformEnabled(true);
		g.setPreTransform(INSIDE_ANCHOR_X, 0, 0, power, 1);
		g.render(insideMesh);
	}

	void render(SolidShader g, Iterable<Powerup> powerups, Color color) {
		g.setColor(color);
		float s = SCALE * Camera.getLayerWidth(0);
		float rightX = Camera.getRightX(0) + s;
		float leftX = Camera.getLeftX(0) - s;
		for (Powerup p : powerups) {
			float x = p.transform.position.x;
			if (x < leftX) continue;
			else if (x > rightX) break;
			render(g, p.transform, p.getOpacity(), p.getPower());
		}
		g.setPreTransformEnabled(false);
	}

	private void render(SolidShader g, Transform transform, float opacity, float power) {
		g.setTransform(transform);
		g.setColorAlpha(opacity);
		render(g, power);
	}

	public void setFixture(Body body) {
		PolygonShape polygonShape = Data.getPhysicsWorld().getPolygonShape();
		polygonShape.set(shape);
		FIXTURE_DEF.shape = polygonShape;
		body.createFixture(FIXTURE_DEF);
	}
}
