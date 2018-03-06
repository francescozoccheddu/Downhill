
package com.francescoz.downhill.components.ui.glyph.fonts;

import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.loaders.MeshDefinition;

final class Char {

	public final float[] vertices;
	public final short[] indices;
	private final Mesh mesh;
	public final float width;

	Char(char key, MeshDefinition meshDefinition) {
		mesh = meshDefinition.getMesh(true);
		vertices = meshDefinition.vertices;
		indices = meshDefinition.indices;
		float maxX = 0;
		for (int i = 0; i < vertices.length; i += 2) {
			if (vertices[i] > maxX) maxX = vertices[i];
		}
		width = maxX;
	}

	public void dispose() {
		mesh.dispose();
	}

	public int getIndicesCount() {
		return indices.length;
	}

	public int getVerticesCount() {
		return vertices.length / 2;
	}

	public void render(SolidShader g) {
		g.render(mesh);

	}

}
