
package com.francescoz.downhill.components.loaders;

import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.Data;

public final class MeshDefinition {

	public final float[] vertices;
	public final short[] indices;
	private float[] transformedVertices;

	MeshDefinition(float[] vertices, short[] indices) {
		this.vertices = vertices;
		this.indices = indices;
	}

	private void allocateTransformVertices() {
		if (transformedVertices == null) transformedVertices = new float[vertices.length];
	}

	private Mesh createMesh(boolean staticVertices) {
		Mesh mesh = Data.getGraphics().solid.newMesh(staticVertices, true, vertices.length / 2, indices.length);
		return mesh;
	}

	public Mesh getMesh(boolean staticVertices) {
		Mesh m = createMesh(staticVertices);
		m.setIndices(indices);
		m.setVertices(vertices);
		return m;
	}

	public Mesh getMesh(boolean staticVertices, float translationX, float translationY, float scaleX, float scaleY) {
		Mesh m = createMesh(staticVertices);
		m.setIndices(indices);
		m.setVertices(transformVertices(translationX, translationY, scaleX, scaleY));
		return m;
	}

	private float[] transformVertices(float translationX, float translationY, float scaleX, float scaleY) {
		allocateTransformVertices();
		for (int i = 0; i < vertices.length; i += 2) {
			transformedVertices[i] = vertices[i] * scaleX + translationX;
			transformedVertices[i + 1] = vertices[i + 1] * scaleY + translationY;
		}
		return transformedVertices;
	}

}