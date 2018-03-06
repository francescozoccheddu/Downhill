
package com.francescoz.downhill.components.ui.glyph.fonts;

import com.badlogic.gdx.utils.Array;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.ScaledAnimatedGlyph;

public final class DynamicText extends ScaledAnimatedGlyph {

	private String text;
	private final FontData font;
	private final Array<Char> chars;

	public DynamicText(FontData font, float scale) {
		this(font, scale, "");
	}

	public DynamicText(FontData font, float scale, String text) {
		super(scale);
		this.font = font;
		this.text = "";
		chars = new Array<Char>(0);
		setText(text);
	}

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY, float scale) {
		g.setColorAlpha(opacity);
		float translationX = relPosition.x + parentX;
		float translationY = relPosition.y + parentY;
		for (Char c : chars) {
			if (c == null) {
				translationX += (font.spaceWidth + font.spacingWidth) * scale;
			}
			else {
				g.setPreTransform(translationX, translationY, 0, scale);
				c.render(g);
				translationX += (c.width + font.spacingWidth) * scale;
			}
		}
	}

	public void setText(String text) {
		if (this.text.equals(text)) return;
		this.text = text;
		chars.clear();
		int len = text.length();
		float width = 0;
		for (int i = 0; i < len; i++) {
			Char c = font.getChar(text.charAt(i));
			chars.add(c);
			if (c == null) {
				width += font.spaceWidth;
			}
			else {
				width += c.width;
			}
		}
		int spaceCount = len - 1;
		if (spaceCount > 0) {
			width += font.spacingWidth * spaceCount;
		}
		setSize(width, 1);
	}

	@Override
	public void touchDown(float x, float y) {
	}

	@Override
	public void touchDragged(float x, float y) {
	}

	@Override
	public void touchUp(float x, float y) {
	}

}
