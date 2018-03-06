
package com.francescoz.downhill.components.car.input;

import com.badlogic.gdx.Input.Keys;

final class Buttons {

	private boolean right, left, shift;

	boolean isLeftDown() {
		return left;
	}

	boolean isRightDown() {
		return right;
	}

	boolean isShiftDown() {
		return shift;
	}

	boolean press(int key) {
		switch (key) {
			case Keys.LEFT:
				left = true;
				return true;
			case Keys.RIGHT:
				right = true;
				return true;
			case Keys.SHIFT_RIGHT:
				shift = true;
				return true;
		}
		return false;
	}

	boolean release(int key) {
		switch (key) {
			case Keys.LEFT:
				left = false;
				return true;
			case Keys.RIGHT:
				right = false;
				return true;
			case Keys.SHIFT_RIGHT:
				shift = false;
				return true;
		}
		return false;
	}

	void releaseAll() {
		right = left = shift = false;
	}

}
