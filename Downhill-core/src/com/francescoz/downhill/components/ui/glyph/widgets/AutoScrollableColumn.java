
package com.francescoz.downhill.components.ui.glyph.widgets;

import com.francescoz.downhill.components.ui.glyph.Glyph;

public final class AutoScrollableColumn extends ScrollableColumn {

	private final float speed;

	public AutoScrollableColumn(float height, float spacing, Align horizontalAlign, float speed, Glyph... glyphs) {
		super(height, spacing, horizontalAlign, glyphs);
		this.speed = speed;
	}

	@Override
	public void updateAnimation(float deltaTime) {
		if (!pressed) scrollY.addNext(deltaTime * speed);
		super.updateAnimation(deltaTime);
	}

}
