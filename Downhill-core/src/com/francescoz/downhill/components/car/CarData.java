
package com.francescoz.downhill.components.car;

import java.util.Map;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.francescoz.downhill.components.car.components.ConeLightData;
import com.francescoz.downhill.components.loaders.MeshDefinition;
import com.francescoz.downhill.components.loaders.MeshLoader;
import com.francescoz.downhill.components.loaders.ShapeLoader;

public final class CarData {

	private final Map<String, float[]> shapeMap;

	public final Mesh base;
	public final Mesh bottom;
	public final Mesh quad;
	public final Mesh baseLED;
	public final Mesh battery;
	public final Mesh batteryLED;
	public final Mesh headlight;
	public final Mesh headlightLED;
	public final Mesh motor;
	public final Mesh jet;
	public final Mesh turbo;
	public final Mesh wheel;
	public final ConeLightData coneLightData;

	public CarData() {
		shapeMap = ShapeLoader.load("car");
		Map<String, MeshDefinition> m = MeshLoader.load("car");
		base = m.get("Base").getMesh(true);
		baseLED = m.get("BaseLED").getMesh(true);
		quad = m.get("Quad").getMesh(true);
		bottom = m.get("Bottom").getMesh(true);
		battery = m.get("Battery").getMesh(true);
		batteryLED = m.get("BatteryLED").getMesh(true);
		headlight = m.get("Headlight").getMesh(true);
		headlightLED = m.get("HeadlightLED").getMesh(true);
		motor = m.get("Motor").getMesh(true);
		jet = m.get("jet").getMesh(true);
		turbo = m.get("Turbo").getMesh(true);
		wheel = m.get("Wheel").getMesh(true);
		coneLightData = new ConeLightData(5);
	}

	public void applyFixtures(String name, Body body, FixtureDef fixtureDef) {
		ShapeLoader.applyFixtures(body, fixtureDef, shapeMap, name, 1, 1, 0, 0);
	}

	public void applyFixtures(String name, Body body, FixtureDef fixtureDef, float translationX, float translationY, float scaleX,
			float scaleY) {
		ShapeLoader.applyFixtures(body, fixtureDef, shapeMap, name, scaleX, scaleY, translationX, translationY);
	}

	public void dispose() {
		base.dispose();
		bottom.dispose();
		quad.dispose();
		baseLED.dispose();
		battery.dispose();
		batteryLED.dispose();
		headlight.dispose();
		headlightLED.dispose();
		motor.dispose();
		jet.dispose();
		turbo.dispose();
		wheel.dispose();
		coneLightData.dispose();
	}
}
