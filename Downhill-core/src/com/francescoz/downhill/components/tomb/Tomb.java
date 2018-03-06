
package com.francescoz.downhill.components.tomb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.camera.Camera;
import com.francescoz.downhill.components.environment.Environment;
import com.francescoz.downhill.components.graphics.ScreenBuffer;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.physics.PhysicsBody;
import com.francescoz.downhill.components.tween.RangeTween;
import com.francescoz.downhill.components.tween.Tween;

public final class Tomb implements PhysicsBody {

	private final float x;
	private final PhysicsPiece[] pieces;
	private final PhysicsPiece base;
	private final PhysicsPiece crown;
	private final Piece floor;
	private boolean destroyed;
	private boolean exploded;
	private final RangeTween opacity;
	private final Color color;

	public Tomb(Environment environment, float x, boolean hasCrown) {
		this.x = x;
		Vector2 position = new Vector2(x, environment.getGround().getHeight(x));
		pieces = new PhysicsPiece[TombData.PIECES_COUNT];
		TombData data = Data.getTombData();
		for (int i = 0; i < TombData.PIECES_COUNT; i++) {
			pieces[i] = new PhysicsPiece(data.pieces[i], position, false);
		}
		base = new PhysicsPiece(data.base, position, true);
		crown = hasCrown ? new PhysicsPiece(data.crown, position, false) : null;
		floor = new Piece(data.floor, position);
		color = environment.getGroundColor();
		opacity = new RangeTween();
		opacity.from = 1;
		opacity.to = 0;
		opacity.duration = 7;
		opacity.easing = Interpolation.pow2In;
		opacity.callback = new com.francescoz.downhill.components.tween.TweenCallback() {

			@Override
			public void end(Tween tween) {
				destroy();
			}
		};
		environment.getPhysics().addBodies(this);
	}

	public void destroy() {
		if (destroyed) return;
		for (PhysicsPiece p : pieces) {
			p.dispose();
		}
		base.dispose();
		if (crown != null) crown.dispose();
		destroyed = true;
		exploded = false;
	}

	private void explode() {
		if (destroyed || exploded) return;
		for (PhysicsPiece p : pieces) {
			p.enable();
		}
		if (crown != null) crown.enable();
		opacity.start();
		exploded = true;
	}

	@Override
	public void fixedUpdate() {

	}

	@Override
	public void interpolateTransform() {
		if (destroyed) return;
		for (PhysicsPiece p : pieces) {
			p.interpolateTransform();
		}
		if (crown != null) crown.interpolateTransform();
	}

	public void render(SolidShader g) {
		if (x - TombData.SCALE > Camera.getRightX(0) || x + TombData.SCALE < Camera.getLeftX(0)) return;
		g.setColor(color);
		base.render(g);
		floor.render(g);
		if (destroyed || exploded) return;
		else g.setColorAlpha(opacity.get());
		for (PhysicsPiece p : pieces) {
			p.render(g);
		}
		if (crown != null) crown.render(g);
	}

	public void renderPieces(SolidShader g) {
		if (exploded) {
			g.begin();
			g.setColor(color);
			g.setProjectionByMainCamera();
			ScreenBuffer screenBuffer = Data.getScreenBuffer();
			screenBuffer.bind();
			for (PhysicsPiece p : pieces) {
				p.render(g);
			}
			if (crown != null) crown.render(g);
			g.end();
			screenBuffer.unbind();
			screenBuffer.renderNormal(opacity.get());
		}
	}

	public void update(float deltaTime, float carX) {
		if (destroyed) return;
		opacity.update(deltaTime);
		if (exploded) return;
		if (carX > x) explode();
	}

	@Override
	public void updateTransform() {
		if (destroyed) return;
		for (PhysicsPiece p : pieces) {
			p.updateTransform();
		}
		if (crown != null) crown.updateTransform();
	}

}
