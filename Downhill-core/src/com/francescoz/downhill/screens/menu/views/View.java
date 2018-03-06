
package com.francescoz.downhill.screens.menu.views;

import com.badlogic.gdx.InputProcessor;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;

abstract class View {

	protected final float offsetX;

	public View(float offsetX) {
		this.offsetX = offsetX;
	}

	public abstract void dispose();

	public abstract InputProcessor getInputProcessor();

	public abstract void render(SolidShader g);

	public abstract void setX(float x);

	public abstract void update(float deltaTime);

}
