
package com.francescoz.downhill.components.ui.glyph.widgets.parameter;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.ui.glyph.MeshGlyph;
import com.francescoz.downhill.components.ui.glyph.widgets.Button;

abstract class Tweak extends Button {

	static enum Direction {
		UP("U"), DOWN("D"), LEFT("L"), RIGHT("R");

		private final String icon;

		private Direction(String iconSuffix) {
			icon = "Triangle" + iconSuffix;
		}

		public void setMeshGlyph(MeshGlyph meshGlyph) {
			Data.getUIData().getIcons().getIcon(icon).setMeshGlyph(meshGlyph);
		}
	}

	private static final float BORDER = 0.4f * Knob.SCALE;
	private static final float SCALE = Knob.SCALE;

	public Tweak(Direction direction) {
		super(new MeshGlyph(SCALE), BORDER);
		direction.setMeshGlyph((MeshGlyph) glyph);
	}

}
