
package com.francescoz.downhill.components.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

public final class MeshLoader {

	private static final String DELIMITER = "[ ]+";

	private static final String VERTEX = "v", INDEX = "f", MESH = "g";
	private static final FloatArray TEMP_VERTICES = new FloatArray(0);
	private static final ShortArray TEMP_INDICES = new ShortArray(0);
	private static Map<String, MeshDefinition> map;
	private static String tempName;
	private static short index;

	private static void flush() {
		if (TEMP_VERTICES.size < 6 || TEMP_INDICES.size < 3) { return; }
		index += (short) (TEMP_VERTICES.size / 2);
		map.put(tempName, new MeshDefinition(TEMP_VERTICES.toArray(), TEMP_INDICES.toArray()));
		TEMP_INDICES.clear();
		TEMP_VERTICES.clear();
	}

	private static void load(Map<String, MeshDefinition> map, String fileName) {
		MeshLoader.map = map;
		TEMP_VERTICES.clear();
		TEMP_INDICES.clear();
		index = 1;
		FileHandle file = Gdx.files.internal("meshes/" + fileName);
		BufferedReader reader = new BufferedReader(file.reader());
		try {
			String nextLine = reader.readLine();
			while (nextLine != null) {
				processLine(nextLine);
				nextLine = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to read '" + fileName + "' file");
		}
		flush();
	}

	public static Map<String, MeshDefinition> load(String fileName) {
		Map<String, MeshDefinition> map = new HashMap<String, MeshDefinition>(0);
		load(map, fileName);
		return map;
	}

	private static void processLine(String line) {
		String[] tokens = line.split(DELIMITER);
		String type = tokens[0];
		if (type.equals(INDEX)) {
			TEMP_INDICES.addAll((short) (Short.valueOf(tokens[1]) - index), (short) (Short.valueOf(tokens[2]) - index),
					(short) (Short.valueOf(tokens[3]) - index));
		}
		else if (type.equals(VERTEX)) {
			TEMP_VERTICES.addAll(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]));
		}
		else if (type.equals(MESH)) {
			flush();
			tempName = tokens[1];
		}
	}

	private MeshLoader() {

	}
}
