
package com.francescoz.downhill.components.ui.glyph.layouts;

import com.badlogic.gdx.math.Interpolation;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.tween.RangeTween;
import com.francescoz.downhill.components.tween.TweenCallback;
import com.francescoz.downhill.components.ui.glyph.Glyph;

public final class TweenColumn extends Column {

	private final RangeTween tween;

	private TweenColumn(float spacing, Align horizontalAlign) {
		super(spacing, horizontalAlign);
		tween = new RangeTween();
	}

	public TweenColumn(float spacing, Align horizontalAlign, Glyph... glyphs) {
		this(spacing, horizontalAlign);
		add(glyphs);
	}

	public void hide(float duration) {
		tween.duration = duration;
		tween.from = 1;
		tween.to = 0;
		tween.start();
	}

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY) {
		if (tween.isEnabled()) {
			parentX += relPosition.x;
			parentY += relPosition.y;
			float alpha = tween.get() * glyphs.size;
			for (int i = 0; i < glyphs.size; i++) {
				int c = glyphs.size - 1 - i;
				glyphs.get(c).render(g, opacity * Math.min(alpha, 1), parentX, parentY);
				alpha--;
				if (alpha < 0) break;
			}
		}
		else super.renderPost(g, opacity, parentX, parentY);
	}

	public void setCallback(TweenCallback callback) {
		tween.callback = callback;
	}

	public void setEasing(Interpolation interpolation) {
		tween.easing = interpolation;
	}

	public void show(float duration) {
		tween.duration = duration;
		tween.from = 0;
		tween.to = 1;
		tween.start();
	}

	@Override
	public void touchDown(float x, float y) {
		x -= relPosition.x;
		y -= relPosition.y;
		float alpha = tween.get() * glyphs.size;
		for (int i = 0; i < glyphs.size; i++) {
			int c = glyphs.size - 1 - i;
			glyphs.get(c).touchDown(x, y);
			alpha--;
			if (alpha < 0) break;
		}
	}

	@Override
	public void touchDragged(float x, float y) {
		x -= relPosition.x;
		y -= relPosition.y;
		float alpha = tween.get() * glyphs.size;
		for (int i = 0; i < glyphs.size; i++) {
			int c = glyphs.size - 1 - i;
			glyphs.get(c).touchDragged(x, y);
			alpha--;
			if (alpha < 0) break;
		}
	}

	@Override
	public void touchUp(float x, float y) {
		x -= relPosition.x;
		y -= relPosition.y;
		float alpha = tween.get() * glyphs.size;
		for (int i = 0; i < glyphs.size; i++) {
			int c = glyphs.size - 1 - i;
			glyphs.get(c).touchUp(x, y);
			alpha--;
			if (alpha < 0) break;
		}
	}

	@Override
	public void updateAnimation(float deltaTime) {
		super.updateAnimation(deltaTime);
		tween.update(deltaTime);
	}

}
