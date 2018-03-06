
package com.francescoz.downhill.components.environment.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.RandomXS128;
import com.francescoz.downhill.components.wrappers.FloatingRange;

public final class TerrainTracer {

	private static final int TRACER_COUNT = 8;
	private static final FloatingRange TRACER_SIZE_X = new FloatingRange(0.0001f, 1f, 0);
	private static final FloatingRange TRACER_SIZE_Y = new FloatingRange(0.01f, 10, 0);
	private static final RandomXS128 RANDOM = new RandomXS128(0);
	static {
		float devianceFactor = 1f / TRACER_COUNT;
		TRACER_SIZE_X.setDevianceFactor(devianceFactor * 2);
		TRACER_SIZE_Y.setDevianceFactor(devianceFactor / 2f);
	}
	private final SimplexTracer[] tracers;
	private final float normalization;
	private final int seed;

	public TerrainTracer(int seed) {
		RANDOM.setSeed(seed);
		this.seed = seed;
		tracers = new SimplexTracer[TRACER_COUNT];
		float normalization = 0;
		float indexFactor = 1f / (TRACER_COUNT - 1);
		for (int i = 0; i < TRACER_COUNT; i++) {
			float alpha = i * indexFactor;
			float sizeX = TRACER_SIZE_X.setAlpha(alpha, Interpolation.exp10In).get(RANDOM, Interpolation.exp5In);
			float sizeY = TRACER_SIZE_Y.setAlpha(1 - TRACER_SIZE_X.fullRange.getAlpha(sizeX), Interpolation.exp10In).get(RANDOM);
			normalization += sizeY;
			tracers[i] = new SimplexTracer(sizeX, sizeY);
		}
		this.normalization = normalization;
	}

	float get(float x, int layer) {
		float height = 0;
		for (SimplexTracer t : tracers) {
			height += t.get(seed + layer, x);
		}
		return height / normalization;
	}

}
