
package com.francescoz.downhill.components.wrappers.simplex;

import com.francescoz.downhill.components.wrappers.Range;

public final class SimplexAnimator {

	public float frequency;
	private float time;
	private final int seed;

	public SimplexAnimator(int seed) {
		this.seed = seed;
		frequency = 1;
	}

	public SimplexAnimator(int seed, float frequency) {
		this.seed = seed;
		this.frequency = frequency;
	}

	public float get() {
		return (float) (SimplexNoise.noise(time, seed) + 1) / 2;
	}

	public float get(Range range) {
		return range.get(get());
	}

	void restart() {
		time = 0;
	}

	public void update(float deltaTime) {
		time += deltaTime * frequency;
	}

}
