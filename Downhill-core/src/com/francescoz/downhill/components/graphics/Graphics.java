
package com.francescoz.downhill.components.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.francescoz.downhill.components.graphics.shaders.DustShader;
import com.francescoz.downhill.components.graphics.shaders.LightShader;
import com.francescoz.downhill.components.graphics.shaders.ParticleShader;
import com.francescoz.downhill.components.graphics.shaders.ScreenShader;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;

public final class Graphics {

	private static final float MAX_DELTA_TIME = 1f / 20;

	private float deltaTime;

	public final DustShader dust;

	public final LightShader light;
	public final ParticleShader particle;
	public final SolidShader solid;
	final ScreenShader screen;
	final Mesh solidScreenMesh;

	public Graphics() {
		dust = new DustShader();
		light = new LightShader();
		particle = new ParticleShader();
		solid = new SolidShader();
		screen = new ScreenShader();
		setFlags();
		solidScreenMesh = new Mesh(true, 4, 6, new VertexAttribute(Usage.Position, 2, "a_position"));
		solidScreenMesh.setVertices(new float[] { -1, -1, -1, 1, 1, 1, 1, -1 });
		solidScreenMesh.setIndices(new short[] { 0, 1, 2, 0, 2, 3 });
	}

	public void clear(Color color) {
		Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public void disableBlending() {
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public void dispose() {
		dust.dispose();
		light.dispose();
		particle.dispose();
		solid.dispose();
		screen.dispose();
		solidScreenMesh.dispose();
	}

	public void enableBlending() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
	}

	public float getDeltaTime() {
		return deltaTime;
	}

	public void resize(int width) {
		setFlags();
		dust.resize(width);
		particle.resize(width);
	}

	public void setDeltaTime() {
		deltaTime = Math.min(Gdx.graphics.getDeltaTime(), MAX_DELTA_TIME);
	}

	private void setFlags() {
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
	}
}
