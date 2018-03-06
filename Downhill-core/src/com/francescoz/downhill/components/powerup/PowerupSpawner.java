
package com.francescoz.downhill.components.powerup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.environment.terrain.Terrain;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.wrappers.Range;

public final class PowerupSpawner {

	private static final float ENABLE_DISTANCE = 3;
	private static final float POWERUP_AMOUNT_FACTOR = 0.005f;
	private static final Array<Integer> REMOVABLES = new Array<Integer>(0);
	private static final Vector2 POSITION = new Vector2();
	private static final float RADIUS = PowerupData.SCALE / 2;
	private static final Range LONG_DISTANCE = new Range(5, 30);
	private static final Range SERIES_COUNT = new Range(3, 15);
	private static final Range SHORT_DISTANCE = new Range(1.5f, 3);
	private final float powerupAmount;
	private final RandomXS128 random;
	private final Car car;
	private final Terrain ground;
	private final Color color;
	private final Array<Powerup> powerups;
	private float animation;
	private final float shortDistance, longDistance;
	private final int seriesCount;
	private int seriesIndex;
	private float nextInstanceX;

	public PowerupSpawner(Car car, Environment environment) {
		this(environment.seed, car, environment.getGround(), environment.getGroundColor());
	}

	private PowerupSpawner(int seed, Car car, Terrain ground, Color color) {
		this.ground = ground;
		this.car = car;
		random = new RandomXS128(seed);
		powerups = new Array<Powerup>(0);
		this.color = color;
		shortDistance = SHORT_DISTANCE.get(random) * PowerupData.SCALE;
		longDistance = LONG_DISTANCE.get(random.nextFloat());
		seriesCount = SERIES_COUNT.getRounded(random.nextFloat());
		nextInstanceX = longDistance;
		float density = seriesCount / (shortDistance * seriesCount + longDistance);
		powerupAmount = POWERUP_AMOUNT_FACTOR / density;
		seriesIndex = 0;
	}

	void collect(float power) {
		car.addBatteryPower(power * powerupAmount);
		Data.getScore().collectBattery();
	}

	public void dispose() {
		for (Powerup p : powerups) {
			p.free();
		}
		powerups.clear();
	}

	private boolean next(float rightX) {
		if (rightX < nextInstanceX) return false;
		seriesIndex++;
		spawn(nextInstanceX);
		if (seriesIndex >= seriesCount) {
			seriesIndex = 0;
			nextInstanceX += longDistance;
		}
		else {
			nextInstanceX += shortDistance;
		}
		return true;
	}

	public void render(SolidShader g) {
		Data.getPowerupData().render(g, powerups, color);
	}

	private void spawn(float x) {
		POSITION.set(x, ground.getMaxHeight(x, RADIUS) + car.getHeight() / 2 + 0.25f);
		powerups.add(Powerup.newPowerup(POSITION, this));
	}

	public void update(float deltaTime) {
		float targetX = car.getX();
		float rightX = targetX + Environment.HALF_WIDTH;
		while (next(rightX)) {
		}
		float enableX = targetX + ENABLE_DISTANCE;
		animation = (animation + deltaTime) % 180;
		for (int i = 0; i < powerups.size; i++) {
			Powerup p = powerups.get(i);
			p.update(deltaTime, animation);
			if (enableX > p.getX()) p.enable();
			if (p.isDisposable()) REMOVABLES.add(i);
		}
		int c = 0;
		for (int r : REMOVABLES) {
			powerups.removeIndex(r - c).free();
			c++;
		}
		REMOVABLES.clear();
	}

}
