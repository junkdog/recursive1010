package net.onedaybeard.recursiveten;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public enum ResourceAsset
{
//	SPACE("gfx/space_sprites.txt", TextureAtlas.class),
	SMALL_FONT("gfx/ui/font.fnt", BitmapFont.class); ///,
//	BIG_FONT("fonts/bank_gothic_medium_42.fnt", BitmapFont.class);
	

	public final String path;
	public final Class<?> type;

	private ResourceAsset(String path, Class<?> type)
	{
		this.path = path;
		this.type = type;
	}
}