
package com.francescoz.downhill.components.car;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.francescoz.downhill.components.wrappers.parameter.DecimalParameter;
import com.francescoz.downhill.components.wrappers.parameter.IntegerParameter;

public final class CarDefinition {

	private static final float MIN_WHEEL_SCALE_FACTOR = 0.5f;
	private static final float WHEEL_SCALE_FACTOR = 1.2f;
	private static final float MIN_BATTERY_SCALE_FACTOR = 0.5f;
	private static final float BATTERY_SLOT_WIDTH = 0.5f;

	public final DecimalParameter headlightScale;
	public final DecimalParameter turboScale;
	public final DecimalParameter jetScale;
	public final DecimalParameter wheelSizeFactor;
	public final DecimalParameter wheelAxisWidth;
	public final DecimalParameter wheelAxisFrequency;
	public final DecimalParameter wheelAxisHeight;
	public final DecimalParameter batterySizeFactor;
	public final DecimalParameter motorScale;
	public final DecimalParameter motorTorqueToSpeed;
	public final DecimalParameter landingDuration;
	public final IntegerParameter wheelCount;
	public final IntegerParameter batteryCount;
	private final boolean serializable;

	public CarDefinition() {
		this("");
	}

	public CarDefinition(String key) {
		if (serializable = key.length() > 0) key += "_";
		headlightScale = new DecimalParameter(5, key + "he_sc", 0.1f, 0.25f);
		turboScale = new DecimalParameter(6, key + "tu_sc", 0.1f, 0.25f);
		jetScale = new DecimalParameter(7, key + "st_sc", 0.05f, 0.2f);
		wheelSizeFactor = new DecimalParameter(8, key + "wh_sf");
		wheelAxisWidth = new DecimalParameter(9, key + "wa_wi", 0.7f, 1f);
		wheelAxisFrequency = new DecimalParameter(10, key + "wa_fr", 3, 15);
		wheelAxisHeight = new DecimalParameter(11, key + "wa_he", 0.6f, 0.9f);
		batterySizeFactor = new DecimalParameter(12, key + "ba_sf");
		motorScale = new DecimalParameter(13, key + "mo_sc", 0.1f, 0.6f);
		motorTorqueToSpeed = new DecimalParameter(14, key + "mo_ts");
		landingDuration = new DecimalParameter(15, key + "la_du", 1f, 5f);
		wheelCount = new IntegerParameter(16, key + "wh_co", 2, 5);
		batteryCount = new IntegerParameter(17, key + "ba_co", 1, 5);
	}

	float getBatteryScale() {
		float maxScale = BATTERY_SLOT_WIDTH / batteryCount.get();
		float minScale = maxScale * MIN_BATTERY_SCALE_FACTOR;
		return MathUtils.lerp(minScale, maxScale, batterySizeFactor.get());
	}

	float getWheelScale() {
		float maxScale = wheelAxisWidth.get() / (wheelCount.get() - 1);
		float minScale = maxScale * MIN_WHEEL_SCALE_FACTOR;
		return MathUtils.lerp(minScale, maxScale, wheelSizeFactor.get()) * WHEEL_SCALE_FACTOR;
	}

	public void load(Preferences p, CarDefinition def) {
		if (!serializable) throw new RuntimeException("Non-Serializable car definition");
		headlightScale.load(p, def.headlightScale.getAlphaFloat());
		turboScale.load(p, def.turboScale.getAlphaFloat());
		jetScale.load(p, def.jetScale.getAlphaFloat());
		wheelSizeFactor.load(p, def.wheelSizeFactor.getAlphaFloat());
		wheelAxisWidth.load(p, def.wheelAxisWidth.getAlphaFloat());
		wheelAxisFrequency.load(p, def.wheelAxisFrequency.getAlphaFloat());
		wheelAxisHeight.load(p, def.wheelAxisHeight.getAlphaFloat());
		batterySizeFactor.load(p, def.batterySizeFactor.getAlphaFloat());
		motorScale.load(p, def.motorScale.getAlphaFloat());
		motorTorqueToSpeed.load(p, def.motorTorqueToSpeed.getAlphaFloat());
		landingDuration.load(p, def.landingDuration.getAlphaFloat());
		wheelCount.load(p, def.wheelCount.get());
		batteryCount.load(p, def.batteryCount.get());
	}

	public void save(Preferences p) {
		if (!serializable) throw new RuntimeException("Non-Serializable car definition");
		headlightScale.save(p);
		turboScale.save(p);
		jetScale.save(p);
		wheelSizeFactor.save(p);
		wheelAxisWidth.save(p);
		wheelAxisFrequency.save(p);
		wheelAxisHeight.save(p);
		batterySizeFactor.save(p);
		motorScale.save(p);
		motorTorqueToSpeed.save(p);
		landingDuration.save(p);
		wheelCount.save(p);
		batteryCount.save(p);
	}

}
