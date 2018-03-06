
package com.francescoz.downhill.components.ui.glyph;

import com.badlogic.gdx.math.Vector2;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;

public abstract class Glyph {

	public final Vector2 relPosition;
	private GlyphSizeListener sizeListener;
	private float width, height;

	public Glyph() {
		relPosition = new Vector2();
	}

	public final float getHeight() {
		return height;
	}

	public final float getWidth() {
		return width;
	}

	public final boolean isContained(float x, float y) {
		x -= relPosition.x;
		y -= relPosition.y;
		return x >= 0 && x <= width && y >= 0 && y <= height;
	}

	public abstract void render(SolidShader g, float opacity, float parentX, float parentY);

	protected void setHeight(float height) {
		this.height = height;
		updateParent();
	}

	protected void setSize(float width, float height) {
		this.width = width;
		this.height = height;
		updateParent();
	}

	public final void setSizeListener(GlyphSizeListener sizeListener) {
		this.sizeListener = sizeListener;
		updateParent();
	}

	protected void setWidth(float width) {
		this.width = width;
		updateParent();
	}

	public abstract void touchDown(float x, float y);

	public abstract void touchDragged(float x, float y);

	public abstract void touchUp(float x, float y);

	private final void updateParent() {
		if (sizeListener != null) sizeListener.sizeChanged();
	}

}
