
package com.francescoz.downhill.components.particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.ParticleShader;

public final class ParticleData {

	private static final Array<Integer> REMOVABLES = new Array<Integer>(0);
	private final int particleCount;
	private final Mesh mesh;
	private final float[] vertices;

	public ParticleData(int particleCount) {
		this.particleCount = particleCount;
		mesh = Data.getGraphics().particle.newMesh(false, true, particleCount, 0);
		vertices = new float[particleCount * 4];
	}

	public void dispose() {
		mesh.dispose();
	}

	void render(Array<Particle> particles, Color color) {
		int count = particles.size;
		int particleIndex = 0;
		ParticleShader g = Data.getGraphics().particle;
		g.begin();
		g.setProjectionByMainCamera();
		g.setColor(color);
		while (particleIndex < count) {
			int bufferIndex = 0;
			while (bufferIndex < particleCount && particleIndex < count) {
				particles.get(particleIndex).pushData(vertices, bufferIndex);
				particleIndex++;
				bufferIndex++;
			}
			mesh.setVertices(vertices);
			g.render(mesh, bufferIndex);
		}
		g.end();
	}

	void update(float deltaTime, Array<Particle> particles, Vector2 gravity) {
		for (int i = 0; i < particles.size; i++) {
			if (!particles.get(i).update(deltaTime, gravity)) REMOVABLES.add(i);
		}
		int c = 0;
		for (int r : REMOVABLES) {
			particles.removeIndex(r - c).free();
			c++;
		}
		REMOVABLES.clear();
	}
}
