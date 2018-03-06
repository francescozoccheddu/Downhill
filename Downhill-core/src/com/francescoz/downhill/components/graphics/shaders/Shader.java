
package com.francescoz.downhill.components.graphics.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

abstract class Shader {

	private final ShaderProgram program;
	private final int primitiveType;

	Shader(String name, int primitiveType) {
		program = new ShaderProgram(Gdx.files.internal("shaders/" + name + "/vertex_shader.glsl").readString(),
				Gdx.files.internal("shaders/" + name + "/fragment_shader.glsl").readString());
		if (!program.isCompiled()) throw new RuntimeException("Error compiling shader (" + name + "): " + program.getLog());
		this.primitiveType = primitiveType;
	}

	public void begin() {
		program.begin();
	}

	public void dispose() {
		program.dispose();
	}

	public void end() {
		program.end();
	}

	protected final int getUniformLocation(String name) {
		return program.getUniformLocation(name);
	}

	public abstract Mesh newMesh(boolean staticVertices, boolean staticIndices, int verticesCount, int indicesCount);

	protected final Mesh newMesh(boolean staticVertices, boolean staticIndices, int verticesCount, int indicesCount,
			VertexAttributes attributes) {
		return new Mesh(staticVertices, staticIndices, verticesCount, indicesCount, attributes);
	}

	public void render(Mesh mesh) {
		mesh.render(program, primitiveType);
	}

	public void render(Mesh mesh, int count) {
		mesh.render(program, primitiveType, 0, count);
	}

	public void render(Mesh mesh, int offset, int count) {
		mesh.render(program, primitiveType, offset, count);
	}

	protected final void setUniform(int location, Color value) {
		program.setUniformf(location, value);
	}

	protected final void setUniform(int location, float value) {
		program.setUniformf(location, value);
	}

	protected final void setUniform(int location, float valueX, float valueY, float valueZ) {
		program.setUniformf(location, valueX, valueY, valueZ);
	}

	protected final void setUniform(int location, Matrix4 value) {
		program.setUniformMatrix(location, value);
	}

	protected final void setUniform(String name, int value) {
		program.setUniformi(name, value);
	}

}
