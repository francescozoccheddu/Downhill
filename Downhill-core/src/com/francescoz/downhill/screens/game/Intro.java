
package com.francescoz.downhill.screens.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.tween.Tween;
import com.francescoz.downhill.components.tween.TweenCallback;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.MeshGlyph;
import com.francescoz.downhill.components.ui.glyph.fonts.DynamicText;
import com.francescoz.downhill.components.ui.glyph.layouts.Row;
import com.francescoz.downhill.components.ui.glyph.layouts.TweenColumn;
import com.francescoz.downhill.components.ui.glyph.widgets.Drawer;

final class Intro {

	private static final float SCALE = 5;
	private static final float LAYER_Z = -50;
	private final TweenColumn column;
	private final Drawer drawer;
	private final Color color;
	private boolean started;
	private boolean completed;

	public Intro(Environment environment, int seed, float localX, float onlineX) {
		UIData data = Data.getUIData();
		Locale loc = data.getLocale();
		String s1 = onlineX > 0 ? Locale.formatMeters(onlineX) : onlineX == -1 ? loc.get(59) : loc.get(19);
		String s2 = localX > 0 ? Locale.formatMeters(localX) : loc.get(18);
		DynamicText t1 = new DynamicText(data.getNormalFont(), SCALE, s1);
		DynamicText t2 = new DynamicText(data.getNormalFont(), SCALE, s2);
		DynamicText t3 = new DynamicText(data.getBoldFont(), SCALE * 4, "#" + seed);
		MeshGlyph m1 = new MeshGlyph(SCALE);
		data.getIcons().setGlyph("KingTrophy", m1);
		MeshGlyph m2 = new MeshGlyph(SCALE);
		data.getIcons().setGlyph("Trophy", m2);
		Row r1 = new Row(SCALE / 2, Align.MIDDLE, t1, m1);
		Row r2 = new Row(SCALE / 2, Align.MIDDLE, t2, m2);
		column = new TweenColumn(SCALE / 2, Align.MIDDLE, r1, r2, t3);
		drawer = new Drawer(column);
		drawer.setPosition(0, 0, LAYER_Z, Align.MIDDLE, Align.LEFT_BOTTOM);
		drawer.opacity = 0.25f;
		color = environment.getGroundColor();
		column.setEasing(Interpolation.pow4Out);
		column.setCallback(new TweenCallback() {

			@Override
			public void end(Tween tween) {
				completed = true;
			}
		});
	}

	public void render(SolidShader g) {
		if (completed) return;
		g.setColor(color);
		drawer.render(g);
	}

	public void start() {
		if (started) return;
		started = true;
		column.hide(5f);
	}

	public void update(float deltaTime) {
		if (completed) return;
		column.updateAnimation(deltaTime);
	}

}
