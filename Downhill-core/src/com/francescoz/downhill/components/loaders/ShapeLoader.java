
package com.francescoz.downhill.components.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public final class ShapeLoader {

	private static final PolygonShape SHAPE = new PolygonShape();
	private static final float[] VERTICES = new float[16];
	private static final String DELIMITER = "[ ]+";
	private static final String VERTEX = "V", SPLINE = "S";
	private static float[] vertices;
	private static Map<String, float[]> map;
	private static String tempName;
	private static int i;

	public static void applyFixtures(Body body, FixtureDef fixtureDef, Map<String, float[]> map, String name, float scaleX, float scaleY,
			float translationX, float translationY) {
		int c = 0;
		float[] vertices;
		PolygonShape shape = SHAPE;
		fixtureDef.shape = shape;
		while (map.containsKey(name + "_" + c)) {
			vertices = map.get(name + "_" + c++);
			for (int i = 0; i < vertices.length;) {
				VERTICES[i] = vertices[i++] * scaleX + translationX;
				VERTICES[i] = vertices[i++] * scaleY + translationY;
			}
			shape.set(VERTICES, 0, vertices.length);
			body.createFixture(fixtureDef);
		}
	}

	private static void flush() {
		map.put(tempName, vertices);
	}

	private static void load(Map<String, float[]> map, String fileName) {
		ShapeLoader.map = map;
		FileHandle file = Gdx.files.internal("shapes/" + fileName);
		BufferedReader reader = new BufferedReader(file.reader());
		try {
			String nextLine = reader.readLine();
			i = 0;
			while (nextLine != null) {
				processLine(nextLine);
				nextLine = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to read '" + fileName + "' file");
		}
		flush();
	}

	public static Map<String, float[]> load(String fileName) {
		Map<String, float[]> map = new HashMap<String, float[]>(0);
		load(map, fileName);
		return map;
	}

	private static void processLine(String line) {
		String[] tokens = line.split(DELIMITER);
		String type = tokens[0];
		if (type.equals(VERTEX)) {
			vertices[i++] = Float.valueOf(tokens[1]);
			vertices[i++] = Float.valueOf(tokens[2]);
		}
		else if (type.equals(SPLINE)) {
			flush();
			tempName = tokens[1];
			vertices = new float[Integer.valueOf(tokens[2]) * 2];
			i = 0;
		}
	}

	public static void transformShapes(Map<String, float[]> map, float scaleX, float scaleY, float translationX, float translationY) {
		Collection<float[]> shapes = map.values();
		for (float[] vertices : shapes) {
			for (int i = 0; i < vertices.length;) {
				vertices[i] = vertices[i++] * scaleX + translationX;
				vertices[i] = vertices[i++] * scaleY + translationY;
			}
		}
	}
}
