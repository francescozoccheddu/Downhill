
package com.francescoz.downhill.components.ui.glyph.layouts;

import com.badlogic.gdx.utils.Array;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer;
import com.francescoz.downhill.components.ui.glyph.Glyph;

public abstract class Flow extends AnimatedGlyphContainer {

	protected final Array<Glyph> glyphs;
	public final float spacing;
	protected final float alignFactor;

	public Flow(float spacing, Align align) {
		this.spacing = spacing;
		glyphs = new Array<Glyph>(0);
		alignFactor = align.factor;
	}

	Flow(float spacing, Align align, Glyph... glyphs) {
		this(spacing, align);
		add(glyphs);
	}

	public final void add(Glyph... glyphs) {
		this.glyphs.addAll(glyphs);
		for (Glyph g : glyphs)
			g.setSizeListener(this);
	}

	public final void add(Glyph glyph) {
		glyphs.add(glyph);
		glyph.setSizeListener(this);
	}

	public Glyph[] getGlyphs() {
		return glyphs.toArray();
	}

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY) {
		parentX += relPosition.x;
		parentY += relPosition.y;
		for (Glyph c : glyphs)
			c.render(g, opacity, parentX, parentY);
	}

	@Override
	public void touchDown(float x, float y) {
		if (!isContained(x, y)) return;
		x -= relPosition.x;
		y -= relPosition.y;
		for (Glyph g : glyphs) {
			g.touchDown(x, y);
		}
	}

	@Override
	public void touchDragged(float x, float y) {
		x -= relPosition.x;
		y -= relPosition.y;
		for (Glyph g : glyphs) {
			g.touchDragged(x, y);
		}
	}

	@Override
	public void touchUp(float x, float y) {
		x -= relPosition.x;
		y -= relPosition.y;
		for (Glyph g : glyphs) {
			g.touchUp(x, y);
		}
	}
}
