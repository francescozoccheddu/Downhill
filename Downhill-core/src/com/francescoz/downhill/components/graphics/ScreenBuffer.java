
package com.francescoz.downhill.components.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.ScreenShader;

public final class ScreenBuffer {

	private final int sizeRatio;
	private FrameBuffer frameBuffer;

	public ScreenBuffer(int sizeRatio) {
		this.sizeRatio = sizeRatio;
	}

	public void bind() {
		Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ZERO);
		frameBuffer.begin();
		Data.getGraphics().clear(Color.CLEAR);
	}

	public void create(int width, int height) {
		dispose();
		frameBuffer = new FrameBuffer(Format.RGBA4444, width / sizeRatio, height / sizeRatio, false);
	}

	public void dispose() {
		if (frameBuffer == null) return;
		frameBuffer.dispose();
		frameBuffer = null;
	}

	public void renderAdditive(float alpha) {
		ScreenShader g = Data.getGraphics().screen;
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		g.begin();
		g.render(frameBuffer.getColorBufferTexture(), alpha);
		g.end();
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void renderNormal(float alpha) {
		ScreenShader g = Data.getGraphics().screen;
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		g.begin();
		g.render(frameBuffer.getColorBufferTexture(), alpha);
		g.end();
	}

	public void unbind() {
		frameBuffer.end();
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
}
