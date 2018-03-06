
package com.francescoz.downhill.components.car.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.car.components.Motor.Status;
import com.francescoz.downhill.components.wrappers.SmoothValue;

public final class CarInputProcessor extends InputAdapter {

	private static final float MAX_ACTIVITY = 0.25f;
	private CarInputHandler handler;
	private final Buttons buttons;
	private final Car car;
	private final SmoothValue activity;
	private final Finger[] fingers;
	private Finger left, right;

	public CarInputProcessor(Car car) {
		this.car = car;
		buttons = new Buttons();
		fingers = new Finger[] { new Finger(), new Finger() };
		activity = new SmoothValue(1 / 5f, 0, 0);
	}

	public float getActivity() {
		return activity.get();
	}

	@Override
	public boolean keyDown(int keycode) {
		return buttons.press(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.MENU || keycode == Keys.BACK || keycode == Keys.SPACE || keycode == Keys.ESCAPE) {
			handler.pause();
			return true;
		}
		return buttons.release(keycode);
	}

	public void reset() {
		buttons.releaseAll();
		fingers[0].reset();
		fingers[1].reset();
		activity.reach();
		left = right = null;
	}

	public void setHandler(CarInputHandler handler) {
		this.handler = handler;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pointer < 2) {
			Finger f = fingers[pointer];
			f.press(screenY / (float) Gdx.graphics.getHeight());
			if (screenX < Gdx.graphics.getWidth() / 2.0f) {
				if (left != null) handler.pause();
				else left = f;
			}
			else {
				if (right != null) handler.pause();
				else right = f;
			}
		}
		else {
			handler.pause();
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (pointer < 2) {
			Finger f = fingers[pointer];
			f.setY(screenY / (float) Gdx.graphics.getHeight());
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (pointer < 2) {
			Finger f = fingers[pointer];
			f.release();
			if (f == left) left = null;
			else if (f == right) right = null;
		}
		return true;
	}

	public void update(float deltaTime) {
		activity.update(deltaTime);
		fingers[0].update(deltaTime);
		fingers[1].update(deltaTime);
		float bLeft, bRight;

		bLeft = left != null ? left.getDeltaY() : 0;
		bRight = right != null ? right.getDeltaY() : 0;

		boolean tLeft = false, tRight = false, tDoubleRight = false;
		if (left != null) {
			float a = left.getActivity();
			activity.addActual(a);
			tLeft = a < MAX_ACTIVITY;
		}
		if (right != null) {
			float a = right.getActivity();
			activity.addActual(a);
			tRight = a < MAX_ACTIVITY;
			tDoubleRight = tRight && right.isDoubleTapped();
		}
		tLeft |= buttons.isLeftDown();
		tRight |= buttons.isRightDown();
		tDoubleRight |= tRight && buttons.isShiftDown();
		activity.clampActual(0, 1);
		car.setLeftjetBoost(handler.leftjetBoost(bLeft));
		car.setRightjetBoost(handler.leftjetBoost(bRight));
		Status motorStatus = handler.motor(tLeft, tRight);
		car.setMotorStatus(motorStatus);
		if (tDoubleRight && motorStatus == Status.FORWARD) {
			car.setTurbo(handler.turbo(true));
		}
		else {
			car.setTurbo(false);
			handler.turbo(false);
		}
	}

}
