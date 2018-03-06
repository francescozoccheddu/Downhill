
package com.francescoz.downhill.components.ui.glyph;

import com.francescoz.downhill.components.graphics.shaders.SolidShader;

public abstract class ScaledAnimatedGlyph extends AnimatedGlyph {

	private final float scale;

	public ScaledAnimatedGlyph(float scale) {
		this.scale = scale;
	}

	@Override
	protected final void renderPost(SolidShader g, float opacity, float parentX, float parentY) {
		renderPost(g, opacity, parentX, parentY, scale);
	}

	protected abstract void renderPost(SolidShader g, float opacity, float parentX, float parentY, float scale);

	@Override
	protected void setHeight(float height) {
		super.setHeight(height * scale);
	}

	@Override
	protected void setSize(float width, float height) {
		super.setSize(width * scale, height * scale);
	}

	@Override
	protected void setWidth(float width) {
		super.setWidth(width * scale);
	}

}
