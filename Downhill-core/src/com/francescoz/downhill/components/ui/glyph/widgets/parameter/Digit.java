
package com.francescoz.downhill.components.ui.glyph.widgets.parameter;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.layouts.Column;
import com.francescoz.downhill.components.ui.glyph.widgets.parameter.Tweak.Direction;
import com.francescoz.downhill.components.wrappers.parameter.IntegerParameter;

public final class Digit extends Column implements ParameterListener {

	private static final float SPACING = 0.025f;
	private final IntegerParameter parameter;
	private boolean enabled;
	private final Display display;
	private final Tweak sub;
	private final Tweak add;
	private ParameterListener listener;

	public Digit(int value) {
		super(SPACING, Align.MIDDLE);
		parameter = new IntegerParameter(0, "", 0, 9);
		parameter.set(value);
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
		sub = new Tweak(Direction.DOWN) {

			@Override
			protected void clicked() {
				parameter.decrement();
				
			}

			@Override
			protected void pressSpawn() {
				parameter.miniDecrement();
				
			}
		};
		add = new Tweak(Direction.UP) {

			@Override
			protected void clicked() {
				parameter.increment();
				
			}

			@Override
			protected void pressSpawn() {
				parameter.miniIncrement();
				
			}
		};
		parameter.setListener(this);
		add(sub, display, add);
	}

	public int get() {
		return parameter.get();
	}

	@Override
	public void parameterUpdated() {
		display.set(parameter.getActualString());
		updateTweaks();
		if (listener != null) listener.parameterUpdated();
	}

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY) {
		super.renderPost(g, enabled ? opacity : opacity / 2f, parentX, parentY);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		updateTweaks();
	}

	public void setListener(ParameterListener listener) {
		this.listener = listener;
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
