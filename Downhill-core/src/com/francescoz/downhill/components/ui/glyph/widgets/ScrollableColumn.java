
package com.francescoz.downhill.components.ui.glyph.widgets;

import com.badlogic.gdx.math.Vector2;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.Glyph;
import com.francescoz.downhill.components.ui.glyph.layouts.Flow;
import com.francescoz.downhill.components.wrappers.Range;
import com.francescoz.downhill.components.wrappers.SmoothValue;

class ScrollableColumn extends Flow {

	private static final float FADE_ZONE_FACTOR = 0.15f;
	private static final float MIN_DELTA_Y_FACTOR = 0.1f;
	private final float fixedHeight;
	private final float fadeHeight;
	private final float minScrollHeight;
	private float glyphHeight;
	private boolean fade;
	protected final SmoothValue scrollY;
	private float clampedScrollY;
	private final Vector2 touch;
	private boolean dragging;
	protected boolean pressed;

	private ScrollableColumn(float height, float spacing, Align horizontalAlign) {
		super(spacing, horizontalAlign);
		fixedHeight = height;
		fadeHeight = height * FADE_ZONE_FACTOR;
		minScrollHeight = height * MIN_DELTA_Y_FACTOR;
		scrollY = new SmoothValue(0.1f);
		touch = new Vector2();
	}

	ScrollableColumn(float height, float spacing, Align horizontalAlign, Glyph... glyphs) {
		this(height, spacing, horizontalAlign);
		add(glyphs);
	}

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY) {
		if (!fade) super.renderPost(g, opacity, parentX, parentY);
		else {
			parentX += relPosition.x;
			parentY += relPosition.y + clampedScrollY;
			float scroll = clampedScrollY;
			for (Glyph c : glyphs) {
				float minY = c.relPosition.y + scroll;
				float maxY = minY + c.getHeight() - fixedHeight;
				float a = 1;
				if (minY < fadeHeight) {
					a = minY / fadeHeight;
					if (a <= 0) continue;
				}
				else if (maxY > -fadeHeight) {
					a = maxY / -fadeHeight;
					if (a <= 0) return;
				}
				c.render(g, opacity * a, parentX, parentY);
			}
			scroll += glyphHeight;
			parentY += glyphHeight;
			for (Glyph c : glyphs) {
				float minY = c.relPosition.y + scroll;
				float maxY = minY + c.getHeight() - fixedHeight;
				float a = 1;
				if (minY < fadeHeight) {
					a = minY / fadeHeight;
					if (a <= 0) continue;
				}
				else if (maxY > -fadeHeight) {
					a = maxY / -fadeHeight;
					if (a <= 0) return;
				}
				c.render(g, opacity * a, parentX, parentY);
			}
		}
	}

	@Override
	public void sizeChanged() {
		float y = 0;
		float width = 0;
		glyphHeight = 0;
		for (Glyph g : glyphs) {
			g.relPosition.y = y;
			float w = g.getWidth();
			float h = g.getHeight();
			glyphHeight += h;
			y += h + spacing;
			if (w > width) width = w;
		}
		int spaceCount = glyphs.size - 1;
		if (spaceCount > 0) {
			glyphHeight += spacing * spaceCount;
		}
		fade = glyphHeight > fixedHeight;
		// scrollY.setBoth(clampedScrollY = fixedHeight - glyphHeight);
		setSize(width, Math.min(glyphHeight, fixedHeight));
		for (Glyph g : glyphs) {
			float x = (width - g.getWidth()) * alignFactor;
			g.relPosition.x = x;
		}
	}

	@Override
	public void touchDown(float x, float y) {
		touch.set(x, y);
		if (!isContained(x, y)) return;
		pressed = true;
		if (!fade) {
			float nx = x - relPosition.x;
			float ny = y - relPosition.y;
			for (Glyph g : glyphs) {
				g.touchDown(nx, ny);
			}
		}
		else {
			float nx = x - relPosition.x;
			float ny = y - relPosition.y - clampedScrollY;
			float scroll = clampedScrollY;
			for (Glyph g : glyphs) {
				float minY = g.relPosition.y + scroll;
				float maxY = minY + g.getHeight() / 2 - fixedHeight;
				if (minY < fadeHeight / 2 || maxY > -fadeHeight) continue;
				g.touchDown(nx, ny);
			}
			ny -= glyphHeight;
			scroll += glyphHeight;
			for (Glyph g : glyphs) {
				float minY = g.relPosition.y + scroll;
				float maxY = minY + g.getHeight() / 2 - fixedHeight;
				if (minY < fadeHeight / 2 || maxY > -fadeHeight) continue;
				g.touchDown(nx, ny);
			}
		}
	}

	@Override
	public void touchDragged(float x, float y) {
		if (!pressed) return;
		float dy = touch.y - y;
		if (dragging) {
			touch.set(x, y);
			scrollY.addNext(-dy * 2);
		}
		else {
			if (Math.abs(dy) > minScrollHeight) {
				touch.set(x, y);
				for (Glyph g : glyphs) {
					g.touchUp(-1, 0);
				}
				dragging = true;
			}
		}
	}

	@Override
	public void touchUp(float x, float y) {
		dragging = false;
		if (!pressed) return;
		pressed = false;
		if (!fade) {
			float nx = x - relPosition.x;
			float ny = y - relPosition.y;
			for (Glyph g : glyphs) {
				g.touchUp(nx, ny);
			}
		}
		else {
			float nx = x - relPosition.x;
			float ny = y - relPosition.y - clampedScrollY;
			float scroll = clampedScrollY;
			for (Glyph g : glyphs) {
				float minY = g.relPosition.y + scroll;
				float maxY = minY + g.getHeight() / 2 - fixedHeight;
				if (minY < fadeHeight / 2 || maxY > -fadeHeight) continue;
				g.touchUp(nx, ny);
			}
			ny -= glyphHeight;
			scroll += glyphHeight;
			for (Glyph g : glyphs) {
				float minY = g.relPosition.y + scroll;
				float maxY = minY + g.getHeight() / 2 - fixedHeight;
				if (minY < fadeHeight / 2 || maxY > -fadeHeight) continue;
				g.touchUp(nx, ny);
			}
		}
	}

	@Override
	public void updateAnimation(float deltaTime) {
		super.updateAnimation(deltaTime);
		if (fade) {
			scrollY.update(deltaTime);
			clampedScrollY = Range.loop(scrollY.get(), -glyphHeight, 0);
		}
	}

}
