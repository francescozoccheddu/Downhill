
package com.francescoz.downhill.screens.menu.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.Score;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.environment.terrain.Terrain;
import com.francescoz.downhill.components.graphics.ScreenBuffer;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.MeshGlyph;
import com.francescoz.downhill.components.ui.glyph.fonts.DynamicText;
import com.francescoz.downhill.components.wrappers.Range;
import com.francescoz.downhill.screens.menu.views.cartel.AlertCartel;
import com.francescoz.downhill.screens.menu.views.cartel.Cartel;
import com.francescoz.downhill.screens.menu.views.cartel.DirectionalCartel;

public final class MenuViews {

	static final float HEAD_SCALE = 0.5f;
	public static final float BODY_H_SPACING = 0.1f;
	public static final float BODY_SCALE = 0.25f;
	public static final float CARTEL_SCALE = 0.15f;
	static final float BODY_V_SPACING = 0.05f;
	static final float HEAD_V_SPACING = 0.1f;
	public static final float Y = 3;
	static final float MAX_BODY_HEIGHT = 2;
	private static final float CARTEL_X = 3;
	private static final float PLAY_CARTEL_X = 9f;
	private static final RandomXS128 RANDOM = new RandomXS128();
	private static final Range CARTEL_ANGLE = new Range(-10, 10);
	private static final float LEFT_X = -16f;
	private static final float RIGHT_X = 11f;
	private final InputMultiplexer inputMultiplexer;
	private final Widget creditsWidget;
	private final Widget carTunerWidget;
	private final Widget playWidget;
	private final Play playBody;
	private final Intro introView;
	private final DirectionalCartel optionCartel;
	private final DirectionalCartel playCartel;
	private final AlertCartel cannotPlayCartel;
	private final DirectionalCartel canPlayCartel;
	private Cartel shouldPlayCartel;
	private float x;
	private boolean playable;

	public MenuViews() {
		introView = new Intro();
		creditsWidget = new Widget(new Credits(), -13);
		carTunerWidget = new Widget(new CarTuner(), -7);
		UIData ui = Data.getUIData();
		Locale loc = ui.getLocale();
		optionCartel = new DirectionalCartel(new DynamicText(ui.getBoldFont(), CARTEL_SCALE, loc.get(36)), false);
		playCartel = new DirectionalCartel(new DynamicText(ui.getBoldFont(), CARTEL_SCALE, loc.get(32)), true);
		MeshGlyph p = new MeshGlyph(0.25f);
		ui.getIcons().setGlyph("Battery", p);
		cannotPlayCartel = new AlertCartel();
		MeshGlyph b = new MeshGlyph(CARTEL_SCALE / 2);
		ui.getIcons().setGlyph("Battery", b);
		canPlayCartel = new DirectionalCartel(cannotPlayCartel.getRow(), true);
		playWidget = new Widget(playBody = new Play(this), 6);
		inputMultiplexer = new InputMultiplexer(creditsWidget.getInputProcessor(), carTunerWidget.getInputProcessor(),
				playWidget.getInputProcessor());
	}

	public void cancelOnlineRequest() {
		playBody.cancelOnlineRequest();
	}

	public void dispose() {
		creditsWidget.dispose();
		carTunerWidget.dispose();
		playWidget.dispose();
		introView.dispose();
	}

	public float getOnlineRecord() {
		return playBody.getOnlineRecord();
	}

	public float getRightX() {
		return x + RIGHT_X;
	}

	public int getSeed() {
		return playBody.getSeed();
	}

	public boolean isPlayable() {
		return playable;
	}

	public void lock() {
		playBody.setLocked(true);
	}

	public void render(SolidShader g) {
		g.setColor(Color.WHITE);
		creditsWidget.render(g);
		carTunerWidget.render(g);
		playWidget.render(g);
		introView.render(g);
	}

	public void renderCartels(SolidShader g, Color groundColor) {
		ScreenBuffer sb = Data.getScreenBuffer();
		sb.bind();
		optionCartel.render(g, groundColor);
		playCartel.render(g, groundColor);
		shouldPlayCartel.render(g, groundColor);
		g.end();
		sb.unbind();
		sb.renderNormal(1);
	}

	public void reset(float x, Terrain ground) {
		set(x, ground);
		playCartel.transform.rotationDegrees = CARTEL_ANGLE.get(RANDOM);
		optionCartel.transform.rotationDegrees = CARTEL_ANGLE.get(RANDOM);
		canPlayCartel.transform.rotationDegrees = cannotPlayCartel.transform.rotationDegrees = CARTEL_ANGLE.get(RANDOM);
		playBody.updateSeed();
		seedChanged(playBody.getSeed());
	}

	void seedChanged(int seed) {
		Score s = Data.getScore();
		int b = s.getBatteries();
		int mb = s.getMaxBatteries();
		int toUnlock = s.getRequiredBatteriesToUnlock(seed);
		playable = false;
		if (mb >= toUnlock) {
			int toPlay = s.getRequiredBatteriesToPlay(seed);
			if (b >= toPlay) {
				playable = true;
				cannotPlayCartel.setCanPlay(toPlay);
				shouldPlayCartel = canPlayCartel;
			}
			else {
				cannotPlayCartel.setLowBattery(toPlay);
				shouldPlayCartel = cannotPlayCartel;
			}
		}
		else {
			cannotPlayCartel.setLocked(toUnlock);
			shouldPlayCartel = cannotPlayCartel;
		}
		shouldPlayCartel.animate();
	}

	private void set(float x, Terrain ground) {
		creditsWidget.setX(x);
		carTunerWidget.setX(x);
		playWidget.setX(x);
		introView.setX(x);
		this.x = x;
		playCartel.transform.position.set(CARTEL_X + x, ground.getMinHeight(CARTEL_X + x, DirectionalCartel.STICK_WIDTH), 0);
		optionCartel.transform.position.set(-CARTEL_X + x, ground.getMinHeight(-CARTEL_X + x, DirectionalCartel.STICK_WIDTH), 0);
		canPlayCartel.transform.position.set(
				cannotPlayCartel.transform.position.set(PLAY_CARTEL_X + x, ground.getMinHeight(PLAY_CARTEL_X + x, Cartel.STICK_WIDTH), 0));
	}

	public void setAsInputProcessor(InputProcessor car) {
		inputMultiplexer.clear();
		inputMultiplexer.addProcessor(creditsWidget.getInputProcessor());
		// inputMultiplexer.addProcessor(intro.getInputProcessor());
		inputMultiplexer.addProcessor(playWidget.getInputProcessor());
		inputMultiplexer.addProcessor(carTunerWidget.getInputProcessor());
		inputMultiplexer.addProcessor(car);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public void unlock() {
		playBody.setLocked(false);
	}

	public void update(float deltaTime, Terrain ground) {
		float rightX = Camera.getRightX(0);
		float leftX = Camera.getLeftX(0);
		if (rightX < LEFT_X + x) {
			set(rightX - LEFT_X, ground);
		}
		else if (leftX > RIGHT_X + x) {
			set(leftX - RIGHT_X, ground);
		}
		creditsWidget.update(deltaTime);
		carTunerWidget.update(deltaTime);
		playWidget.update(deltaTime);
		shouldPlayCartel.update(deltaTime);
	}

}
