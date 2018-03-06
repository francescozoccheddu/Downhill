
package com.francescoz.downhill.components.ui.glyph.fonts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.francescoz.downhill.components.loaders.MeshDefinition;
import com.francescoz.downhill.components.loaders.MeshLoader;

public final class FontData {

	private static final float SPACING_WIDTH_FACTOR = 0.05f;
	private static final float SPACE_WIDTH_FACTOR = 0.2f;
	private final Map<Character, Char> charMap;
	final float spacingWidth;
	final float spaceWidth;

	public FontData(String name) {
		Map<String, MeshDefinition> meshMap = MeshLoader.load(name);
		float totalWidth = 0;
		int charCount = meshMap.size();
		charMap = new HashMap<Character, Char>(0);
		int maxVerts = 0;
		int maxInds = 0;
		for (Entry<String, MeshDefinition> e : meshMap.entrySet()) {
			char c = e.getKey().charAt(0);
			Char obj = new Char(c, e.getValue());
			totalWidth += obj.width;
			charMap.put(c, obj);
			int vertCount = obj.getVerticesCount();
			int indsCount = obj.getIndicesCount();
			if (vertCount > maxVerts) maxVerts = vertCount;
			if (indsCount > maxInds) maxInds = indsCount;
		}
		float averageWidth = totalWidth / charCount;
		spacingWidth = averageWidth * SPACING_WIDTH_FACTOR;
		spaceWidth = averageWidth * SPACE_WIDTH_FACTOR;
	}

	public void dispose() {
		for (Char c : charMap.values()) {
			c.dispose();
		}
		charMap.clear();
	}

	Char getChar(char key) {
		return charMap.get(key);
	}

	final int getStringInds(String string) {
		int len = string.length();
		int count = 0;
		for (int i = 0; i < len; i++) {
			Char c = getChar(string.charAt(i));
			if (c != null) {
				count += c.getIndicesCount();

			}
		}
		return count;
	}

	final int getStringVerts(String string) {
		int len = string.length();
		int count = 0;
		for (int i = 0; i < len; i++) {
			Char c = getChar(string.charAt(i));
			if (c != null) {
				count += c.getVerticesCount();

			}
		}
		return count;
	}
}
