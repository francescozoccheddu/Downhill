
package com.francescoz.downhill.components.wrappers.parameter;

import com.badlogic.gdx.Preferences;
import com.francescoz.downhill.Data;

public final class BooleanParameter extends Parameter<Boolean> {

	private static final int FALSE_LOCALE_INDEX = 0;
	private static final int TRUE_LOCALE_INDEX = 1;

	private boolean value;

	public BooleanParameter(int localeIndex, String key) {
		super(localeIndex, key);
	}

	@Override
	public void decrement() {
		value = false;
		updateKnob();
	}

	@Override
	public Boolean get() {
		return value;
	}

	@Override
	public String getActualString() {
		return Data.getUIData().getLocale().get(value ? TRUE_LOCALE_INDEX : FALSE_LOCALE_INDEX);
	}

	@Override
	public void increment() {
		value = true;
		updateKnob();
	}

	@Override
	public boolean isMax() {
		return value;
	}

	@Override
	public boolean isMin() {
		return !value;
	}

	@Override
	protected void load(Preferences prefs, String key, Boolean defaultValue) {
		value = prefs.getBoolean(key, defaultValue);
		updateKnob();
	}

	@Override
	public void loopIncrement() {
		value = !value;
		updateKnob();
	}

	@Override
	public void loopMiniIncrement() {
		loopIncrement();
	}

	@Override
	public void miniDecrement() {
		value = false;
		updateKnob();
	}

	@Override
	public void miniIncrement() {
		value = true;
		updateKnob();
	}

	@Override
	protected void save(Preferences prefs, String key) {
		prefs.putBoolean(key, value);
	}

	@Override
	public void set(Boolean value) {
		this.value = value;
		updateKnob();
	}

}
