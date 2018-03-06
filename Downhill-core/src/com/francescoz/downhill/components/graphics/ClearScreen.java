
package com.francescoz.downhill.components.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;

public class ClearScreen {

	private static final Matrix4 IDENTITY = new Matrix4();
	private final Mesh mesh;
	public float alpha;

	public ClearScreen() {
		mesh = Data.getGraphics().solidScreenMesh;
	}

	public final void render(SolidShader g) {
		render(g, alpha);
	}

	private final void render(SolidShader g, float alpha) {
		if (alpha <= 0) return;
		g.setPreTransformEnabled(false);
		g.setTransform(0, 0, 0);
		g.setProjection(IDENTITY);
		g.setColor(Color.BLACK, alpha);
		g.render(mesh);
	}

}
