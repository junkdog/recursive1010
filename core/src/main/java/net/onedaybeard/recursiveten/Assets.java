package net.onedaybeard.recursiveten;

import lombok.Getter;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Assets
{
	public static final String TAG = Assets.class.getSimpleName();
	
	@Getter private static AssetManager assetManager;
	
	private static FileResolver fileResolver = new FileResolver();

	private Assets()
	{
		
	}

	static void loadAssetManager()
	{
		int screenSize = Math.min(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
		
		Assets.assetManager = new AssetManager(fileResolver);
//		assetManager.load("uiskin.json", Skin.class);
//		for (Resource res : TextureMap.Resource.values())
//			assetManager.load(res.getRes(), Texture.class);
//		TextureMap.loadAssetManager(assetManager);
//		loadManagedResources(FontResource.SCORE);
//		loadManagedResources(FontResource.SMALL);
//		loadManagedResources(FontResource.TINY);
//		loadManagedResources(FontResource.MONO);
		for (ResourceAsset asset : ResourceAsset.values())
			assetManager.load(asset.path, asset.type);
		assetManager.load("gfx/ui/uiskin.json", Skin.class);
		assetManager.finishLoading();
	}
	
	
	public static FileHandle getFileHandle(String path)
	{
		return fileResolver.resolve(path);
	}
	
//	static void loadManagedResources(ManagedAsset... resources)
//	{
//		NTimer timer = NTimer.obtain();
//		for (ManagedAsset resource : resources)
	//		{
//			timer.start();
//			assetManager.load(resource.resource(), resource.type());
//			timer.stop();
//
//			if (isLogEnabled(DEBUG_LOG_INFO))
//			{
//				log(TAG, "Loading %s: %s (%06.2fms)",
//					resource.type().getSimpleName(),
//					resource.resource(),
//					timer.getTotalMs());
//			}
//			timer.reset();
//		}
//		NTimer.free(timer);
//	}
	
//	static void loadBackgroundTile(Background background, int tile)
//	{
//		NTimer timer = NTimer.obtain();
//		timer.start();
//		assetManager.load(background.getTile(tile), Texture.class);
//		assetManager.finishLoading();
//		timer.stop();
//		if (RebelEscape.isLogEnabled(RebelEscape.DEBUG_LOG_INFO))
//			log(TAG, "Loading texture: %s (%06.2fms)", background.getTile(tile),
//				timer.getTotalMs());
//		NTimer.free(timer);
//	}
//
//	private static Texture getTexture(Background background, int tile)
//	{
//		if (RebelEscape.isLogEnabled(RebelEscape.DEBUG_LOG_INFO))
//			log(TAG, "Assets.getTexture:background, tile".replaceAll(", ", "=%s, ") + "=%s", background, tile);
//		loadBackgroundTile(background, tile);
//		return assetManager.get(background.getTile(tile), Texture.class);
//	}

	public static BitmapFont getFont(ResourceAsset font)
	{
		assert font.type == BitmapFont.class;
		return assetManager.get(font.path, BitmapFont.class);
	}
	
//	public static BitmapFont getFont(FontResource font)
//	{
//		return assetManager.get(font.resource(), BitmapFont.class);
//	}
//	
//	public static Sound getSound(AudioResource resource)
//	{
//		return assetManager.get(resource.resource(), Sound.class);
//	}
//	
//	public static TextureAtlas getAtlas(ManagedAsset atlas)
//	{
//		assert atlas.type() == TextureAtlas.class;
//		return assetManager.get(atlas.resource(), TextureAtlas.class);
//	}
//
//	private static Array<AtlasRegion> findRegions(ManagedAsset atlas, String region)
//	{
//		Array<AtlasRegion> regions = getAtlas(atlas).findRegions(region);
//		if (regions.size == 0)
//			throw new RuntimeException("Unable to find region " + region + " in " + atlas);
//		return regions;
//	}
//	
//	public static Sprite allocateSprite(ManagedAsset atlas, SpriteAsset asset)
//	{
//		return new Sprite(findRegions(atlas, asset.resource()).first());
//	}
//	
//	static Sprite allocateSprite(Background background, int tile)
//	{
//		return ppuResize(new Sprite(getTexture(background, tile)));
//	}
//	
//	static Sprite allocateSprite(ManagedAsset resource)
//	{
//		return new Sprite(assetManager.get(resource.resource(), Texture.class));
//	}
//
//	@Deprecated
//	static Array<Sprite> allocateSprites(Resource atlas, String region, SpriteFlip flip)
//	{
//		Array<Sprite> sprites = getAtlas(atlas).createSprites(region);
//		
//		if (sprites.size == 0)
//			throw new RuntimeException("Unable to find region " + region + " in " + atlas);
//		
//		for (int i = 0; sprites.size > i; i++)
//		{
//			sprites.get(i).flip(flip.flipX(), flip.flipY());
//			ppuResize(sprites.get(i));
//		}
//		
//		return sprites;
//	}
//	
	public static Sprite allocateSprite(String resource)
	{
		return new Sprite(assetManager.get(resource, Texture.class));
	}
//	
//	public static Array<Sprite> allocateSprites(String atlas, String region)
//	{
//		TextureAtlas textureAtlas = assetManager.get(atlas, TextureAtlas.class);
//		Array<Sprite> sprites = textureAtlas.createSprites(region);
//
//		if (sprites.size == 0)
//			throw new RuntimeException("Unable to find region " + region
//				+ " in " + atlas);
//
//		for (int i = 0; sprites.size > i; i++)
//		{
//			ppuResize(sprites.get(i));
//		}
//		
//		return sprites;
//	}
	
	private static final class FileResolver implements FileHandleResolver
	{
		@Override
		public FileHandle resolve(String fileName)
		{
			if (ApplicationType.Android == Gdx.app.getType())
				return Gdx.files.internal(fileName);
			else
				return Gdx.files.local(fileName);
		}
	}

}
