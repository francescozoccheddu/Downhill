
package com.francescoz.downhill.screens.menu.views;

import com.badlogic.gdx.InputProcessor;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.Main;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.Icons;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.MeshGlyph;
import com.francescoz.downhill.components.ui.glyph.fonts.CachedText;
import com.francescoz.downhill.components.ui.glyph.layouts.Column;
import com.francescoz.downhill.components.ui.glyph.layouts.Row;
import com.francescoz.downhill.components.ui.glyph.widgets.Drawer;

final class Intro extends View {

	private final Drawer title;
	private final Drawer body;
	private final CachedText t1, t2;

	public Intro() {
		super(0);
		UIData ui = Data.getUIData();
		Icons ic = ui.getIcons();
		MeshGlyph tm = new MeshGlyph(2);
		ic.setGlyph("Text", tm);
		MeshGlyph im = new MeshGlyph(0.4f);
		ic.setGlyph("Icon", im);
		t1 = new CachedText(ui.getNormalFont(), 0.15f, "Downhill Project " + Main.VERSION);
		t2 = new CachedText(ui.getNormalFont(), 0.1f, "by Francesco Zoccheddu");
		Column c = new Column(MenuViews.BODY_V_SPACING, Align.LEFT_BOTTOM, t2, t1);
		Row r = new Row(MenuViews.BODY_H_SPACING, Align.MIDDLE, im, c);
		title = new Drawer(tm);
		body = new Drawer(r);
	}

	@Override
	public void dispose() {
		t1.dispose();
		t2.dispose();
	}

	@Override
	public InputProcessor getInputProcessor() {
		return null;
	}

	@Override
	public void render(SolidShader g) {
		float x = title.position.x;
		if (Camera.getRightX(0) < x || Camera.getLeftX(0) > x + title.glyph.getWidth()) return;
		title.render(g);
		body.render(g);
	}

	@Override
	public void setX(float x) {
		title.setPosition(x, MenuViews.Y, 0, Align.MIDDLE, Align.RIGHT_TOP);
		body.setPosition(x, MenuViews.Y - 2, 0, Align.MIDDLE, Align.RIGHT_TOP);
	}

	@Override
	public void update(float deltaTime) {

	}

}
