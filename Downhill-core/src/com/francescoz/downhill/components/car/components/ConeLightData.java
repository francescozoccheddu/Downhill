
package com.francescoz.downhill.components.car.components;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.MathUtils;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.LightShader;
import com.francescoz.downhill.components.wrappers.Transform;

public final class ConeLightData {

	private static final float ANGLE = 90;
	static final float DISTANCE = 50;
	private final Mesh mesh;
	final float[] angles;
	final int rayCount;

	public ConeLightData(int rayCount) {
		this.rayCount = rayCount;
		mesh = Data.getGraphics().light.newMesh(true, true, rayCount + 1, 0);
		float maxIndex = rayCount - 1f;
		angles = new float[rayCount];
		for (int i = 0; i < rayCount; i++) {
			angles[i] = (i / maxIndex - 0.5f) * ANGLE;
		}
		float[] verts = new float[9];
		verts[0] = verts[1] = 0;
		verts[2] = 1;
		verts[5] = verts[8] = 0;
		verts[3] = verts[6] = DISTANCE;
		verts[4] = (float) Math.tan(ANGLE / 2f * MathUtils.degreesToRadians) * DISTANCE;
		verts[7] = -verts[4];
		mesh.setVertices(verts);
	}

	public void dispose() {
		mesh.dispose();
	}

	void render(Transform transform, float lenght, float opacity) {
		LightShader g = Data.getGraphics().light;
		g.begin();
		g.setProjectionByMainCamera();
		g.setTransform(transform);
		g.setLength(lenght);
		g.setAlpha(opacity);
		g.render(mesh);
		g.end();
	}
}
