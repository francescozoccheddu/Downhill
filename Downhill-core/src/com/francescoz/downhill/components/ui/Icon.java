
package com.francescoz.downhill.components.ui;

import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.components.loaders.MeshDefinition;
import com.francescoz.downhill.components.ui.glyph.MeshGlyph;

public final class Icon {

	private static final String SEPARATOR = "=";
	private final Mesh mesh;
	private final float width;
	final String key;

	Icon(String key, MeshDefinition mesh) {
		this.mesh = mesh.getMesh(true);
		String[] tokens = key.split(SEPARATOR);
		this.key = tokens[0];
		width = Float.parseFloat(tokens[1]);
	}

	void dispose() {
		mesh.dispose();
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMeshGlyph(MeshGlyph target) {
		target.setMesh(mesh, width);
	}

}