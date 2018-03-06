
package com.francescoz.downhill.screens.menu.views;

import com.badlogic.gdx.InputProcessor;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.fonts.CachedText;
import com.francescoz.downhill.components.ui.glyph.layouts.Column;
import com.francescoz.downhill.components.ui.glyph.widgets.Drawer;

final class Widget extends View {

	private final Drawer drawer;
	private final CachedText text;
	private final WidgetBody body;

	public Widget(WidgetBody body, float x) {
		super(x);
		this.body = body;
		UIData ui = Data.getUIData();
		text = new CachedText(ui.getBoldFont(), MenuViews.HEAD_SCALE, body.getHeader());
		Column c = new Column(MenuViews.HEAD_V_SPACING, Align.MIDDLE, body.getBody(), text);
		drawer = new Drawer(c);
		drawer.opacity = 1f;
	}

	@Override
	public void dispose() {
		text.dispose();
		body.dispose();
	}

	@Override
	public InputProcessor getInputProcessor() {
		return drawer;
	}

	@Override
	public void render(SolidShader g) {
		float x = drawer.position.x;
		if (Camera.getRightX(0) < x || Camera.getLeftX(0) > x + drawer.glyph.getWidth()) return;
		drawer.render(g);
	}

	@Override
	public void setX(float x) {
		drawer.setPosition(x + offsetX, MenuViews.Y, 0, Align.MIDDLE, Align.RIGHT_TOP);
	}

	@Override
	public void update(float deltaTime) {
		body.update(deltaTime);
	}

}
