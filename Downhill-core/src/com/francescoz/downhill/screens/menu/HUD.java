
package com.francescoz.downhill.screens.menu;

import com.badlogic.gdx.graphics.Color;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.Score;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.fonts.DynamicText;
import com.francescoz.downhill.components.ui.glyph.MeshGlyph;
import com.francescoz.downhill.components.ui.glyph.layouts.Row;
import com.francescoz.downhill.components.ui.glyph.widgets.Drawer;

final class HUD {

	private static final float SCALE = 0.2f;
	private static final float Y = -0.15f;
	private static final float DURATION = 0.5f;
	private int batteriesCount;
	private final DynamicText batteries;
	private final Drawer drawer;
	private final Score score;
	private boolean updating;

	public HUD() {
		UIData ui = Data.getUIData();
		float halfScale = SCALE / 2;
		score = Data.getScore();
		batteriesCount = -1;
		batteries = new DynamicText(ui.getNormalFont(), SCALE);
		MeshGlyph mb = new MeshGlyph(halfScale);
		ui.getIcons().setGlyph("Battery", mb);
		Row rb = new Row(halfScale, Align.MIDDLE, batteries, mb);
		drawer = new Drawer(rb);
		drawer.opacity = 0;
	}

	public void kill() {
		updating = false;
		drawer.opacity = 0;
	}

	public void render(SolidShader g) {
		g.setColor(Color.WHITE);
		drawer.render(g);
	}

	public void start() {
		updating = true;
		drawer.opacity = 0;
	}

	public void update(float deltaTime, float carX, float y) {
		if (updating) {
			drawer.opacity += deltaTime / DURATION;
			if (drawer.opacity >= 1) {
				drawer.opacity = 1;
				updating = false;
			}
		}
		int actualBatteries = score.getBatteries();
		if (actualBatteries != batteriesCount) {
			batteriesCount = actualBatteries;
			batteries.setText(Integer.toString(batteriesCount));
		}
		drawer.setPosition(carX, y + Y, 0, Align.MIDDLE, Align.RIGHT_TOP);
	}

}
