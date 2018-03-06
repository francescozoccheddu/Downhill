
package com.francescoz.downhill.components.ui.glyph.widgets;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.AnimatedGlyphContainer.Align;
import com.francescoz.downhill.components.ui.glyph.Glyph;

public final class Drawer extends InputAdapter {

	private final Vector2 touch;
	public final Vector3 position;
	public float opacity;
	public final Glyph glyph;

	public Drawer(Glyph glyph) {
		this.glyph = glyph;
		position = new Vector3();
		touch = new Vector2();
		opacity = 1;
	}

	public void render(SolidShader g) {
		g.setPreTransformEnabled(true);
		g.setTransform(0, 0, position.z);
		glyph.render(g, opacity, position.x, position.y);
		g.setPreTransformEnabled(false);
	}

	public void setPosition(float x, float y, float z, Align horizontalAlign, Align verticalAlign) {
		position.set(x, y, z).sub(glyph.getWidth() * horizontalAlign.factor, glyph.getHeight() * verticalAlign.factor, 0);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		unproject(screenX, screenY);
		glyph.touchDown(touch.x, touch.y);
		return glyph.isContained(touch.x, touch.y);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		unproject(screenX, screenY);
		glyph.touchDragged(touch.x, touch.y);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		unproject(screenX, screenY);
		glyph.touchUp(touch.x, touch.y);
		return false;
	}

	private void unproject(int screenX, int screenY) {
		touch.set(Camera.unproject(screenX, screenY).sub(position.x, position.y));
		if (/*position.z!=0*/ true) {
			// FIXME
		}
	}

}
