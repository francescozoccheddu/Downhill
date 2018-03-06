
package com.francescoz.downhill.screens.menu.views;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.Score;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.Glyph;
import com.francescoz.downhill.components.ui.glyph.MeshGlyph;
import com.francescoz.downhill.components.ui.glyph.fonts.CachedText;
import com.francescoz.downhill.components.ui.glyph.fonts.DynamicText;
import com.francescoz.downhill.components.ui.glyph.layouts.Column;
import com.francescoz.downhill.components.ui.glyph.layouts.Row;
import com.francescoz.downhill.components.ui.glyph.widgets.parameter.Digit;
import com.francescoz.downhill.components.ui.glyph.widgets.parameter.ParameterListener;

final class Play extends WidgetBody {

	private static final int DIGITS = 3;
	private final Digit[] digits;
	private final CachedText text;
	private final Column column;
	private final DynamicText localScore;
	private final DynamicText onlineScore;
	private int seed;
	private final MenuViews menuViews;
	private boolean locked;
	private final Online online;
	private float onlineRecord;

	public Play(MenuViews menuViews) {
		super(32);
		online = new Online() {

			@Override
			public void response(float onlineX) {
				onlineRecord = onlineX;
				Locale l = Data.getUIData().getLocale();
				onlineScore.setText(onlineX > 0 ? Locale.formatMeters(onlineX) : onlineX == -1 ? l.get(58) : l.get(19));
				onlineScore.animate();
			}
		};
		this.menuViews = menuViews;
		UIData ui = Data.getUIData();
		text = new CachedText(ui.getNormalFont(), MenuViews.BODY_SCALE, ui.getLocale().get(33));
		digits = new Digit[DIGITS];
		ParameterListener listener = new ParameterListener() {

			@Override
			public void parameterUpdated() {
				updateSeed();
			}
		};
		for (int i = 0; i < DIGITS; i++) {
			digits[i] = new Digit(0);
			digits[i].setEnabled(true);
			digits[i].setListener(listener);
		}
		Row r1 = new Row(MenuViews.BODY_H_SPACING, Align.MIDDLE);
		r1.add(text);
		r1.add(digits);
		localScore = new DynamicText(ui.getNormalFont(), MenuViews.BODY_SCALE);
		onlineScore = new DynamicText(ui.getNormalFont(), MenuViews.BODY_SCALE);
		MeshGlyph m1 = new MeshGlyph(MenuViews.BODY_SCALE);
		ui.getIcons().setGlyph("Trophy", m1);
		MeshGlyph m2 = new MeshGlyph(MenuViews.BODY_SCALE);
		ui.getIcons().setGlyph("KingTrophy", m2);
		Row r2 = new Row(MenuViews.BODY_H_SPACING, Align.MIDDLE, localScore, m1);
		Row r3 = new Row(MenuViews.BODY_H_SPACING, Align.MIDDLE, onlineScore, m2);
		column = new Column(MenuViews.BODY_V_SPACING, Align.MIDDLE, r3, r2, r1);
		updateSeed();
	}

	public void cancelOnlineRequest() {
		online.cancel();
	}

	@Override
	public void dispose() {
		text.dispose();
	}

	@Override
	public Glyph getBody() {
		return column;
	}

	public float getOnlineRecord() {
		return onlineRecord;
	}

	public int getSeed() {
		return seed;
	}

	public void setLocked(boolean locked) {
		if (locked != this.locked) {
			this.locked = locked;
			for (Digit d : digits)
				d.setEnabled(!locked);
		}
	}

	@Override
	public void update(float deltaTime) {
		for (Digit d : digits) {
			d.updateAnimation(deltaTime);
		}
		onlineScore.updateAnimation(deltaTime);
		localScore.updateAnimation(deltaTime);
	}

	public void updateSeed() {
		online.cancel();
		seed = 0;
		for (int i = 0; i < DIGITS; i++) {
			int e = DIGITS - i - 1;
			seed += digits[i].get() * Math.pow(10, e);
		}
		Score s = Data.getScore();
		Locale loc = Data.getUIData().getLocale();
		float localX = s.getLocalScore(seed);
		String localS = localX > 0 ? Locale.formatMeters(localX) : loc.get(18);
		onlineRecord = -1;
		localScore.setText(localS);
		localScore.animate();
		onlineScore.setText(loc.get(57));
		menuViews.seedChanged(seed);
		online.request(seed, localX);
	}

}
