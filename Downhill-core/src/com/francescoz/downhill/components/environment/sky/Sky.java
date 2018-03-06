
package com.francescoz.downhill.components.environment.sky;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.environment.SkyDefinition;
import com.francescoz.downhill.components.graphics.Graphics;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;

public final class Sky {

	private static final Matrix4 IDENTITY = new Matrix4();
	private static final Matrix4 PROJECTION = new Matrix4();

	public static void setSize(int width, int height) {
		float r = height / (float) width;
		PROJECTION.setToOrtho2D(0, -r / 2, 1, r);
	}

	private final Color skyColor;
	private final SmallStarSet starSet;
	private final LargeStar star;

	private final boolean hasMainStar;

	public Sky(int seed, SkyDefinition skyDefinition) {
		skyColor = skyDefinition.skyColor;
		starSet = new SmallStarSet(seed, skyDefinition);
		star = (hasMainStar = skyDefinition.brightnessAlpha < 0.5f) ? new LargeStar(seed, skyDefinition) : null;
	}

	public void render(SolidShader g) {
		Graphics gr = Data.getGraphics();
		gr.clear(skyColor);
		g.setProjection(PROJECTION);
		g.setTransform(IDENTITY);
		starSet.render();
		if (hasMainStar) star.render();
	}

	public void update(float deltaTime) {
		starSet.update(deltaTime);
		if (hasMainStar) star.update(deltaTime);
	}
}
