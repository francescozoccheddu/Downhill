
package com.francescoz.downhill.components.environment.sky;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.wrappers.Transform;

public final class LargeStarData {

	private static final float[] VERTICES;
	private static final short[] INDICES;
	private static final int SEGMENTS = 10;
	static {
		INDICES = new short[SEGMENTS * 3];
		for (int i = 0; i < SEGMENTS; i++) {
			int j = i * 3;
			INDICES[j + 0] = 0;
			INDICES[j + 1] = (short) (i + 1);
			INDICES[j + 2] = (short) (i + 2);
		}
		INDICES[SEGMENTS * 3 - 1] = 1;
		float angle = 0;
		VERTICES = new float[(SEGMENTS + 1) * 2];
		VERTICES[0] = 0;
		VERTICES[1] = 0;
		for (int i = 0; i < SEGMENTS * 2; i += 2) {
			VERTICES[i + 2] = (float) (Math.cos(angle));
			VERTICES[i + 3] = (float) (Math.sin(angle));
			angle += Math.PI * 2f / SEGMENTS;
		}
	}
	private final Mesh mesh;

	public LargeStarData() {
		mesh = Data.getGraphics().solid.newMesh(true, true, SEGMENTS + 1, (SEGMENTS * 3));
		mesh.setIndices(INDICES);
		mesh.setVertices(VERTICES);
	}

	public void dispose() {
		mesh.dispose();
	}

	void render(Color color, Transform transform) {
		SolidShader g = Data.getGraphics().solid;
		g.setColor(color);
		g.setTransform( transform);
		g.render(mesh);
	}
}
