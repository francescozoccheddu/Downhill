
package com.francescoz.downhill.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.camera.CameraHandler;
import com.francescoz.downhill.components.graphics.ScreenBuffer;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.fonts.CachedText;
import com.francescoz.downhill.components.ui.glyph.layouts.Row;
import com.francescoz.downhill.components.ui.glyph.widgets.Drawer;
import com.francescoz.downhill.components.ui.glyph.widgets.SolidButton;

public final class PauseScreen implements CameraHandler {

	private static final float BUTTON_BORDERS = 0.1f;
	private static final float TEXT_SCALE = 0.15f;
	private static final float CAMERA_SCALE = 1.5f;
	private static final float SPACING = 0.1f;
	private static final float BACKGROUND_LAYER_Z = -50;
	private final Vector3 position;
	private final CachedText[] texts;
	private final SolidButton[] changeButtons;
	private final Drawer buttonDrawer;
	private final Drawer textDrawer;
	private Screen screen;

	public PauseScreen() {
		UIData ui = Data.getUIData();
		Locale loc = ui.getLocale();
		CachedText pauseText = new CachedText(ui.getBoldFont(), TEXT_SCALE * 100, loc.get(20));
		CachedText resumeText = new CachedText(ui.getBoldFont(), TEXT_SCALE, loc.get(21));
		CachedText restartText = new CachedText(ui.getBoldFont(), TEXT_SCALE, loc.get(22));
		CachedText exitText = new CachedText(ui.getBoldFont(), TEXT_SCALE, loc.get(23));
		texts = new CachedText[] { pauseText, restartText, exitText };
		SolidButton resume = new SolidButton(resumeText, BUTTON_BORDERS) {

			@Override
			protected void clicked() {
				screen.resume();
				
			}

			@Override
			protected void pressSpawn() {

			}
		};
		SolidButton restart = new SolidButton(restartText, BUTTON_BORDERS) {

			@Override
			protected void clicked() {
				Data.getMain().restart();
				
			}

			@Override
			protected void pressSpawn() {

			}
		};
		SolidButton exit = new SolidButton(exitText, BUTTON_BORDERS) {

			@Override
			protected void clicked() {
				Data.getMain().menu();
				
			}

			@Override
			protected void pressSpawn() {

			}
		};
		Row buttonRow = new Row(SPACING, Align.MIDDLE, resume, restart, exit);
		buttonDrawer = new Drawer(buttonRow);
		buttonDrawer.opacity = 1;
		textDrawer = new Drawer(pauseText);
		textDrawer.opacity = 0.25f;
		position = new Vector3();
		resume.setEnabled(true);
		changeButtons = new SolidButton[] { restart, exit };
	}

	public void dispose() {
		for (CachedText t : texts)
			t.dispose();
	}

	public void renderBackground(SolidShader g) {
		textDrawer.render(g);
	}

	public void renderForeground(SolidShader g) {
		ScreenBuffer b = Data.getScreenBuffer();
		b.bind();
		g.setColor(Color.WHITE);
		buttonDrawer.render(g);
		g.end();
		b.unbind();
		b.renderNormal(1);
	}

	public void set(Screen screen, float carX, float carY, boolean canChange) {
		for (SolidButton b : changeButtons)
			b.setEnabled(canChange);
		Camera.setHandler(this);
		Gdx.input.setInputProcessor(buttonDrawer);
		buttonDrawer.setPosition(carX, carY, 0, Align.MIDDLE, Align.MIDDLE);
		textDrawer.setPosition(carX, carY, BACKGROUND_LAYER_Z, Align.MIDDLE, Align.LEFT_BOTTOM);
		position.set(carX, carY, buttonDrawer.glyph.getWidth() / CAMERA_SCALE);
		this.screen = screen;
		
	}

	@Override
	public void setted(Vector3 position) {

	}

	@Override
	public void update(float deltaTime, Vector3 position) {
		position.lerp(this.position, Math.min(deltaTime * 4, 1));
	}

}
