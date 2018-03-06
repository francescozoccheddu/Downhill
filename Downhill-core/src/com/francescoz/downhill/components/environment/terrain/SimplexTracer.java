
package com.francescoz.downhill.components.environment.terrain;

import com.francescoz.downhill.components.wrappers.simplex.SimplexNoise;

final class SimplexTracer {

	private final float sizeX, sizeY;

	SimplexTracer(float sizeX, float sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public float get(int layer, float x) {
		return (float) (SimplexNoise.noise(x * sizeX, layer) * sizeY);
	}

}
