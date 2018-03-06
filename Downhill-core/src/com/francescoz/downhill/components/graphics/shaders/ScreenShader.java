
package com.francescoz.downhill.components.graphics.shaders;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public final class ScreenShader extends Shader {

	private final int uAlphaLoc;
	private final Mesh mesh;

	public ScreenShader() {
		super("screen", GL20.GL_TRIANGLES);
		begin();
		uAlphaLoc = getUniformLocation("u_alpha");
		setUniform("u_texture", 0);
		end();
		mesh = new Mesh(true, 4, 6, new VertexAttributes(new VertexAttribute(Usage.Position, 2, "a_position"),
				new VertexAttribute(Usage.Generic, 2, "a_texCoord")));
		mesh.setVertices(new float[] { -1, -1, 0, 0, -1, 1, 0, 1, 1, 1, 1, 1, 1, -1, 1, 0 });
		mesh.setIndices(new short[] { 0, 1, 2, 0, 2, 3 });
	}

	@Override
	public void dispose() {
		super.dispose();
		mesh.dispose();
	}

	@Override
	public Mesh newMesh(boolean staticVertices, boolean staticIndices, int verticesCount, int indicesCount) {
		throw new RuntimeException("Use incapsulated mesh");
	}

	public void render(Texture texture, float alpha) {
		setUniform(uAlphaLoc, alpha);
		texture.bind(0);
		render(mesh);
	}

}
