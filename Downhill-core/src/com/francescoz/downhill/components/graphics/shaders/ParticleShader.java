
package com.francescoz.downhill.components.graphics.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;
import com.francescoz.downhill.components.camera.Camera;

public final class ParticleShader extends Shader {

	private static final VertexAttributes VERTEX_ATTRIBUTES = new VertexAttributes(new VertexAttribute(Usage.Position, 2, "a_position"),
			new VertexAttribute(Usage.Generic, 1, "a_size"), new VertexAttribute(Usage.Generic, 1, "a_opacity"));

	private final int uColorLoc;
	private final int uProjLoc;
	private final int uWidthLoc;

	public ParticleShader() {
		super("particle", GL20.GL_POINTS);
		begin();
		uColorLoc = getUniformLocation("u_color");
		uProjLoc = getUniformLocation("u_projection");
		uWidthLoc = getUniformLocation("u_width");
		end();
	}

	@Override
	public void begin() {
		Gdx.gl.glEnable(0x8861);
		super.begin();
	}

	@Override
	public void end() {
		super.end();
		Gdx.gl.glDisable(0x8861);
	}

	@Override
	public Mesh newMesh(boolean staticVertices, boolean staticIndices, int verticesCount, int indicesCount) {
		return newMesh(staticVertices, staticIndices, verticesCount, indicesCount, VERTEX_ATTRIBUTES);
	}

	public void resize(int width) {
		begin();
		setSize(width);
		end();
	}

	public void setColor(Color color) {
		setUniform(uColorLoc, color.r, color.g, color.b);
	}

	public void setProjection(Matrix4 matrix) {
		setUniform(uProjLoc, matrix);
	}

	public void setProjectionByMainCamera() {
		setProjection(Camera.getProjectionMatrix());
	}

	public void setSize(int width) {
		setUniform(uWidthLoc, width);
	}
}
