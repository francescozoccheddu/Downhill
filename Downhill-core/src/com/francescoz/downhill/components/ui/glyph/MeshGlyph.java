
package com.francescoz.downhill.components.ui.glyph;

import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;

public final class MeshGlyph extends ScaledAnimatedGlyph {

	private Mesh mesh;

	public MeshGlyph(float scale) {
		super(scale);
	}

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY, float scale) {
		g.setColorAlpha(opacity);
		g.setPreTransform(relPosition.x + parentX, relPosition.y + parentY, 0, scale);
		g.render(mesh);
	}

	public void setMesh(Mesh mesh, float meshWidth) {
		this.mesh = mesh;
		setSize(meshWidth, 1);
	}

	@Override
	public void touchDown(float x, float y) {
		isContained(x, y);
	}

	@Override
	public void touchDragged(float x, float y) {
	}

	@Override
	public void touchUp(float x, float y) {
	}

}
