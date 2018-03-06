
package com.francescoz.downhill.components.ui.glyph.layouts;

import com.francescoz.downhill.components.ui.glyph.Glyph;

public class Row extends Flow {

	public Row(float spacing, Align verticalAlign) {
		super(spacing, verticalAlign);
	}

	public Row(float spacing, Align verticalAlign, Glyph... glyphs) {
		super(spacing, verticalAlign, glyphs);
	}

	@Override
	public void sizeChanged() {
		float x = 0;
		float width = 0;
		float height = 0;
		for (Glyph g : glyphs) {
			g.relPosition.x = x;
			float w = g.getWidth();
			float h = g.getHeight();
			width += w;
			x += w + spacing;
			if (h > height) height = h;
		}
		int spaceCount = glyphs.size - 1;
		if (spaceCount > 0) {
			width += spacing * spaceCount;
		}
		setSize(width, height);
		for (Glyph g : glyphs) {
			float y = (height - g.getHeight()) * alignFactor;
			g.relPosition.y = y;
		}
	}

}
