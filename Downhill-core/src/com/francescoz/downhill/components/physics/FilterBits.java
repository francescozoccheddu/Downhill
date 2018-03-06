
package com.francescoz.downhill.components.physics;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public final class FilterBits {

	private static int count;
	private final short categoryBits;
	private short maskBits;

	public FilterBits() {
		categoryBits = (short) (1 << count++);
		maskBits = 0;
	}

	FilterBits addAllColliders() {
		maskBits = -1;
		return this;
	}

	FilterBits addAllOtherColliders() {
		maskBits = (short) (-1 ^ categoryBits);
		return this;
	}

	FilterBits addCollider(FilterBits... filters) {
		for (FilterBits f : filters) {
			maskBits |= f.categoryBits;
		}
		return this;
	}

	public short getCategory() {
		return categoryBits;
	}

	FilterBits removeCollider(FilterBits... filters) {
		for (FilterBits f : filters) {
			maskBits &= ~f.categoryBits;
		}
		return this;
	}

	public void set(Filter filter) {
		filter.categoryBits = categoryBits;
		filter.maskBits = maskBits;
	}

	public void set(FixtureDef fixtureDef) {
		set(fixtureDef.filter);
	}
}
