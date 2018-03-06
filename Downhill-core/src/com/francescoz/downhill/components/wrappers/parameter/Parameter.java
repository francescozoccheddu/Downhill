
package com.francescoz.downhill.components.wrappers.parameter;

import com.badlogic.gdx.Preferences;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.ui.glyph.widgets.parameter.ParameterListener;

public abstract class Parameter<T> {

	private final int localeIndex;
	private ParameterListener listener;
	private final String key;

	Parameter(int localeIndex, String key) {
		this.localeIndex = localeIndex;
		this.key = key;
	}

	public abstract void decrement();

	public abstract T get();

	public abstract String getActualString();

	public final String getOptionString() {
		return Data.getUIData().getLocale().get(localeIndex);
	}

	public abstract void increment();

	public abstract boolean isMax();

	public abstract boolean isMin();

	protected abstract void load(Preferences prefs, String key, T defaultValue);

	public final void load(Preferences prefs, T defaultValue) {
		load(prefs, key, defaultValue);
	}

	public abstract void loopIncrement();

	public abstract void loopMiniIncrement();

	public abstract void miniDecrement();

	public abstract void miniIncrement();

	public final void save(Preferences prefs) {
		save(prefs, key);
	}

	protected abstract void save(Preferences prefs, String key);

	public abstract void set(T value);

	public final void setListener(ParameterListener listener) {
		this.listener = listener;
		updateKnob();
	}

	protected final void updateKnob() {
		if (listener != null) listener.parameterUpdated();
	}

}
