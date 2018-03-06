
package com.francescoz.downhill.components.ui.glyph.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.Glyph;

public abstract class SolidButton extends Button {

	private final Mesh mesh;

	public SolidButton(Glyph glyph, float border) {
		super(glyph, border);
		mesh = Data.getUIData().getIcons().getMesh("Quad");
	}

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY) {
		float opacityFactor = enabled ? pressed ? 0.7f : 1 : 0.5f;
		g.setColor(Color.WHITE, opacityFactor * opacity);
		parentX += relPosition.x;
		parentY += relPosition.y;
		g.setPreTransform(parentX, parentY, 0, getWidth(), getHeight());
		g.render(mesh);
		g.setColor(Color.CLEAR);
		glyph.render(g, 0, parentX, parentY);
	}

}
