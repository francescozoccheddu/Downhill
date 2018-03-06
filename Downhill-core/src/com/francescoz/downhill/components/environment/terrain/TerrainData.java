
package com.francescoz.downhill.components.environment.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;

public class TerrainData {

	protected final Mesh mesh;
	final int segments;

	public TerrainData(int segments) {
		this.segments = segments;
		short[] indices = new short[segments * 6];
		for (short s = 0; s < segments; s++) {
			int c = s * 6;
			int i = s * 4;
			indices[c + 0] = (short) (i + 1);
			indices[c + 1] = (short) (i + 0);
			indices[c + 2] = (short) (i + 2);
			indices[c + 3] = (short) (i + 2);
			indices[c + 4] = (short) (i + 3);
			indices[c + 5] = (short) (i + 1);
		}
		mesh = Data.getGraphics().solid.newMesh(false, true, segments * 4, segments * 6);
		mesh.setIndices(indices);
		mesh.setVertices(new float[segments * 8]);
	}

	public void dispose() {
		mesh.dispose();
	}

	final void render(SolidShader g, Matrix4 transform, Color color) {
		g.setColor(color);
		g.setTransform(transform);
		g.render(mesh);
	}

	public void setRange(int startIndex, float[] verts) {
		int count = verts.length / 8;
		int firstPassCount = Math.min(segments, startIndex + count) - startIndex;
		int secondPassCount = count - firstPassCount;
		if (firstPassCount > 0) mesh.updateVertices(startIndex * 8, verts, 0, firstPassCount * 8);
		if (secondPassCount > 0) mesh.updateVertices(0, verts, firstPassCount * 8, secondPassCount * 8);
	}

}
