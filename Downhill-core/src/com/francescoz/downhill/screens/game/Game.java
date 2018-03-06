
package com.francescoz.downhill.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.Score;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.car.components.Motor.Status;
import com.francescoz.downhill.components.car.input.CarInputHandler;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.graphics.Graphics;
import com.francescoz.downhill.components.graphics.TweenClearScreen;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.powerup.PowerupSpawner;
import com.francescoz.downhill.components.tomb.Tomb;
import com.francescoz.downhill.components.tween.RangeTween;
import com.francescoz.downhill.components.tween.Tween;
import com.francescoz.downhill.components.tween.TweenCallback;
import com.francescoz.downhill.screens.Screen;

public final class Game implements Screen {

	private static final float Z = 1;
	private final Environment environment;
	private final Car car;
	private final GameCamera gameCamera;
	private final PowerupSpawner powerupSpawner;
	private final float localRecordX;
	public final float onlineRecordX;
	public final int seed;
	private final Intro intro;
	private final Tomb[] tombs;
	private final HUD hud;
	private final TweenClearScreen clearScreen;
	private boolean paused;
	private boolean clearing;

	public Game(int seed, float onlineScore) {
		this.seed = seed;
		Score score = Data.getScore();
		score.consumeBatteriesToPlay(seed);
		localRecordX = score.getLocalScore(seed);
		onlineRecordX = onlineScore;
		environment = new Environment(seed, false);
		intro = new Intro(environment, seed, localRecordX, onlineRecordX);
		car = new Car(0, 0, Data.getGameCarDefinition(), environment);
		car.enablePhysics();
		gameCamera = new GameCamera(car);
		Camera.setHandler(gameCamera);
		float w = Camera.getViewWidth(Z) / 2;
		float y = environment.getGround().getMinHeight(0, w);
		Camera.forceSetPosition(0, y - w, Z);
		hud = new HUD(localRecordX, onlineRecordX);
		car.setInputHandler(new CarInputHandler() {

			private boolean started;

			@Override
			public float leftjetBoost(float amount) {
				if (!started && amount != 0) start();
				return amount;
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
				Game.this.pause();
			}

			@Override
			public float rightjetBoost(float amount) {
				if (!started && amount != 0) start();
				return amount;
			}

			private void start() {
				started = true;
				intro.start();
				hud.start();
				car.land();
			}

			@Override
			public boolean turbo(boolean enabled) {
				return true;
			}
		});
		Gdx.input.setInputProcessor(car.getInputProcessor());
		Array<Tomb> tombs = new Array<Tomb>(0);
		if (localRecordX > 0) tombs.add(new Tomb(environment, localRecordX, false));
		if (onlineRecordX > 0) tombs.add(new Tomb(environment, onlineRecordX, true));
		this.tombs = tombs.toArray(Tomb.class);
		powerupSpawner = new PowerupSpawner(car, environment);
		clearing = true;
		clearScreen = new TweenClearScreen();
		RangeTween t = clearScreen.tween;
		t.duration = 1;
		t.easing = Interpolation.pow2Out;
		t.from = 1;
		t.to = 0;
		t.callback = new TweenCallback() {

			@Override
			public void end(Tween tween) {
				clearing = false;
			}
		};
		t.start();
	}

	@Override
	public void dispose() {
		Data.getScore().submit(seed, car.getX());
		environment.dispose();
		car.dispose();
		for (Tomb t : tombs) {
			t.destroy();
		}
		powerupSpawner.dispose();
	}

	@Override
	public void loop(float deltaTime) {
		float carX = car.getX();
		if (paused) environment.updateOnlyGraphics(deltaTime, carX);
		else {
			car.update(deltaTime);
			environment.update(deltaTime, carX);
			powerupSpawner.update(deltaTime);
			intro.update(deltaTime);
			for (Tomb t : tombs) {
				t.update(deltaTime, carX);
			}
		}
		hud.update(deltaTime, carX, car.getY() - car.getHeight());
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
		else intro.render(sg);
		g.disableBlending();
		for (Tomb t : tombs) {
			t.render(sg);
		}
		g.enableBlending();
		powerupSpawner.render(sg);
		sg.end();
		for (Tomb t : tombs) {
			t.renderPieces(sg);
		}
		environment.renderDust();
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
		if (clearing) {
			clearScreen.update(deltaTime);
			clearScreen.render(sg);
			sg.setProjectionByMainCamera();
		}
		if (paused) Data.getPauseScreen().renderForeground(sg);
		g.disableBlending();
		sg.end();
	}

	@Override
	public void pause() {
		car.resetInput();
		Data.getPauseScreen().set(this, car.getX(), car.getY(), true);
		paused = true;
	}

	@Override
	public void resume() {
		car.resetInput();
		Gdx.input.setInputProcessor(car.getInputProcessor());
		Camera.setHandler(gameCamera);
		paused = false;
	}

}
