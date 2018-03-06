
package com.francescoz.downhill.components.ui.glyph.widgets.parameter;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.fonts.CachedText;
import com.francescoz.downhill.components.ui.glyph.layouts.Row;
import com.francescoz.downhill.components.ui.glyph.widgets.parameter.Tweak.Direction;
import com.francescoz.downhill.components.wrappers.parameter.Parameter;
import com.francescoz.downhill.screens.menu.views.MenuViews;

public final class Knob extends Row implements ParameterListener {

	static final float SCALE = MenuViews.BODY_SCALE;
	private static final float SPACING = 0.1f;
	private final Parameter<?> parameter;
	private boolean enabled;
	private final Display display;
	private final Tweak sub;
	private final CachedText title;
	private final Tweak add;

	public Knob(final Parameter<?> parameter) {
		super(SPACING, Align.MIDDLE);
		this.parameter = parameter;
		display = new Display() {

			@Override
			protected void clicked() {
				parameter.loopIncrement();
				
			}

			@Override
			protected void pressSpawn() {
				parameter.loopMiniIncrement();
				
			}
		};
		display.setEnabled(true);
		sub = new Tweak(Direction.LEFT) {

			@Override
			protected void clicked() {
				parameter.decrement();
				
			}

			@Override
			protected void pressSpawn() {
				parameter.miniDecrement();
				
			}
		};
		add = new Tweak(Direction.RIGHT) {

			@Override
			protected void clicked() {
				parameter.increment();
				
			}

			@Override
			protected void pressSpawn() {
				parameter.miniIncrement();
				
			}
		};
		title = new CachedText(Data.getUIData().getNormalFont(), SCALE, parameter.getOptionString());
		parameter.setListener(this);
		add(title, sub, display, add);
	}

	public void dispose() {
		title.dispose();
	}

	@Override
	public void parameterUpdated() {
		display.set(parameter.getActualString());
		updateTweaks();
	}

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY) {
		super.renderPost(g, enabled ? opacity : opacity / 2f, parentX, parentY);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		updateTweaks();
	}

	@Override
	public void touchDown(float x, float y) {
		if (!enabled) return;
		super.touchDown(x, y);
	}

	@Override
	public void updateAnimation(float deltaTime) {
		super.updateAnimation(deltaTime);
		display.updateAnimation(deltaTime);
		add.updateAnimation(deltaTime);
		sub.updateAnimation(deltaTime);
	}

	private void updateTweaks() {
		add.setEnabled(!enabled || !parameter.isMax());
		sub.setEnabled(!enabled || !parameter.isMin());
	}

}
