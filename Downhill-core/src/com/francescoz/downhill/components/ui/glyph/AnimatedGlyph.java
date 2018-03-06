
package com.francescoz.downhill.components.ui.glyph;

import com.badlogic.gdx.math.Interpolation;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;

abstract class AnimatedGlyph extends Glyph {

	private static final float ANIMATION_DURATION = 0.25f;
	private static final Interpolation ANIMATION_EASING = Interpolation.pow2Out;
	private float time;

	public AnimatedGlyph() {
		time = 1;
	}

	public final void animate() {
		time = 0;
	}

	@Override
	public final void render(SolidShader g, float opacity, float parentX, float parentY) {
		renderPost(g, opacity * (time == 1 ? 1 : ANIMATION_EASING.apply(time)), parentX, parentY);
	}

	protected abstract void renderPost(SolidShader g, float opacity, float parentX, float parentY);

	public void updateAnimation(float deltaTime) {
		if (time < 1) {
			time = Math.min(1, time + deltaTime / ANIMATION_DURATION);
		}
	}

}
