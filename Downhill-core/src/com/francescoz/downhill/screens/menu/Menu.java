
package com.francescoz.downhill.screens.menu;

import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.camera.CameraHandler;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.car.CarDefinition;
import com.francescoz.downhill.components.car.components.Motor.Status;
import com.francescoz.downhill.components.car.input.CarInputHandler;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.graphics.ClearScreen;
import com.francescoz.downhill.components.graphics.Graphics;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.screens.Screen;
import com.francescoz.downhill.screens.menu.views.MenuViews;

public final class Menu implements Screen {

	private static final float INITIAL_Z = 1;
	private static final float Z = 4;
	private static final RandomXS128 RANDOM = new RandomXS128();
	private static final CarDefinition CAR_DEFINITION;
	static {
		CarDefinition d = new CarDefinition();
		d.headlightScale.set(0.1f);
		d.jetScale.set(0f);
		d.batteryCount.set(4);
		d.wheelCount.set(3);
		d.wheelSizeFactor.set(1f);
		d.wheelAxisWidth.set(1f);
		d.wheelAxisHeight.set(1f);
		d.wheelAxisFrequency.set(1f);
		d.motorScale.set(0.2f);
		d.motorTorqueToSpeed.set(0.75f);
		CAR_DEFINITION = d;
	}
	private final Environment environment;
	private Car car;
	private final ClearScreen clearScreen;
	private final MenuViews views;
	private final HUD hud;

	public Menu() {
		int seed = RANDOM.nextInt(999999999);
		environment = new Environment(seed, true);
		views = Data.getMenuViews();
		Camera.setHandler(new CameraHandler() {

			private static final float TRACKING_SPEED = 3f;
			private final Vector3 target = new Vector3(0, MenuViews.Y / 2, Z);

			@Override
			public void setted(Vector3 position) {

			}

			@Override
			public void update(float deltaTime, Vector3 position) {
				float carX = car.getX();
				target.x = views.isPlayable() ? Math.min(car.getX(), views.getRightX() - Camera.getViewWidth(position.z) / 2) : carX;
				position.lerp(target, deltaTime * TRACKING_SPEED);
			}
		});
		clearScreen = new ClearScreen();
		Data.getMenuViews().reset(0, environment.getGround());
		hud = new HUD();
		spawn(false);
	}

	@Override
	public void dispose() {
		Data.getGameCarDefinition().save(Data.getPreferences());
		environment.dispose();
		car.dispose();
	}

	@Override
	public void loop(float deltaTime) {
		float carX = car.getX();
		float playX = views.getRightX();
		if (carX > playX) {
			views.lock();
			if (views.isPlayable()) {
				float offset = carX - playX;
				if (offset > 0) {
					clearScreen.alpha += deltaTime * (offset + 0.25f);
					if (clearScreen.alpha >= 1) {
						Data.getGameCarDefinition().save(Data.getPreferences());
						views.cancelOnlineRequest();
						Data.getMain().play(views.getSeed(), views.getOnlineRecord());
						return;
					}
				}
				else updateClearScreen(deltaTime);
			}
			else updateClearScreen(deltaTime);
		}
		else {
			views.unlock();
			updateClearScreen(deltaTime);
		}
		car.update(deltaTime);
		hud.update(deltaTime, car.getX(), car.getY() - car.getHeight());
		views.update(deltaTime, environment.getGround());
		environment.update(deltaTime, carX);
		Graphics g = Data.getGraphics();
		SolidShader sg = g.solid;
		g.disableBlending();
		sg.begin();
		environment.renderBackground(sg);
		g.enableBlending();
		sg.end();
		environment.renderDust();
		sg.begin();
		sg.setProjectionByMainCamera();
		views.render(sg);
		views.renderCartels(sg, environment.getGroundColor());
		car.renderLight(deltaTime);
		g.disableBlending();
		sg.begin();
		sg.setProjectionByMainCamera();
		environment.renderForeground(sg);
		g.enableBlending();
		hud.render(sg);
		sg.end();
		environment.renderParticle();
		sg.begin();
		sg.setProjectionByMainCamera();
		car.render(sg);
		clearScreen.render(sg);
		g.disableBlending();
		sg.end();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	private void spawn(boolean respawn) {
		if (car != null) car.dispose();
		car = new Car(0, 0, CAR_DEFINITION, environment, false, false, true) {

			@Override
			public float requireBatteryPower(float amount) {
				return 1;
			};
		};
		car.enablePhysics();
		clearScreen.alpha = 1;
		car.setInputHandler(new CarInputHandler() {

			private boolean started;

			@Override
			public float leftjetBoost(float amount) {
				if (!started && amount != 0) start();
				return 0;
			}

			@Override
			public Status motor(boolean backward, boolean forward) {
				if (!started) {
					if (forward || backward) start();
				}
				if (forward) {
					if (backward) return Status.BRAKE;
					return Status.FORWARD;
				}
				if (backward) return Status.BACKWARD;
				return Status.NONE;
			}

			@Override
			public void pause() {
				Menu.this.pause();
			}

			@Override
			public float rightjetBoost(float amount) {
				if (!started && amount != 0) start();
				return 0;
			}

			private void start() {
				started = true;
				car.land();
				hud.start();
			}

			@Override
			public boolean turbo(boolean enabled) {
				return false;
			}
		});
		Data.getMenuViews().setAsInputProcessor(car.getInputProcessor());
		if (respawn) {
			Camera.forceSetPosition(0, MenuViews.Y / 2, Z);
		}
		else {
			float w = Camera.getViewWidth(INITIAL_Z) / 2;
			float y = environment.getGround().getMinHeight(0, w);
			Camera.forceSetPosition(0, y - w, INITIAL_Z);
		}
		hud.kill();
	}

	private void updateClearScreen(float deltaTime) {
		float angle = Math.max(1 - Math.abs(car.getRotation() / 180 - 1f) - 0.5f, 0) * 2;
		if (angle > 0) clearScreen.alpha += deltaTime * (angle + 0.1f);
		else clearScreen.alpha -= deltaTime;
		if (clearScreen.alpha < 0) clearScreen.alpha = 0;
		else if (clearScreen.alpha >= 1) {
			spawn(true);
		}
	}

}
