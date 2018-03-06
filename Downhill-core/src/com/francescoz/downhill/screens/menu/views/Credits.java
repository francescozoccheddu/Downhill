
package com.francescoz.downhill.screens.menu.views;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.ui.Locale;
import com.francescoz.downhill.components.ui.UIData;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.Glyph;
import com.francescoz.downhill.components.ui.glyph.fonts.CachedText;
import com.francescoz.downhill.components.ui.glyph.widgets.AutoScrollableColumn;

final class Credits extends WidgetBody {

	private static final int LAST_LOC_ROW = 30;
	private static final int ROW_COUNT = 6;
	private final CachedText[] body;
	private final AutoScrollableColumn column;

	public Credits() {
		super(24);
		UIData ui = Data.getUIData();
		Locale loc = ui.getLocale();
		body = new CachedText[ROW_COUNT];
		for (int i = 0; i < ROW_COUNT; i++) {
			body[i] = new CachedText(ui.getNormalFont(), MenuViews.BODY_SCALE, loc.get(LAST_LOC_ROW - i));
		}
		column = new AutoScrollableColumn(MenuViews.MAX_BODY_HEIGHT, MenuViews.BODY_V_SPACING, Align.MIDDLE, 0.1f, body);
	}

	@Override
	public void dispose() {
		for (CachedText t : body)
			t.dispose();
	}

	@Override
	public Glyph getBody() {
		return column;
	}

	@Override
	public void update(float deltaTime) {
		column.updateAnimation(deltaTime);
	}

}
