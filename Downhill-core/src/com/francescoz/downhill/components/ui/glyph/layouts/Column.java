
package com.francescoz.downhill.components.ui.glyph.layouts;

import com.francescoz.downhill.components.ui.glyph.Glyph;

public class Column extends Flow {

	public Column(float spacing, Align horizontalAlign) {
		super(spacing, horizontalAlign);
	}

	public Column(float spacing, Align horizontalAlign, Glyph... glyphs) {
		super(spacing, horizontalAlign, glyphs);
	}

	@Override
	public void sizeChanged() {
		float y = 0;
		float width = 0;
		float height = 0;
		for (Glyph g : glyphs) {
			g.relPosition.y = y;
			float w = g.getWidth();
			float h = g.getHeight();
			height += h;
			y += h + spacing;
			if (w > width) width = w;
		}
		int spaceCount = glyphs.size - 1;
		if (spaceCount > 0) {
			height += spacing * spaceCount;
		}
		setSize(width, height);
		for (Glyph g : glyphs) {
			float x = (width - g.getWidth()) * alignFactor;
			g.relPosition.x = x;
		}
	}

}
