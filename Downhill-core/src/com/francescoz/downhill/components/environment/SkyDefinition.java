
package com.francescoz.downhill.components.environment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.RandomXS128;
import com.francescoz.downhill.components.wrappers.ColorUtils;
import com.francescoz.downhill.components.wrappers.FloatingRange;
import com.francescoz.downhill.components.wrappers.Range;

public final class SkyDefinition {

	private static final FloatingRange SATURATION = new FloatingRange(0.5f, 1f, 0.2f);
	private static final Range BRIGHTNESS = new Range(0.5f, 1f);
	private static final RandomXS128 TEMP_RANDOM = new RandomXS128(0);

	public final Color skyColor;
	public final float hue;
	public final float saturation;
	public final float brightness;
	public final float brightnessAlpha;

	SkyDefinition(int seed) {
		skyColor = new Color();
		TEMP_RANDOM.setSeed(seed);
		hue = TEMP_RANDOM.nextFloat();
		brightnessAlpha = Interpolation.circle.apply(TEMP_RANDOM.nextFloat());
		saturation = SATURATION.setAlpha(brightnessAlpha).get(TEMP_RANDOM);
		brightness = BRIGHTNESS.get(brightnessAlpha);
		ColorUtils.setAsHSB(skyColor, hue, saturation, brightness);
	}

}
