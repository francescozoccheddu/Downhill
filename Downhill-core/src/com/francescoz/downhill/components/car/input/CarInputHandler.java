
package com.francescoz.downhill.components.car.input;

import com.francescoz.downhill.components.car.components.Motor.Status;

public interface CarInputHandler {

	public CarInputHandler NONE = new CarInputHandler() {

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
		}

		@Override
		public float rightjetBoost(float amount) {
			return 0;
		}

		@Override
		public boolean turbo(boolean enabled) {
			return false;
		}
	};

	public float leftjetBoost(float amount);

	public Status motor(boolean backward, boolean forward);

	public void pause();

	public float rightjetBoost(float amount);

	public boolean turbo(boolean enabled);
}
