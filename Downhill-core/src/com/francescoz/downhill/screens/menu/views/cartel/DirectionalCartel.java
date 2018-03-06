
package com.francescoz.downhill.screens.menu.views.cartel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.Glyph;

public final class DirectionalCartel extends Cartel {

	private static final float STICK_HEIGHT = 0.75f;
	public static final float STICK_WIDTH = 0.15f;
	private static final float BORDER = 0.2f;
	private static final float ARROW_WIDTH = 0.5f;

	private final Glyph glyph;
	private final Mesh triangle;
	private final boolean right;

	public DirectionalCartel(Glyph glyph, boolean right) {
		this.glyph = glyph;
		triangle = Data.getUIData().getIcons().getMesh(right ? "TriangleR" : "TriangleL");
		this.right = right;
	}

	@Override
	protected void render(SolidShader g) {
		g.setPreTransformEnabled(true);
		g.setTransform(transform);
		float height = glyph.getHeight();
		float width = glyph.getWidth();
		float headHeight = height + BORDER;
		float headWidth = width + BORDER;
		float halfHeadWidth = headWidth / 2;
		g.setPreTransform(STICK_WIDTH / -2, -STICK_WIDTH, 0, STICK_WIDTH, STICK_HEIGHT);
		g.render(quad);
		float y = STICK_HEIGHT + headHeight / -2 - STICK_WIDTH;
		g.setPreTransform(-halfHeadWidth, y, 0, headWidth, headHeight);
		g.render(quad);
		float arrowWidth = headHeight * ARROW_WIDTH;
		g.setPreTransform(right ? halfHeadWidth : -halfHeadWidth - arrowWidth, y, 0, arrowWidth, headHeight);
		g.render(triangle);
		g.setColor(Color.CLEAR);
		glyph.render(g, 0, -width / 2, y + BORDER / 2);
		g.setPreTransformEnabled(false);
	}

}
