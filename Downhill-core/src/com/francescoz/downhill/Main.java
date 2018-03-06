
package com.francescoz.downhill;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.environment.sky.Sky;
import com.francescoz.downhill.components.graphics.Graphics;
import com.francescoz.downhill.screens.Screen;
import com.francescoz.downhill.screens.game.Game;
import com.francescoz.downhill.screens.menu.Menu;
import com.francescoz.downhill.screens.tutorial.Tutorial;

public final class Main implements ApplicationListener {

	public static final String VERSION = "v0.4";
	private Screen screen;

	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);
		Data.create(this);
		screen = Data.getPreferences().getBoolean(Tutorial.SHOULD_LEARN_KEY, true) ? new Tutorial() : new Menu();
	}

	@Override
	public void dispose() {
		Data.dispose();
	}

	public void menu() {
		screen.dispose();
		screen = new Menu();
	}

	@Override
	public void pause() {
		Data.getPreferences().flush();
		screen.pause();
	}

	public void play(int seed, float onlineScore) {
		screen.dispose();
		screen = new Game(seed, onlineScore);
	}

	@Override
	public void render() {
		Graphics g = Data.getGraphics();
		g.setDeltaTime();
		float deltaTime = g.getDeltaTime();
		Camera.update(deltaTime);
		screen.loop(deltaTime);
	}

	@Override
	public void resize(int width, int height) {
		Data.getGraphics().resize(width);
		Data.getScreenBuffer().create(width, height);
		Camera.resize(width, height);
		Sky.setSize(width, height);
	}

	public void restart() {
		Game g = (Game) screen;
		play(g.seed, g.onlineRecordX);
	}

	@Override
	public void resume() {

	}
}
