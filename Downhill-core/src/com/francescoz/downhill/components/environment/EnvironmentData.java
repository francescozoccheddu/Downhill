
package com.francescoz.downhill.components.environment;

import com.francescoz.downhill.components.environment.dust.DustData;
import com.francescoz.downhill.components.environment.sky.LargeStarData;
import com.francescoz.downhill.components.environment.sky.SmallStarData;
import com.francescoz.downhill.components.environment.terrain.SolidTerrainData;
import com.francescoz.downhill.components.environment.terrain.TerrainData;

public final class EnvironmentData {

	static final int MAX_TERRAINS = 4;
	final TerrainData[] terrainData;
	final SolidTerrainData groundData;
	final DustData dustData;
	public final LargeStarData largeStarData;
	public final SmallStarData smallStarData;

	public EnvironmentData() {
		groundData = new SolidTerrainData(400);
		terrainData = new TerrainData[MAX_TERRAINS];
		for (int i = 0; i < MAX_TERRAINS; i++) {
			terrainData[i] = new TerrainData(100);
		}
		dustData = new DustData(100);
		largeStarData = new LargeStarData();
		smallStarData = new SmallStarData(50);
	}

	public void dispose() {
		for (int i = 0; i < MAX_TERRAINS; i++) {
			terrainData[i].dispose();
		}
		groundData.dispose();
		dustData.dispose();
		largeStarData.dispose();
		smallStarData.dispose();
	}
}
