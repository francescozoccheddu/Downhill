
package com.francescoz.downhill.components.environment.dust;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.Data;

public final class DustData {

	private final Mesh mesh;
	private final float[] vertices;
	final int particleCount;

	public DustData(int particleCount) {
		mesh = Data.getGraphics().dust.newMesh(true, true, particleCount, 0);
		vertices = new float[particleCount * 5];
		this.particleCount = particleCount;
	}

	public void dispose() {
		mesh.dispose();
	}

	void render(Color color, Particle[] particles) {
		for (int i = 0; i < particles.length; i++) {
			particles[i].pushData(vertices, i);
		}
		mesh.setVertices(vertices);
		com.francescoz.downhill.components.graphics.shaders.DustShader g = Data.getGraphics().dust;
		g.begin();
		g.setProjectionByMainCamera();
		g.render(mesh, particles.length);
		g.end();
	}

}
