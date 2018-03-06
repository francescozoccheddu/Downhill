
package com.francescoz.downhill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.francescoz.downhill.components.Score;
import com.francescoz.downhill.components.car.CarData;
import com.francescoz.downhill.components.car.CarDefinition;
import com.francescoz.downhill.components.environment.EnvironmentData;
import com.francescoz.downhill.components.graphics.Graphics;
import com.francescoz.downhill.components.graphics.ScreenBuffer;
import com.francescoz.downhill.components.particle.ParticleData;
import com.francescoz.downhill.components.physics.PhysicsWorld;
import com.francescoz.downhill.components.powerup.PowerupData;
import com.francescoz.downhill.components.tomb.TombData;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.screens.PauseScreen;
import com.francescoz.downhill.screens.menu.views.MenuViews;

public final class Data {

	private static PhysicsWorld physicsWorld;
	private static Graphics graphics;
	private static ParticleData particleData;
	private static EnvironmentData environmentData;
	private static boolean created;
	private static CarData carData;
	private static TombData tombData;
	private static PowerupData powerupData;
	private static ScreenBuffer screenBuffer;
	private static UIData uiData;
	private static Main main;
	private static Preferences preferences;
	private static Score score;
	private static PauseScreen pauseScreen;
	private static MenuViews menuViews;
	private static CarDefinition gameCarDefinition;

	private static final CarDefinition DEFAULT_GAME_CAR_DEF;

	static {
		CarDefinition d = new CarDefinition();
		d.batteryCount.set(4);
		d.batterySizeFactor.set(.1f);
		d.landingDuration.set(0.5f);
		d.motorScale.set(0.3f);
		d.jetScale.set(0.3f);
		d.turboScale.set(0.2f);
		d.wheelAxisFrequency.set(0.5f);
		d.wheelAxisWidth.set(1f);
		d.wheelCount.set(3);
		d.wheelAxisHeight.set(0.75f);
		d.wheelSizeFactor.set(1f);
		d.motorTorqueToSpeed.set(0.7f);
		DEFAULT_GAME_CAR_DEF = d;
	}

	static void create(Main main) {
		if (created) throw new RuntimeException("Resources already created");
		created = true;
		physicsWorld = new PhysicsWorld();
		graphics = new Graphics();
		particleData = new ParticleData(200);
		environmentData = new EnvironmentData();
		carData = new CarData();
		tombData = new TombData();
		powerupData = new PowerupData();
		screenBuffer = new ScreenBuffer(1);
		preferences = Gdx.app.getPreferences("prefs");
		uiData = new UIData();
		score = new Score();
		Data.main = main;
		pauseScreen = new PauseScreen();
		gameCarDefinition = new CarDefinition("gm_cd");
		gameCarDefinition.load(preferences, DEFAULT_GAME_CAR_DEF);
		menuViews = new MenuViews();
	}

	static void dispose() {
		if (!created) throw new RuntimeException("Resources already disposed");
		created = false;
		environmentData.dispose();
		particleData.dispose();
		pauseScreen.dispose();
		graphics.dispose();
		physicsWorld.dispose();
		carData.dispose();
		tombData.dispose();
		powerupData.dispose();
		screenBuffer.dispose();
		uiData.dispose();
		menuViews.dispose();
		gameCarDefinition.save(preferences);
		preferences.flush();
	}

	public static CarData getCarData() {
		return carData;
	}

	public static EnvironmentData getEnvironmentData() {
		return environmentData;
	}

	public static CarDefinition getGameCarDefinition() {
		return gameCarDefinition;
	}

	public static Graphics getGraphics() {
		return graphics;
	}

	public static Main getMain() {
		return main;
	}

	public static MenuViews getMenuViews() {
		return menuViews;
	}

	public static ParticleData getParticleData() {
		return particleData;
	}

	public static PauseScreen getPauseScreen() {
		return pauseScreen;
	}

	public static PhysicsWorld getPhysicsWorld() {
		return physicsWorld;
	}

	public static PowerupData getPowerupData() {
		return powerupData;
	}

	public static Preferences getPreferences() {
		return preferences;
	}

	public static Score getScore() {
		return score;
	}

	public static ScreenBuffer getScreenBuffer() {
		return screenBuffer;
	}

	public static TombData getTombData() {
		return tombData;
	}

	public static UIData getUIData() {
		return uiData;
	}

	private Data() {
	}
}
