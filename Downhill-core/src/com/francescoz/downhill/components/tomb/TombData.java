
package com.francescoz.downhill.components.tomb;

import java.util.Map;
import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.loaders.MeshDefinition;
import com.francescoz.downhill.components.loaders.MeshLoader;
import com.francescoz.downhill.components.loaders.ShapeLoader;

public final class TombData {

	static final float SCALE = 0.6f;
	static final int PIECES_COUNT = 10;
	private static final int BASE_INDEX = 10;
	private static final int CROWN_INDEX = 11;
	final PieceData[] pieces;
	final PieceData base;
	final PieceData crown;
	final Mesh floor;

	public TombData() {
		Map<String, MeshDefinition> meshMap = MeshLoader.load("tomb");
		Map<String, float[]> shapeMap = ShapeLoader.load("tomb");
		ShapeLoader.transformShapes(shapeMap, SCALE, SCALE, 0, 0);
		pieces = new PieceData[PIECES_COUNT];
		for (int i = 0; i < PIECES_COUNT; i++) {
			String key = "Piece_" + i;
			pieces[i] = new PieceData(meshMap.get(key).getMesh(true, 0, 0, SCALE, SCALE), shapeMap.get(key));
		}
		String key = "Piece_" + BASE_INDEX;
		base = new PieceData(meshMap.get(key).getMesh(true, 0, 0, SCALE, SCALE), shapeMap.get(key));
		key = "Piece_" + CROWN_INDEX;
		crown = new PieceData(meshMap.get(key).getMesh(true, 0, 0, SCALE, SCALE), shapeMap.get(key));
		float size = SCALE * 0.75f;
		floor = Data.getGraphics().solid.newMesh(true, true, 4, 6);
		floor.setVertices(new float[] { -size, 0, size, 0, -size, -1, size, -1 });
		floor.setIndices(new short[] { 0, 1, 2, 1, 3, 2 });
	}

	public void dispose() {
		for (PieceData p : pieces) {
			p.dispose();
		}
		base.dispose();
		crown.dispose();
		floor.dispose();
	}
}
