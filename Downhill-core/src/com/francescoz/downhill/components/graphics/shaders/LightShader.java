
package com.francescoz.downhill.components.graphics.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.wrappers.Transform;

public final class LightShader extends Shader {

	private static final VertexAttributes VERTEX_ATTRIBUTES = new VertexAttributes(new VertexAttribute(Usage.Position, 2, "a_position"),
			new VertexAttribute(Usage.Generic, 1, "a_alpha"));
	private final int uProjLoc;
	private final int uAlphaLoc;
	private final int uLengthLoc;
	private final int uTransLoc;
	private final Matrix4 transformMatrix;

	public LightShader() {
		super("light", GL20.GL_TRIANGLES);
		transformMatrix = new Matrix4();
		begin();
		uProjLoc = getUniformLocation("u_projection");
		uTransLoc = getUniformLocation("u_transform");
		uAlphaLoc = getUniformLocation("u_alpha");
		uLengthLoc = getUniformLocation("u_length");
		end();
	}

	@Override
	public Mesh newMesh(boolean staticVertices, boolean staticIndices, int verticesCount, int indicesCount) {
		return newMesh(staticVertices, staticIndices, verticesCount, indicesCount, VERTEX_ATTRIBUTES);
	}

	public void setAlpha(float alpha) {
		setUniform(uAlphaLoc, alpha);
	}

	public void setLength(float length) {
		setUniform(uLengthLoc, length);
	}

	public void setProjection(Matrix4 matrix) {
		setUniform(uProjLoc, matrix);
	}

	public void setProjectionByMainCamera() {
		setProjection(Camera.getProjectionMatrix());
	}

	public void setTransform(Matrix4 matrix) {
		setUniform(uTransLoc, matrix);
	}

	public void setTransform(Transform transform) {
		transform.setMatrix(transformMatrix);
		setUniform(uTransLoc, transformMatrix);
	}

}
