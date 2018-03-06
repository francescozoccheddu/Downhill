
package com.francescoz.downhill.screens.tutorial;

import com.badlogic.gdx.math.Interpolation;
import com.francescoz.downhill.components.car.input.CarInputHandler;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.tween.Tween;
import com.francescoz.downhill.components.tween.TweenCallback;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.layouts.TweenColumn;
import com.francescoz.downhill.components.ui.glyph.widgets.Drawer;

abstract class View {

	protected static final float HEAD_SCALE = 0.25f;
	protected static final float BODY_SCALE = 0.15f;
	private static final float V_SPACING = 0.1f;
	private static final float TWEEN_DURATION = 1.5f;

	private final Drawer drawer;
	protected final TweenColumn column;
	protected final TutorialViews tutorialViews;
	protected boolean done;

	View(TutorialViews tutorialViews) {
		column = new TweenColumn(V_SPACING, Align.MIDDLE);
		column.setEasing(Interpolation.pow2Out);
		drawer = new Drawer(column);
		this.tutorialViews = tutorialViews;
	}

	public void done() {
		if (done) return;
		tutorialViews.screen.car.setInputHandler(CarInputHandler.NONE);
		done = true;
		column.hide(TWEEN_DURATION);
		column.setCallback(new TweenCallback() {

			@Override
			public void end(Tween tween) {
				tutorialViews.next();
			}
		});
	}

	public void render(SolidShader g) {
		drawer.render(g);
	}

	public void restart() {
		tutorialViews.screen.car.setInputHandler(CarInputHandler.NONE);
		if (done) return;
		column.show(TWEEN_DURATION);
	}

	public void update(float deltaTime, float x) {
		drawer.setPosition(x, TutorialViews.Y, 0, Align.MIDDLE, Align.RIGHT_TOP);
		column.updateAnimation(deltaTime);
	}

}
