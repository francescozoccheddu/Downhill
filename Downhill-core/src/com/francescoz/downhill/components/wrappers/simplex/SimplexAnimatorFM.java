
package com.francescoz.downhill.components.wrappers.simplex;

import com.francescoz.downhill.components.wrappers.Range;

public final class SimplexAnimatorFM {

	private final SimplexAnimator frequencyAnimator;
	private final Range frequency;
	private float time;
	private final int seed;

	public SimplexAnimatorFM(int seed, Range frequency, float modulatorFrequency) {
		this.seed = seed;
		this.frequency = frequency;
		frequencyAnimator = new SimplexAnimator(seed + 1, modulatorFrequency);
	}

	public float get() {
		return (float) (SimplexNoise.noise(time, seed) + 1) / 2;
	}

	public float get(Range range) {
		return range.get(get());
	}

	public void restart() {
		time = 0;
		frequencyAnimator.restart();
	}

	public void update(float deltaTime) {
		frequencyAnimator.update(time);
		time += deltaTime * frequencyAnimator.get(frequency);
	}

}
