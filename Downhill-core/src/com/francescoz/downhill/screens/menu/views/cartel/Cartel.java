
package com.francescoz.downhill.screens.menu.views.cartel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Interpolation;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.tween.Tween;
import com.francescoz.downhill.components.wrappers.Transform;

public abstract class Cartel {

	static final float STICK_HEIGHT = 0.75f;
	public static final float STICK_WIDTH = 0.15f;
	static final float BORDER = 0.2f;
	private static final float ANIMATION_DURATION = 0.25f;
	private final Tween opacity;
	public final Transform transform;
	protected final Mesh quad;

	public Cartel() {
		opacity = new Tween();
		opacity.duration = ANIMATION_DURATION;
		opacity.easing = Interpolation.pow2Out;
		transform = new Transform();
		quad = Data.getUIData().getIcons().getMesh("Quad");
		opacity.terminate();
	}

	public final void animate() {
		opacity.start();
	}

	protected abstract void render(SolidShader g);

	public final void render(SolidShader g, Color color) {
		g.setColor(color, opacity.getEasedAlpha());
		g.setTransform(transform);
		render(g);
	}

	public final void update(float deltaTime) {
		opacity.update(deltaTime);
	}

}
