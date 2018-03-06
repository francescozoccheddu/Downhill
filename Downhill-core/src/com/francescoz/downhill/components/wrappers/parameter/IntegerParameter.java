
package com.francescoz.downhill.components.wrappers.parameter;

import com.badlogic.gdx.Preferences;

public final class IntegerParameter extends Parameter<Integer> {

	private final int max, min;
	private int value;

	public IntegerParameter(int localeIndex, String key, int min, int max) {
		super(localeIndex, key);
		this.min = min;
		this.max = max;
		value = min;
	}

	@Override
	public void decrement() {
		value = Math.max(min, value - 1);
		updateKnob();
	}

	@Override
	public Integer get() {
		return value;
	}

	@Override
	public String getActualString() {
		return Integer.toString(value);
	}

	@Override
	public void increment() {
		value = Math.min(max, value + 1);
		updateKnob();
	}

	@Override
	public boolean isMax() {
		return value == max;
	}

	@Override
	public boolean isMin() {
		return value == min;
	}

	@Override
	protected void load(Preferences prefs, String key, Integer defaultValue) {
		value = prefs.getInteger(key, defaultValue);
		validate();
		updateKnob();
	}

	@Override
	public void loopIncrement() {
		value++;
		if (value > max) value = min;
		updateKnob();
	}

	@Override
	public void loopMiniIncrement() {
		loopIncrement();
	}

	@Override
	public void miniDecrement() {
		value = Math.max(min, value - 1);
		updateKnob();
	}

	@Override
	public void miniIncrement() {
		value = Math.min(max, value + 1);
		updateKnob();
	}

	@Override
	protected void save(Preferences prefs, String key) {
		prefs.putInteger(key, value);
	}

	@Override
	public void set(Integer value) {
		this.value = value;
		validate();
		updateKnob();
	}

	private void validate() {
		if (value < min || value > max) throw new RuntimeException("Invalid value");
	}

}
