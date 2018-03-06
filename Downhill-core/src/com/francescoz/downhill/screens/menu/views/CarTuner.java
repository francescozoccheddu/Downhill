
package com.francescoz.downhill.screens.menu.views;

import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.car.CarDefinition;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.Glyph;
import com.francescoz.downhill.components.ui.glyph.widgets.AutoScrollableColumn;
import com.francescoz.downhill.components.ui.glyph.widgets.parameter.Knob;

final class CarTuner extends WidgetBody {

	private final AutoScrollableColumn column;
	private final Knob[] knobs;

	public CarTuner() {
		super(31);
		knobs = new Knob[13];
		CarDefinition d = Data.getGameCarDefinition();
		knobs[0] = new Knob(d.batteryCount);
		knobs[1] = new Knob(d.batterySizeFactor);
		knobs[2] = new Knob(d.headlightScale);
		knobs[3] = new Knob(d.landingDuration);
		knobs[4] = new Knob(d.motorScale);
		knobs[5] = new Knob(d.motorTorqueToSpeed);
		knobs[6] = new Knob(d.jetScale);
		knobs[7] = new Knob(d.turboScale);
		knobs[8] = new Knob(d.wheelAxisFrequency);
		knobs[9] = new Knob(d.wheelAxisHeight);
		knobs[10] = new Knob(d.wheelAxisWidth);
		knobs[11] = new Knob(d.wheelCount);
		knobs[12] = new Knob(d.wheelSizeFactor);
		for (Knob k : knobs) {
			k.setEnabled(true);
		}
		column = new AutoScrollableColumn(MenuViews.MAX_BODY_HEIGHT, MenuViews.BODY_V_SPACING, Align.MIDDLE, 0.05f, knobs);
	}

	@Override
	public void dispose() {
		for (Knob k : knobs)
			k.dispose();
	}

	@Override
	public Glyph getBody() {
		return column;
	}

	@Override
	public void update(float deltaTime) {
		column.updateAnimation(deltaTime);
		for (Knob k : knobs) {
			k.updateAnimation(deltaTime);
		}
	}

}
