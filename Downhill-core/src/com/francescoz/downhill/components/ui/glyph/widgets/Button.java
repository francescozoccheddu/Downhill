
package com.francescoz.downhill.components.ui.glyph.widgets;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer;
import com.francescoz.downhill.components.ui.glyph.Glyph;

public abstract class Button extends AnimatedGlyphContainer {

	private static final float PRESS_SPAWN_DELAY = 0.1f;
	private static final float INITIAL_PRESS_SPAWN_DELAY = 0.3f;
	private final float border;
	public final Glyph glyph;
	protected boolean pressed;
	protected boolean enabled;
	private float nextPressSpawn;
	private boolean spawning;

	public Button(Glyph glyph, float border) {
		this.glyph = glyph;
		this.border = border;
		glyph.setSizeListener(this);
	}

	protected abstract void clicked();

	protected abstract void pressSpawn();

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY) {
		float opacityFactor = enabled ? pressed ? 0.5f : 1 : 0.25f;
		parentX += relPosition.x;
		parentY += relPosition.y;
		glyph.render(g, opacity * opacityFactor, parentX, parentY);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (!enabled) {
			pressed = false;
		}
	}

	@Override
	public void sizeChanged() {
		float gw = glyph.getWidth();
		float gh = glyph.getHeight();
		float offset = border / 2;
		setSize(gw + border, gh + border);
		glyph.relPosition.set(offset, offset);
	}

	@Override
	public void touchDown(float x, float y) {
		if (isContained(x, y)) {
			if (!enabled) {
				
				return;
			}
			
			pressed = true;
			spawning = false;
			nextPressSpawn = INITIAL_PRESS_SPAWN_DELAY;
		}
	}

	@Override
	public void touchDragged(float x, float y) {
	}

	@Override
	public void touchUp(float x, float y) {
		if (pressed && isContained(x, y)) {
			if (!spawning) clicked();
		}
		pressed = false;
	}

	@Override
	public void updateAnimation(float deltaTime) {
		super.updateAnimation(deltaTime);
		if (pressed) {
			nextPressSpawn -= deltaTime;
			if (nextPressSpawn <= 0) {
				spawning = true;
				nextPressSpawn = PRESS_SPAWN_DELAY;
				pressSpawn();
			}
		}
	}

}
