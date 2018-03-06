
package com.francescoz.downhill.components.ui.glyph.fonts;

import com.badlogic.gdx.graphics.Mesh;
import com.francescoz.downhill.Data;
import com.francescoz.downhill.components.graphics.shaders.SolidShader;
import com.francescoz.downhill.components.ui.glyph.ScaledAnimatedGlyph;

public final class CachedText extends ScaledAnimatedGlyph {

	private final FontData font;
	private final float[] verts;
	private final short[] inds;
	private int indsCount;
	private final Mesh mesh;
	private String text;

	private CachedText(FontData font, float scale, int verts, int inds) {
		super(scale);
		mesh = Data.getGraphics().solid.newMesh(true, true, verts, inds);
		this.font = font;
		this.verts = new float[verts * 2];
		this.inds = new short[inds];
		text = "";
	}

	public CachedText(FontData font, float scale, String text) {
		this(font, scale, font.getStringVerts(text), font.getStringInds(text));
		setText(text);
	}

	public void dispose() {
		mesh.dispose();
	}

	@Override
	protected void renderPost(SolidShader g, float opacity, float parentX, float parentY, float scale) {
		g.setColorAlpha(opacity);
		g.setPreTransform(relPosition.x + parentX, relPosition.y + parentY, 0, scale);
		g.render(mesh, indsCount);
	}

	public final void setText(String text) {
		if (this.text.equals(text)) return;
		this.text = text;
		int len = text.length();
		indsCount = 0;
		Char[] chars = new Char[text.length()];
		for (int i = 0; i < len; i++) {
			Char c = font.getChar(text.charAt(i));
			chars[i] = c;
			if (c != null) {
				indsCount += c.getIndicesCount();
			}
		}
		float fontWidth = font.spaceWidth;
		float x = 0;
		int vInd = 0, iInd = 0;
		for (int i = 0; i < len; i++) {
			Char c = chars[i];
			if (c != null) {
				float[] v = c.vertices;
				for (int j = 0; j < v.length;) {
					verts[j + vInd] = v[j++] + x;
					verts[j + vInd] = v[j++];
				}
				short[] d = c.indices;
				int offset = vInd / 2;
				for (int j = 0; j < d.length; j++) {
					inds[j + iInd] = (short) (d[j] + offset);
				}
				vInd += v.length;
				iInd += d.length;
				x += c.width + font.spacingWidth;
			}
			else {
				x += fontWidth + font.spacingWidth;
			}
		}
		mesh.setVertices(verts);
		mesh.setIndices(inds);
		setSize(x, 1);
	}

	@Override
	public void touchDown(float x, float y) {
	}

	@Override
	public void touchDragged(float x, float y) {
	}

	@Override
	public void touchUp(float x, float y) {
	}

}
