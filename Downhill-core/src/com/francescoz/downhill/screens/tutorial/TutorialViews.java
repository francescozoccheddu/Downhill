
package com.francescoz.downhill.screens.tutorial;

import com.badlogic.gdx.graphics.Color;
import com.francescoz.downhill.components.car.components.Motor.Status;
import com.francescoz.downhill.components.car.input.CarInputHandler;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.wrappers.SmoothValue;

class TutorialViews {

	public static final float Y = 2;
	static final int VIEW_COUNT = 6;
	final Tutorial screen;
	private final SmoothValue x;
	private final View[] views;
	private int index;

	TutorialViews(Tutorial tutorial) {
		screen = tutorial;
		views = new View[VIEW_COUNT + 2];
		x = new SmoothValue(0.25f);
		final HoldTutorialView tutForward = new HoldTutorialView(this, 37, 38, 1);
		tutForward.setInputHandler(new CarInputHandler() {

			@Override
			public float leftjetBoost(float amount) {
				return 0;
			}

			@Override
			public Status motor(boolean backward, boolean forward) {
				if (forward && !backward) {
					tutForward.pressed();
					return Status.FORWARD;
				}
				tutForward.released();
				return Status.NONE;
			}

			@Override
			public void pause() {
			}

			@Override
			public float rightjetBoost(float amount) {
				return 0;
			}

			@Override
			public boolean turbo(boolean enabled) {
				return false;
			}
		});
		views[0] = tutForward;
		final HoldTutorialView tutBackwards = new HoldTutorialView(this, 42, 43, 2);
		tutBackwards.setInputHandler(new CarInputHandler() {

			@Override
			public float leftjetBoost(float amount) {
				return 0;
			}

			@Override
			public Status motor(boolean backward, boolean forward) {
				if (backward && !forward) {
					tutBackwards.pressed();
					return Status.BACKWARD;
				}
				tutBackwards.released();
				return Status.NONE;
			}

			@Override
			public void pause() {
			}

			@Override
			public float rightjetBoost(float amount) {
				return 0;
			}

			@Override
			public boolean turbo(boolean enabled) {
				return false;
			}
		});
		views[1] = tutBackwards;
		final HoldTutorialView tutBrake = new HoldTutorialView(this, 44, 45, 3);
		tutBrake.setInputHandler(new CarInputHandler() {

			@Override
			public float leftjetBoost(float amount) {
				return 0;
			}

			@Override
			public Status motor(boolean backward, boolean forward) {
				if (backward && forward) {
					tutBrake.pressed();
					return Status.BRAKE;
				}
				tutBrake.released();
				return Status.NONE;
			}

			@Override
			public void pause() {
			}

			@Override
			public float rightjetBoost(float amount) {
				return 0;
			}

			@Override
			public boolean turbo(boolean enabled) {
				return false;
			}
		});
		views[2] = tutBrake;
		final HoldTutorialView tutTurbo = new HoldTutorialView(this, 46, 47, 4);
		tutTurbo.setInputHandler(new CarInputHandler() {

			@Override
			public float leftjetBoost(float amount) {
				return 0;
			}

			@Override
			public Status motor(boolean backward, boolean forward) {
				if (forward) { return Status.FORWARD; }
				return Status.NONE;
			}

			@Override
			public void pause() {
			}

			@Override
			public float rightjetBoost(float amount) {
				return 0;
			}

			@Override
			public boolean turbo(boolean enabled) {
				if (enabled) tutTurbo.pressed();
				else tutTurbo.released();
				return true;
			}
		});
		views[3] = tutTurbo;
		final JetTutorialView tutJet = new JetTutorialView(this, 5);
		tutJet.setInputHandler(new CarInputHandler() {

			@Override
			public float leftjetBoost(float amount) {
				processJet(amount);
				return amount;
			}

			@Override
			public Status motor(boolean backward, boolean forward) {
				return Status.NONE;
			}

			@Override
			public void pause() {
			}

			private void processJet(float amount) {
				amount = Math.abs(amount);
				tutJet.boost(amount);
			}

			@Override
			public float rightjetBoost(float amount) {
				processJet(amount);
				return amount;
			}

			@Override
			public boolean turbo(boolean enabled) {
				return false;
			}
		});
		views[4] = tutJet;
		final TutorialView tutPause = new TutorialView(this, 51, 52, 6);
		tutPause.setInputHandler(new CarInputHandler() {

			@Override
			public float leftjetBoost(float amount) {
				return 0;
			}

			@Override
			public Status motor(boolean backward, boolean forward) {
				return Status.NONE;
			}

			@Override
			public void pause() {
				tutPause.done();
				screen.pause();
			}

			@Override
			public float rightjetBoost(float amount) {
				return 0;
			}

			@Override
			public boolean turbo(boolean enabled) {
				return false;
			}
		});
		views[5] = tutPause;
		views[6] = new NoteView(this, 53, 54, 55);
		views[7] = new NoteView(this, 56, 25, 26);
	}

	public void next() {
		index++;
		if (index > VIEW_COUNT + 1) {
			screen.end();
			return;
		}
		set();
	}

	void render(SolidShader g) {
		g.setColor(Color.WHITE);
		views[index].render(g);
	}

	void reset() {
		set();
		x.setBoth(screen.car.getX());
	}

	void set() {
		views[index].restart();
	}

	void update(float deltaTime) {
		x.setNext(screen.car.getX());
		x.update(deltaTime);
		views[index].update(deltaTime, x.get());
	}

}
