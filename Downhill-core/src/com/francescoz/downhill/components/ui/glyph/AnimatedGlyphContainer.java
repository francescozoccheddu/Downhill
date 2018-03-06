
package com.francescoz.downhill.components.ui.glyph;

public abstract class AnimatedGlyphContainer extends AnimatedGlyph implements GlyphSizeListener {

	public static enum Align {
		LEFT_BOTTOM(0), MIDDLE(0.5f), RIGHT_TOP(1f);

		public final float factor;

		private Align(float factor) {
			this.factor = factor;
		}

	}

	public AnimatedGlyphContainer() {
	}

}
