
package com.francescoz.downhill.screens.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.camera.CameraHandler;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.car.CarDefinition;
import com.francescoz.downhill.components.car.input.CarInputHandler;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.graphics.ClearScreen;
import com.francescoz.downhill.components.graphics.Graphics;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.screens.Screen;
import com.francescoz.downhill.screens.menu.views.MenuViews;

public final class Tutorial implements Screen {

	public static final String SHOULD_LEARN_KEY = "tut_sl";
	private static final float INITIAL_Z = 40;
	private static final float Z = 3;
	private static final RandomXS128 RANDOM = new RandomXS128();
	private static final CarDefinition CAR_DEFINITION;
	private static final float DURATION = 3;
	static {
		CarDefinition d = new CarDefinition();
		d.headlightScale.set(0.1f);
		d.jetScale.set(0.3f);
		d.turboScale.set(0.5f);
		d.batteryCount.set(4);
		d.wheelCount.set(3);
		d.wheelSizeFactor.set(1f);
		d.wheelAxisWidth.set(1f);
		d.wheelAxisHeight.set(1f);
		d.wheelAxisFrequency.set(1f);
		d.motorScale.set(0.15f);
		d.motorTorqueToSpeed.set(0.5f);
		CAR_DEFINITION = d;
	}
	private final Environment environment;
	private final TutorialViews views;
	Car car;
	private final ClearScreen clearScreen;
	private boolean paused;
	private final CameraHandler cameraHandler;
	private float starting;
	private boolean end;

	public Tutorial() {
		int seed = RANDOM.nextInt(999999999);
		environment = new Environment(seed, true);
		Camera.setHandler(cameraHandler = new CameraHandler() {

			private static final float TRACKING_SPEED = 2f;
			private final Vector3 target = new Vector3(0, TutorialViews.Y / 2, Z);

			@Override
			public void setted(Vector3 position) {
			}

			@Override
			public void update(float deltaTime, Vector3 position) {
				target.x = car.getX();
				position.lerp(target, deltaTime * TRACKING_SPEED);
			}
		});
		clearScreen = new ClearScreen();
		Data.getMenuViews().reset(0, environment.getGround());
		views = new TutorialViews(this);
		spawn();
		starting = DURATION;
	}

	@Override
	public void dispose() {
		environment.dispose();
		car.dispose();
		Data.getPreferences().flush();
	}

	void end() {
		if (end) return;
		end = true;
		Data.getPreferences().putBoolean(SHOULD_LEARN_KEY, false);
	}

	@Override
	public void loop(float deltaTime) {
		if (starting > 0) {
			starting -= deltaTime;
			if (starting <= 0) {
				views.reset();
			}
		}
		float carX = car.getX();
		if (paused) environment.updateOnlyGraphics(deltaTime, carX);
		else {
			car.update(deltaTime);
			environment.update(deltaTime, carX);
			if (starting <= 0 && !end) views.update(deltaTime);
		}
		updateClearScreen(deltaTime);
		Graphics g = Data.getGraphics();
		SolidShader sg = g.solid;
		g.disableBlending();
		sg.begin();
		environment.renderBackground(sg);
		g.enableBlending();
		if (paused) {
			sg.setColor(environment.getGroundColor());
			Data.getPauseScreen().renderBackground(sg);
		}
		else if (starting <= 0 && !end) views.render(sg);
		sg.end();
		environment.renderDust();
		sg.begin();
		sg.setProjectionByMainCamera();
		car.renderLight(deltaTime);
		g.disableBlending();
		sg.begin();
		sg.setProjectionByMainCamera();
		environment.renderForeground(sg);
		g.enableBlending();
		sg.end();
		environment.renderParticle();
		sg.begin();
		sg.setProjectionByMainCamera();
		car.render(sg);
		if (paused) Data.getPauseScreen().renderForeground(sg);
		clearScreen.render(sg);
		g.disableBlending();
		sg.end();
	}

	@Override
	public void pause() {
		car.resetInput();
		Data.getPauseScreen().set(this, car.getX(), car.getY(), false);
		paused = true;
	}

	@Override
	public void resume() {
		car.resetInput();
		Gdx.input.setInputProcessor(car.getInputProcessor());
		Camera.setHandler(cameraHandler);
		paused = false;
	}

	private void spawn() {
		if (car != null) car.dispose();
		car = new Car(0, 0, CAR_DEFINITION, environment) {

			@Override
			public float requireBatteryPower(float amount) {
				return 1;
			};
		};
		car.enablePhysics();
		clearScreen.alpha = 1;
		if (starting <= 0) views.reset();
		car.setInputHandler(CarInputHandler.NONE);
		Gdx.input.setInputProcessor(car.getInputProcessor());
		Camera.forceSetPosition(0, MenuViews.Y / 2, INITIAL_Z);
	}

	private void updateClearScreen(float deltaTime) {
		if (end) {
			clearScreen.alpha += deltaTime / DURATION;
			if (clearScreen.alpha >= 1) {
				Data.getMain().play(0, 0);
			}
			return;
		}
		float angle = Math.max(1 - Math.abs(car.getRotation() / 180 - 1f) - 0.5f, 0) * 2;
		if (angle > 0) clearScreen.alpha += deltaTime * (angle + 0.1f);
		else clearScreen.alpha -= deltaTime / DURATION;
		if (clearScreen.alpha < 0) clearScreen.alpha = 0;
		else if (clearScreen.alpha >= 1) {
			spawn();
		}
	}

}
