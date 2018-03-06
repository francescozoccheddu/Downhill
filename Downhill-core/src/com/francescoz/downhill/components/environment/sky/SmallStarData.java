
package com.francescoz.downhill.components.environment.sky;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.MathUtils;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;

public final class SmallStarData {

	private static final float[] TRIANGLE_VERTICES;

	static {
		float[] vertices = new float[6];
		float angle = 360 / 6;
		float step = 360 / 3;
		for (int i = 0; i < 6;) {
			vertices[i++] = MathUtils.cosDeg(angle);
			vertices[i++] = MathUtils.sinDeg(angle);
			angle += step;
		}
		TRIANGLE_VERTICES = vertices;
	}

	final int starCount;

	private final float[] vertices;

	private final Mesh mesh;

	public SmallStarData(int count) {
		starCount = count;
		vertices = new float[6 * count];
		mesh = Data.getGraphics().solid.newMesh(false, true, starCount * 3, 0);
	}

	public void dispose() {
		mesh.dispose();
	}

	private void pushData(SmallStar star, int offset) {
		offset *= 6;
		float cos = MathUtils.cosDeg(star.angle);
		float sin = MathUtils.sinDeg(star.angle);
		for (int c = 0; c < 6; c += 2) {
			float bX = TRIANGLE_VERTICES[c];
			float bY = TRIANGLE_VERTICES[c + 1];
			vertices[c + offset] = (bX * cos - bY * sin) * star.scale + star.x;
			vertices[c + offset + 1] = (bX * sin + bY * cos) * star.scale + star.y;
		}
	}

	void render(SmallStar[] stars, Color color) {
		for (int i = 0; i < stars.length; i++) {
			pushData(stars[i], i);
		}
		mesh.setVertices(vertices);
		SolidShader g = Data.getGraphics().solid;
		g.setColor(color);
		g.render(mesh, stars.length * 3);
	}

}
