
package com.francescoz.downhill.components.graphics;

import com.francescoz.downhill.components.tween.RangeTween;

public final class TweenClearScreen extends ClearScreen {

	public final RangeTween tween;

	public TweenClearScreen() {
		tween = new RangeTween();
	}

	public void update(float deltaTime) {
		tween.update(deltaTime);
		alpha = tween.get();
	}

}
