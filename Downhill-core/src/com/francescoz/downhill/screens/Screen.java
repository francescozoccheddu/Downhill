
package com.francescoz.downhill.screens;

public interface Screen {

	public void dispose();

	public void loop(float deltaTime);

	public void pause();

	public void resume();

}
