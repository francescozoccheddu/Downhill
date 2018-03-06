
package com.francescoz.downhill.components.environment.sky;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.environment.SkyDefinition;
import com.francescoz.downhill.components.wrappers.ColorUtils;
import com.francescoz.downhill.components.wrappers.FloatingRange;

final class SmallStarSet {

	private static final float BRIGHTNESS_DIFFERENCE = 0.25f;
	private static final float SATURATION_DIFFERENCE = -0.25f;
	private static final RandomXS128 RANDOM = new RandomXS128(0);
	private static final FloatingRange ROTATION_SPEED = new FloatingRange(-180, 180, 40);
	private static final FloatingRange SIZE = new FloatingRange(0.001f, 0.01f, 0.004f);
	private final SmallStar[] stars;
	private final Color color;

	public SmallStarSet(int seed, SkyDefinition skyDefinition) {
		RANDOM.setSeed(seed);
		ROTATION_SPEED.setAlpha(RANDOM.nextFloat());
		SIZE.setAlpha(RANDOM.nextFloat());
		int starCount = (int) Math.max(Data.getEnvironmentData().smallStarData.starCount * (1 - skyDefinition.brightnessAlpha), 1);
		stars = new SmallStar[starCount];
		color = new Color();
		ColorUtils.setAsHSB(color, skyDefinition.hue, skyDefinition.saturation + SATURATION_DIFFERENCE,
				skyDefinition.brightness + BRIGHTNESS_DIFFERENCE);
		for (int i = 0; i < starCount; i++) {
			float rotationSpeed = ROTATION_SPEED.choosenRange.get(RANDOM);
			stars[i] = new SmallStar(seed + i, rotationSpeed, SIZE.choosenRange, RANDOM.nextFloat(), RANDOM.nextFloat());
		}
	}

	public void render() {
		Data.getEnvironmentData().smallStarData.render(stars, color);
	}

	public void update(float deltaTime) {
		for (SmallStar s : stars) {
			s.update(deltaTime);
		}
	}
}
