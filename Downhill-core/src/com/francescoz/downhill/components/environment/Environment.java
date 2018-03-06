
package com.francescoz.downhill.components.environment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.environment.dust.Dust;
import com.francescoz.downhill.components.environment.sky.Sky;
import com.francescoz.downhill.components.environment.terrain.Terrain;
import com.francescoz.downhill.components.environment.terrain.TerrainTracer;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.particle.ParticleBatch;
import com.francescoz.downhill.components.physics.Physics;
import com.francescoz.downhill.components.wrappers.ColorUtils;
import com.francescoz.downhill.components.wrappers.FloatingRange;
import com.francescoz.downhill.components.wrappers.Range;

public final class Environment {

	private static final float WIND_GRAVITY_INFLUENCE = 0.5f;
	private static final Range GRAVITY_Y = new Range(1, 15);
	private static final float GROUND_BRIGHTNESS = 0.1f;
	private static final float GROUND_SATURATION = 0.5f;
	private static final float WIDTH = 70;
	public static final float HALF_WIDTH = WIDTH / 2;
	public static final float CAMERA_MAX_Z = Camera.getCameraPositionZ(0, Environment.WIDTH);
	private static final Range TERRAIN_FRICTION = new Range(0.6f, 1f);
	private static final Range TERRAIN_RESTITUTION = new Range(0.05f, 0.3f);
	private static final float GROUND_GROWTH = 1 / 50f;
	private static final float FLAT_GROUND_HEIGHT = 0.3f;
	private static final float FLAT_GROUND_X_FACTOR = 1f / FLAT_GROUND_HEIGHT;
	private static final Range TERRAIN_SIZE_Y = new Range(2, 10);
	private static final FloatingRange TERRAIN_POSITION_Y = new FloatingRange(-2, 10, 5);
	private static final Range TERRAIN_COUNT = new Range(2, EnvironmentData.MAX_TERRAINS);
	private static final float MIN_DISTANCE = 100;
	private static final float SKY_POSITION_Z = 1000;
	private static final FloatingRange TERRAIN_POSITION_Z = new FloatingRange(MIN_DISTANCE, SKY_POSITION_Z, 0);
	private static final Color TEMP_COLOR = new Color();

	private final Terrain[] terrains;
	private final RandomXS128 random;
	private final Dust dust;
	private final SkyDefinition skyDefinition;
	private final Sky sky;
	private final ParticleBatch particleBatch;
	private final Physics physics;
	public final int seed;

	public Environment(int seed, boolean flatGround) {
		this.seed = seed;
		EnvironmentData data = Data.getEnvironmentData();
		random = new RandomXS128(seed);
		int terrainCount = TERRAIN_COUNT.getRounded(random);
		terrains = new Terrain[terrainCount + 1];
		TerrainTracer tracer = new TerrainTracer(seed);
		float height = TERRAIN_SIZE_Y.get(random);
		Color groundColor = new Color();
		skyDefinition = new SkyDefinition(seed);
		Color skyColor = skyDefinition.skyColor;
		ColorUtils.setAsHSB(groundColor, skyDefinition.hue, skyDefinition.saturation * GROUND_SATURATION,
				skyDefinition.brightness * GROUND_BRIGHTNESS);
		data.groundData.set(TERRAIN_FRICTION.get(random), TERRAIN_RESTITUTION.get(random));
		float wind = random.nextFloat() * 2 - 1;
		if (flatGround) {
			terrains[0] = new Terrain(tracer, 0, 0, 0, FLAT_GROUND_HEIGHT, data.groundData, groundColor, 0, FLAT_GROUND_X_FACTOR);
			dust = new Dust(data.dustData, wind, FLAT_GROUND_HEIGHT, GROUND_GROWTH, groundColor, seed);
		}
		else {
			terrains[0] = new Terrain(tracer, 0, 0, 0, height, data.groundData, groundColor, 1);
			dust = new Dust(data.dustData, wind, height, 0, groundColor, seed);
		}
		TERRAIN_POSITION_Z.setDevianceFactor(1f / terrainCount);
		for (int i = 1; i < terrainCount + 1; i++) {
			float alpha = i / (float) (terrainCount);
			float z = TERRAIN_POSITION_Z.setAlpha(alpha).get(random);
			float positionZAlpha = (TERRAIN_POSITION_Z.fullRange.getAlpha(z) - 0.5f) * 0.75f + 0.5f;
			float y = TERRAIN_POSITION_Y.setAlpha(positionZAlpha).get(random);
			TEMP_COLOR.set(groundColor).lerp(skyColor, positionZAlpha);
			terrains[i] = new Terrain(tracer, i, y, -z, height, data.terrainData[i - 1], TEMP_COLOR, 1);
		}
		sky = new Sky(seed, skyDefinition);
		float gravityY = random.nextFloat();
		particleBatch = new ParticleBatch(groundColor, new Vector2(wind, -gravityY + 1));
		float gX = flatGround ? 0 : wind * WIND_GRAVITY_INFLUENCE;
		float gY = flatGround ? -10 : -GRAVITY_Y.get(gravityY);
		physics = new Physics(gX, gY, particleBatch);
	}

	public void dispose() {
		particleBatch.dispose();
	}

	public Terrain getGround() {
		return terrains[0];
	}

	public Color getGroundColor() {
		return terrains[0].getColor();
	}

	public ParticleBatch getParticleBatch() {
		return particleBatch;
	}

	public Physics getPhysics() {
		return physics;
	}

	public void renderBackground(SolidShader g) {
		sky.render(g);
		g.setProjectionByMainCamera();
		for (int i = terrains.length - 1; i > 0; i--) {
			terrains[i].render(g);
		}
	}

	public void renderDust() {
		dust.render();
	}

	public void renderForeground(SolidShader g) {
		terrains[0].render(g);
	}

	public void renderParticle() {
		particleBatch.render();
	}

	public void update(float deltaTime, float targetX) {
		physics.update(deltaTime);
		for (Terrain t : terrains) {
			t.update(targetX);
		}
		dust.update(deltaTime, targetX);
		sky.update(deltaTime);
		particleBatch.update(deltaTime);
	}

	public void updateOnlyGraphics(float deltaTime, float targetX) {
		dust.update(deltaTime, targetX);
		sky.update(deltaTime);
	}

}
