
package com.francescoz.downhill.components;

import com.badlogic.gdx.Preferences;
import com.francescoz.downhill.Data;

public final class Score {

	private static final int PLAY_COST_RATIO = 5;
	private static final String BATT_KEY = "sc_b";
	private static final String MAX_BATT_KEY = "sc_mb";
	private static final String LOCAL_REC_KEY = "sc_lr_";
	private int batteries;
	private int maxBatteries;
	private final Preferences prefs;

	public Score() {
		prefs = Data.getPreferences();
		batteries = prefs.getInteger(BATT_KEY, 0);
		maxBatteries = prefs.getInteger(MAX_BATT_KEY, 0);
	}

	public void collectBattery() {
		batteries++;
		updateMaxBatteries();
		prefs.putInteger(BATT_KEY, batteries);
	}

	public void consumeBatteriesToPlay(int seed) {
		int required = getRequiredBatteriesToPlay(seed);
		if (required > batteries) throw new RuntimeException("Insufficient batteries");
		batteries = Math.max(0, batteries - required);
		prefs.putInteger(BATT_KEY, batteries);
	}

	public int getBatteries() {
		return batteries;
	}

	public float getLocalScore(int seed) {
		return prefs.getFloat(LOCAL_REC_KEY + seed, 0);
	}

	public int getMaxBatteries() {
		return maxBatteries;
	}

	public int getRequiredBatteriesToPlay(int seed) {
		return seed / PLAY_COST_RATIO;
	}

	public int getRequiredBatteriesToUnlock(int seed) {
		if (seed < 3) return 0;
		return (int) Math.round(Math.pow(seed + 10, 1.5f));
	}

	public void submit(int seed, float x) {
		if (x > getLocalScore(seed)) prefs.putFloat(LOCAL_REC_KEY + seed, x);
		updateMaxBatteries();
		prefs.putInteger(BATT_KEY, batteries);
		prefs.putInteger(MAX_BATT_KEY, maxBatteries);
		prefs.flush();
	}

	private void updateMaxBatteries() {
		if (batteries > maxBatteries) maxBatteries = batteries;
	}
}
