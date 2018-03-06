
package com.francescoz.downhill.components.wrappers.parameter;

import java.math.BigDecimal;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public final class DecimalParameter extends Parameter<Float> {

	public static float round(float value, int decimalCount) {
		return BigDecimal.valueOf(value).setScale(decimalCount, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public final float min, max;

	private int alpha;

	public DecimalParameter(int localeIndex, String key) {
		this(localeIndex, key, 0, 1);
	}

	public DecimalParameter(int localeIndex, String key, float min, float max) {
		super(localeIndex, key);
		this.min = min;
		this.max = max;
	}

	@Override
	public void decrement() {
		alpha -= 10;
		if (alpha < 0) alpha = 0;
		updateKnob();
	}

	@Override
	public Float get() {
		return MathUtils.lerp(min, max, alpha / 100f);
	}

	@Override
	public String getActualString() {
		return alpha + "%";
	}

	public float getAlphaFloat() {
		return alpha / 100f;
	}

	public int getAlphaPercent() {
		return alpha;
	}

	@Override
	public void increment() {
		alpha += 10;
		if (alpha > 100) alpha = 100;
		updateKnob();
	}

	@Override
	public boolean isMax() {
		return alpha == 100;
	}

	@Override
	public boolean isMin() {
		return alpha == 0;
	}

	@Override
	protected void load(Preferences prefs, String key, Float defaultValue) {
		if (prefs.contains(key)) {
			alpha = prefs.getInteger(key);
			validate();
			updateKnob();
		}
		else set(defaultValue);
	}

	@Override
	public void loopIncrement() {
		alpha += 10;
		if (alpha > 100) alpha = 0;
		updateKnob();
	}

	@Override
	public void loopMiniIncrement() {
		alpha++;
		if (alpha > 100) alpha = 0;
		updateKnob();
	}

	@Override
	public void miniDecrement() {
		alpha--;
		if (alpha < 0) alpha = 0;
		updateKnob();
	}

	@Override
	public void miniIncrement() {
		alpha++;
		if (alpha > 100) alpha = 100;
		updateKnob();
	}

	@Override
	protected void save(Preferences prefs, String key) {
		prefs.putInteger(key, alpha);
	}

	@Override
	public void set(Float alpha) {
		this.alpha = (int) (alpha * 100);
		validate();
		updateKnob();
	}

	public void setPercent(int alpha) {
		this.alpha = alpha;
		validate();
		updateKnob();
	}

	private void validate() {
		if (alpha < 0 || alpha > 100) throw new RuntimeException("Invalid alpha");
	}

}
