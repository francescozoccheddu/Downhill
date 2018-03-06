
package com.francescoz.downhill.components.graphics.shaders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.wrappers.Transform;

public final class SolidShader extends Shader {

	private static final VertexAttributes VERTEX_ATTRIBUTES = new VertexAttributes(new VertexAttribute(Usage.Position, 2, "a_position"));
	private boolean needsTransformUpdate;
	private final Color color;
	private final int uColorLoc;
	private final int uProjLoc;
	private final int uTransLoc;
	private final Matrix4 postTransform;
	private final Matrix4 preTransform;
	private boolean preTransformEnabled;

	public SolidShader() {
		super("solid", GL20.GL_TRIANGLES);
		postTransform = new Matrix4();
		preTransform = new Matrix4();
		begin();
		uColorLoc = getUniformLocation("u_color");
		uProjLoc = getUniformLocation("u_projection");
		uTransLoc = getUniformLocation("u_transform");
		setUniform(uTransLoc, postTransform);
		end();
		color = new Color();
	}

	@Override
	public void begin() {
		super.begin();
		setPreTransformEnabled(false);
	}

	@Override
	public Mesh newMesh(boolean staticVertices, boolean staticIndices, int verticesCount, int indicesCount) {
		return newMesh(staticVertices, staticIndices, verticesCount, indicesCount, VERTEX_ATTRIBUTES);
	}

	private void pushTransform() {
		needsTransformUpdate = false;
		if (preTransformEnabled) {
			preTransform.mulLeft(postTransform);
			setUniform(uTransLoc, preTransform);
		}
		else {
			setUniform(uTransLoc, postTransform);
		}
	}

	@Override
	public void render(Mesh mesh) {
		if (needsTransformUpdate) pushTransform();
		super.render(mesh);
	}

	@Override
	public void render(Mesh mesh, int count) {
		if (needsTransformUpdate) pushTransform();
		super.render(mesh, count);
	}

	@Override
	public void render(Mesh mesh, int offset, int count) {
		if (needsTransformUpdate) pushTransform();
		super.render(mesh, offset, count);
	}

	public void setColor(Color color) {
		if (!color.equals(this.color)) {
			this.color.set(color);
			setUniform(uColorLoc, color);
		}
	}

	public void setColor(Color color, float alpha) {
		this.color.set(color).a = alpha;
		setUniform(uColorLoc, this.color);
	}

	public void setColorAlpha(float alpha) {
		if (color.a != alpha) {
			color.a = alpha;
			setUniform(uColorLoc, color);
		}
	}

	public void setPreTransform(float x, float y, float z) {
		needsTransformUpdate = true;
		preTransform.setToTranslation(x, y, z);
	}

	public void setPreTransform(float x, float y, float z, float scaleXY) {
		needsTransformUpdate = true;
		preTransform.setToTranslationAndScaling(x, y, z, scaleXY, scaleXY, 1);
	}

	public void setPreTransform(float x, float y, float z, float scaleX, float scaleY) {
		needsTransformUpdate = true;
		preTransform.setToTranslationAndScaling(x, y, z, scaleX, scaleY, 1);
	}

	public void setPreTransform(Matrix4 transform) {
		needsTransformUpdate = true;
		preTransform.set(transform);
	}

	public void setPreTransform(Transform transform) {
		needsTransformUpdate = true;
		transform.setMatrix(preTransform);
	}

	public void setPreTransformEnabled(boolean enabled) {
		if (enabled == preTransformEnabled) return;
		needsTransformUpdate = true;
		preTransformEnabled = enabled;
	}

	public void setProjection(Matrix4 matrix) {
		setUniform(uProjLoc, matrix);
	}

	public void setProjectionByMainCamera() {
		setProjection(Camera.getProjectionMatrix());
	}

	public void setTransform(float x, float y, float z) {
		needsTransformUpdate = true;
		postTransform.setToTranslation(x, y, z);
	}

	public void setTransform(float x, float y, float z, float scaleXY) {
		needsTransformUpdate = true;
		postTransform.setToTranslationAndScaling(x, y, z, scaleXY, scaleXY, 1);
	}

	public void setTransform(float x, float y, float z, float scaleX, float scaleY) {
		needsTransformUpdate = true;
		postTransform.setToTranslationAndScaling(x, y, z, scaleX, scaleY, 1);
	}

	public void setTransform(Matrix4 transform) {
		needsTransformUpdate = true;
		postTransform.set(transform);
	}

	public void setTransform(Transform transform) {
		needsTransformUpdate = true;
		transform.setMatrix(postTransform);
	}

}
