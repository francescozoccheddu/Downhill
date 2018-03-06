
package com.francescoz.downhill.screens.game;

import com.badlogic.gdx.graphics.Color;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.Score;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.tween.Tween;
import com.francescoz.downhill.components.tween.TweenCallback;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.fonts.DynamicText;
import com.francescoz.downhill.components.ui.glyph.MeshGlyph;
import com.francescoz.downhill.components.ui.glyph.layouts.Row;
import com.francescoz.downhill.components.ui.glyph.layouts.TweenColumn;
import com.francescoz.downhill.components.ui.glyph.widgets.Drawer;

final class HUD {

	private static enum MeterStatus {

		PROGRESS("Distance", 2) {

			@Override
			public void updateText(DynamicText text, float carX, float localX, float onlineX) {
				text.setText(Locale.formatMeters(carX));
			}
		},
		LOCAL_REC("Trophy", 1) {

			@Override
			public void updateText(DynamicText text, float carX, float localX, float onlineX) {
				float distance = carX - localX;
				String t = Locale.formatMeters(distance);
				if (distance > 0) t = "+" + t;
				text.setText(t);
			}
		},
		ONLINE_REC("KingTrophy", 1) {

			@Override
			public void updateText(DynamicText text, float carX, float localX, float onlineX) {
				float distance = carX - onlineX;
				String t = Locale.formatMeters(distance);
				if (distance > 0) t = "+" + t;
				text.setText(t);
			}
		};

		private static final MeterStatus[] PROG_LOOP = new MeterStatus[] { PROGRESS };
		private static final MeterStatus[] PROG_LOC_LOOP = new MeterStatus[] { PROGRESS, LOCAL_REC };
		private static final MeterStatus[] PROG_ONL_LOOP = new MeterStatus[] { PROGRESS, ONLINE_REC };
		private static final MeterStatus[] ALL_LOOP = new MeterStatus[] { PROGRESS, LOCAL_REC, ONLINE_REC };

		public static MeterStatus[] getLoop(boolean hasLocal, boolean hasOnline) {
			if (hasLocal) {
				if (hasOnline) return ALL_LOOP;
				return PROG_LOC_LOOP;
			}
			if (hasOnline) return PROG_ONL_LOOP;
			return PROG_LOOP;
		}

		private final String icon;
		public final float duration;

		private MeterStatus(String key, float duration) {
			icon = key;
			this.duration = duration;
		}

		public void setMesh(MeshGlyph glyph) {
			Data.getUIData().getIcons().getIcon(icon).setMeshGlyph(glyph);
		}

		public abstract void updateText(DynamicText text, float carX, float localX, float onlineX);
	}

	private static final float SCALE = 0.2f;
	private static final float Y = -0.25f;
	private int batteriesCount;
	private final DynamicText batteries;
	private final DynamicText meters;
	private final MeshGlyph metersMesh;
	private final MeterStatus[] loop;
	private final Drawer drawer;
	private final float onlineX;
	private final float localX;
	private final Score score;
	private final TweenColumn column;
	private final Row metersRow;
	private float time;
	private int index;
	private boolean started;
	private boolean completed;

	public HUD(float localX, float onlineX) {
		this.localX = localX;
		this.onlineX = onlineX;
		boolean hasLocal = localX > 0;
		boolean hasOnline = onlineX > 0;
		UIData ui = Data.getUIData();
		float halfScale = SCALE / 2;
		score = Data.getScore();
		batteriesCount = -1;
		batteries = new DynamicText(ui.getNormalFont(), SCALE);
		MeshGlyph mb = new MeshGlyph(halfScale);
		ui.getIcons().setGlyph("Battery", mb);
		Row rb = new Row(halfScale, Align.MIDDLE, batteries, mb);
		meters = new DynamicText(ui.getNormalFont(), SCALE);
		metersMesh = new MeshGlyph(SCALE);
		metersRow = new Row(halfScale, Align.MIDDLE, meters, metersMesh);
		loop = MeterStatus.getLoop(hasLocal, hasOnline);
		column = new TweenColumn(halfScale / 2, Align.MIDDLE, metersRow, rb);
		drawer = new Drawer(column);
		drawer.opacity = 1;
		MeterStatus current = loop[index];
		current.setMesh(metersMesh);
		current.updateText(meters, 0, localX, onlineX);
		column.setCallback(new TweenCallback() {

			@Override
			public void end(Tween tween) {
				completed = true;
			}
		});
	}

	public void render(SolidShader g) {
		if (started) {
			g.setColor(Color.WHITE);
			drawer.render(g);
		}
	}

	public void start() {
		if (started) return;
		started = true;
		column.show(0.5f);
	}

	public void update(float deltaTime, float carX, float y) {
		if (!started) return;
		if (!completed) column.updateAnimation(deltaTime);
		int actualBatteries = score.getBatteries();
		if (actualBatteries != batteriesCount) {
			batteriesCount = actualBatteries;
			batteries.setText(Integer.toString(batteriesCount));
		}
		if (loop.length > 1) {
			MeterStatus current = loop[index];
			time += deltaTime / current.duration;
			if (time > 1) {
				time -= (int) time;
				index = (index + 1) % loop.length;
				current = loop[index];
				metersRow.animate();
				current.setMesh(metersMesh);
			}
			current.updateText(meters, carX, localX, onlineX);
			metersRow.updateAnimation(deltaTime);
		}
		else loop[0].updateText(meters, carX, localX, onlineX);
		drawer.setPosition(carX, y + Y, 0, Align.MIDDLE, Align.RIGHT_TOP);
	}

}
