
package com.francescoz.downhill.screens.menu.views.cartel;

import com.badlogic.gdx.graphics.Color;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.Icons;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.fonts.DynamicText;
import com.francescoz.downhill.components.ui.glyph.Glyph;
import com.francescoz.downhill.components.ui.glyph.MeshGlyph;
import com.francescoz.downhill.components.ui.glyph.layouts.Row;
import com.francescoz.downhill.screens.menu.views.MenuViews;

public final class AlertCartel extends Cartel {

	private static final float ICON_SIZE = 0.25f;
	private static final float SPACING = 0.1f;

	private final Row row;
	private final DynamicText text;
	private final MeshGlyph lockIcon, lowBatteryIcon;
	private MeshGlyph icon;

	public AlertCartel() {
		UIData ui = Data.getUIData();
		text = new DynamicText(ui.getBoldFont(), MenuViews.CARTEL_SCALE);
		Icons ic = ui.getIcons();
		lockIcon = new MeshGlyph(ICON_SIZE * 1.5f);
		lowBatteryIcon = new MeshGlyph(ICON_SIZE);
		ic.setGlyph("LowBattery", lowBatteryIcon);
		ic.setGlyph("Lock", lockIcon);
		MeshGlyph m = new MeshGlyph(MenuViews.CARTEL_SCALE * 0.75f);
		ic.setGlyph("Battery", m);
		row = new Row(MenuViews.BODY_H_SPACING, Align.MIDDLE, text, m);
	}

	public Glyph getRow() {
		return row;
	}

	@Override
	protected void render(SolidShader g) {
		g.setPreTransformEnabled(true);
		g.setTransform(transform);
		float textHeight = row.getHeight();
		float textWidth = row.getWidth();
		float headTextHeight = textHeight + BORDER;
		float headTextWidth = textWidth + BORDER;
		float halfHeadTextWidth = headTextWidth / 2;
		g.setPreTransform(STICK_WIDTH / -2, -STICK_WIDTH, 0, STICK_WIDTH, STICK_HEIGHT + headTextHeight + SPACING);
		g.render(quad);
		float y = STICK_HEIGHT + headTextHeight / -2 - STICK_WIDTH;
		g.setPreTransform(-halfHeadTextWidth, y, 0, headTextWidth, headTextHeight);
		g.render(quad);
		float iconWidth = icon.getWidth();
		float iconHeight = icon.getHeight();
		float headIconWidth = iconWidth + BORDER;
		float headIconHeight = iconHeight + BORDER;
		float alertY = y + headTextHeight + SPACING;
		g.setPreTransform(headIconWidth / -2, alertY, 0, headIconWidth, headIconHeight);
		g.render(quad);
		g.setColor(Color.CLEAR);
		row.render(g, 0, -textWidth / 2, y + BORDER / 2);
		icon.render(g, 0, iconWidth / -2, alertY + BORDER / 2);
		g.setPreTransformEnabled(false);
	}

	public void setCanPlay(int requiredBatteries) {
		text.setText(Data.getUIData().getLocale().get(35) + " " + requiredBatteries);
	}

	public void setLocked(int requiredBatteries) {
		text.setText(Data.getUIData().getLocale().get(34) + " " + requiredBatteries);
		icon = lockIcon;
	}

	public void setLowBattery(int requiredBatteries) {
		text.setText(Data.getUIData().getLocale().get(35) + " " + requiredBatteries);
		icon = lowBatteryIcon;
	}
}
