
package com.francescoz.downhill.components.ui.glyph.layouts;

import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer;
import com.francescoz.downhill.components.ui.glyph.Glyph;

public final class Box extends AnimatedGlyphContainer {

	private final float alignFactorX, alignFactorY;
	public final Glyph glyph;

	public Box(Glyph glyph, Align horizontalAlign, float width, Align verticalAlign, float height) {
		this.glyph = glyph;
		alignFactorX = horizontalAlign.factor;
		alignFactorY = verticalAlign.factor;
		setSize(width, height);
		glyph.setSizeListener(this);
	}

	@Override
	public void sizeChanged() {
		float offsetX = getWidth() - glyph.getWidth();
		float offsetY = getHeight() - glyph.getHeight();
		glyph.relPosition.set(offsetX * alignFactorX, offsetY * alignFactorY);
	}

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY) {
		glyph.render(g, opacity, parentX + relPosition.x, parentY + relPosition.y);
	}

	@Override
	public void touchDown(float x, float y) {
		glyph.touchDown(x - relPosition.x, y - relPosition.y);
	}

	@Override
	public void touchDragged(float x, float y) {
		glyph.touchDragged(x - relPosition.x, y - relPosition.y);
	}

	@Override
	public void touchUp(float x, float y) {
		glyph.touchUp(x - relPosition.x, y - relPosition.y);
	}

}
