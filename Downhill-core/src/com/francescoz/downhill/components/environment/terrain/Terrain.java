
package com.francescoz.downhill.components.environment.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;

public final class Terrain {

	private static final float HEIGHT = Camera.getViewWidth(Environment.CAMERA_MAX_Z);
	private final float bottom;
	private final float layerHalfWidth;
	private final Color color;
	private int firstIndex;
	private int leftSegment;
	private final float perspectiveInfluence;
	private final float segmentLenght;
	private final Matrix4 transform;
	private final TerrainTracer tracer;
	private final int layer;
	private final float sizeY;
	private final float positionY;
	private final TerrainData data;
	private final float growth;
	private final float xFactor;

	public Terrain(TerrainTracer tracer, int layer, float positionY, float positionZ, float scaleY, TerrainData data, Color color,
			float xFactor) {
		this(tracer, layer, positionY, positionZ, scaleY, data, color, 0, xFactor);
	}

	public Terrain(TerrainTracer tracer, int layer, float positionY, float positionZ, float scaleY, TerrainData data, Color color,
			float growth, float xFactor) {
		this.layer = layer;
		this.tracer = tracer;
		float layerWidth = Camera.getViewWidth(Environment.CAMERA_MAX_Z - positionZ);
		layerHalfWidth = layerWidth / 2;
		segmentLenght = layerWidth / data.segments;
		perspectiveInfluence = layerWidth / Camera.getViewWidth(Environment.CAMERA_MAX_Z);
		this.positionY = positionY * perspectiveInfluence;
		sizeY = scaleY * perspectiveInfluence;
		bottom = perspectiveInfluence * -HEIGHT;
		this.data = data;
		transform = new Matrix4();
		transform.setToTranslation(0, 0, positionZ);
		this.color = new Color(color);
		this.growth = growth;
		this.xFactor = xFactor;
		refresh();
	}

	private float[] calculateVertices(int startSegment, int count) {
		float[] vertices = new float[count * 8];
		float x = (startSegment - 1) * segmentLenght;
		float y = getHeight(x);
		for (int i = 0; i < count; i++) {
			int c = i * 8;
			vertices[c++] = x;
			vertices[c++] = y;
			vertices[c++] = x;
			vertices[c++] = bottom;
			x = (startSegment + i) * segmentLenght;
			y = getHeight(x);
			vertices[c++] = x;
			vertices[c++] = y;
			vertices[c++] = x;
			vertices[c++] = bottom;
		}
		return vertices;
	}

	public Color getColor() {
		return color;
	}

	public float getHeight(float x) {
		x /= perspectiveInfluence;
		x *= xFactor;
		return tracer.get(x, layer) * sizeY * (1 + Math.abs(x) * growth) + positionY;
	}

	public float getMaxHeight(float centerX, float radius) {
		float minX = centerX - radius;
		float maxX = centerX + radius;
		int fromSeg = (int) Math.floor(minX / segmentLenght);
		int toSeg = (int) Math.ceil(maxX / segmentLenght);
		float y = getHeight(toSeg * segmentLenght);
		for (int i = fromSeg; i < toSeg; i++) {
			float newY = getHeight(i * segmentLenght);
			if (newY > y) y = newY;
		}
		return y;
	}

	public float getMinHeight(float centerX, float radius) {
		float minX = centerX - radius;
		float maxX = centerX + radius;
		int fromSeg = (int) Math.floor(minX / segmentLenght);
		int toSeg = (int) Math.ceil(maxX / segmentLenght);
		float y = getHeight(toSeg * segmentLenght);
		for (int i = fromSeg; i < toSeg; i++) {
			float newY = getHeight(i * segmentLenght);
			if (newY < y) y = newY;
		}
		return y;
	}

	private void refresh() {
		leftSegment = 0;
		firstIndex = 0;
		data.setRange(0, calculateVertices(0, data.segments));
	}

	public void render(SolidShader g) {
		data.render(g, transform, color);
	}

	public void update(float targetX) {
		int segments = data.segments;
		int leftTargetSegment = (int) ((targetX - layerHalfWidth) / segmentLenght);
		int rightTargetSegment = (int) Math.ceil((targetX + layerHalfWidth) / segmentLenght) + 1;
		int rightSegment = leftSegment + segments;
		if (rightTargetSegment > rightSegment) {
			int count = Math.min(segments, rightTargetSegment - rightSegment);
			data.setRange(firstIndex, calculateVertices(rightSegment, count));
			firstIndex = (firstIndex + count) % segments;
			leftSegment += count;
		}
		else if (leftTargetSegment < leftSegment) {
			int count = Math.min(segments, leftSegment - leftTargetSegment);
			firstIndex = firstIndex - count;
			if (firstIndex < 0) {
				firstIndex += segments;
			}
			leftSegment -= count;
			data.setRange(firstIndex, calculateVertices(leftSegment, count));
		}
	}

}