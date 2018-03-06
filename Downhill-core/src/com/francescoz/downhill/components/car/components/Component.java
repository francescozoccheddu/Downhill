
package com.francescoz.downhill.components.car.components;

import com.francescoz.downhill.components.car.Car;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.physics.PhysicsBody;

public abstract class Component implements PhysicsBody {

	protected final Car car;

	Component(Car car) {
		this.car = car;
	}

	public abstract void dispose();

	public abstract void enableBody();

	public boolean isDisposable() {
		return false;
	}

	public abstract void render(SolidShader g);

	public abstract void setGravityScale(float value);

	public abstract void update(float deltaTime);
}
