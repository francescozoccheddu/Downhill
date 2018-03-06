
package com.francescoz.downhill.components.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.components.loaders.MeshDefinition;
import com.francescoz.downhill.components.loaders.MeshLoader;
import com.francescoz.downhill.components.ui.glyph.MeshGlyph;

public final class Icons {

	private final Map<String, Icon> icons;

	public Icons() {
		icons = new HashMap<String, Icon>(0);
		Map<String, MeshDefinition> map = MeshLoader.load("ui");
		for (Entry<String, MeshDefinition> e : map.entrySet()) {
			Icon m = new Icon(e.getKey(), e.getValue());
			icons.put(m.key, m);
		}
	}

	void dispose() {
		for (Icon m : icons.values()) {
			m.dispose();
		}
	}

	public Icon getIcon(String key) {
		return icons.get(key);
	}

	public Mesh getMesh(String key) {
		return icons.get(key).getMesh();
	}

	public void setGlyph(String key, MeshGlyph target) {
		icons.get(key).setMeshGlyph(target);
	}

}