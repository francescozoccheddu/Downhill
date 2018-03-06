
package com.francescoz.downhill.components.environment.sky;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.environment.SkyDefinition;
import com.francescoz.downhill.components.wrappers.ColorUtils;
import com.francescoz.downhill.components.wrappers.FloatingRange;
import com.francescoz.downhill.components.wrappers.Range;
import com.francescoz.downhill.components.wrappers.Transform;

final class LargeStar extends Star {

	private static final Range DURATION = new Range(2f, 5);
	private static final RandomXS128 RANDOM = new RandomXS128(0);
	private static final Range ROTATION_SPEED = new Range(-45, 45);
	private static final FloatingRange SIZE = new FloatingRange(0.075f, 0.4f, 0.05f);
	private static final Color ADDITIVE_COLOR = new Color(0, 0, 0, 1);
	private static final Range[] COLOR_HUES = new Range[] { new Range(20 / 360f, 60 / 360f), new Range(310 / 360f, 340 / 360f) };
	private static final Range COLOR_SATURATION = new Range(0.8f, 1);
	private final Color color;
	private final Transform transform;
	private final Range scale;
	private final float rotationSpeed;

	LargeStar(int seed, SkyDefinition skyDefinition) {
		super(DURATION, seed);
		RANDOM.setSeed(seed);
		rotationSpeed = ROTATION_SPEED.get(RANDOM);
		scale = new Range(SIZE.setAlpha(RANDOM.nextFloat()));
		color = new Color();
		transform = new Transform();
		transform.position.set(RANDOM.nextFloat(), RANDOM.nextFloat(), 0);
		Range choosen = Range.choose(RANDOM, COLOR_HUES);
		ColorUtils.setAsHSB(color, choosen.get(RANDOM), COLOR_SATURATION.get(RANDOM), 1);
		ADDITIVE_COLOR.set(skyDefinition.skyColor).add(color);
		color.lerp(ADDITIVE_COLOR, 0.5f);
	}

	public void render() {
		Data.getEnvironmentData().largeStarData.render(color, transform);
	}

	@Override
	protected void updateTransform(float animatorAlpha, float angle) {
		float scaleXY = scale.get(animatorAlpha);
		transform.scale.set(scaleXY, scaleXY);
		transform.rotationDegrees = Transform.normalizeAngleDeg(angle * rotationSpeed);
	}

}
