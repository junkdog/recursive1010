package net.onedaybeard.recursiveten.system.debug;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.Assets;
import net.onedaybeard.recursiveten.ResourceAsset;
import net.onedaybeard.recursiveten.profile.Profiler;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem
public final class CameraInfoSystem extends VoidEntitySystem
{
	private final OrthographicCamera camera;
	private final SpriteBatch batch;

	private final OrthographicCamera screenCamera;

	private final BitmapFont font;
	private final String[] cameraBounds;


	public CameraInfoSystem(OrthographicCamera camera, SpriteBatch batch)
	{
		super();
		this.camera = camera;
		
		cameraBounds = new String[3];
		this.batch = batch;
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		screenCamera = new OrthographicCamera(w, h);
		screenCamera.position.x += w/2;
		screenCamera.position.y += h/2;
		screenCamera.update();
		
		font = Assets.getFont(ResourceAsset.SMALL_FONT);
	}

	@Override
	protected void processSystem()
	{
		float halfWidth = camera.viewportWidth * camera.zoom / 2;
		float halfHeight = camera.viewportHeight * camera.zoom / 2;
		
		cameraBounds[0] = String.format("%.2f", camera.position.y - halfHeight);
		cameraBounds[1] = String.format("%.2f + %.2f",
			camera.position.x - halfWidth,
			camera.position.x + halfWidth);
		cameraBounds[2] = String.format("%.2f", camera.position.y + halfHeight);
		
		screenCamera.update();
		batch.setProjectionMatrix(screenCamera.combined);
		batch.begin();
		for (int i = 0; cameraBounds.length > i; i++)
		{
			float width = font.getBounds(cameraBounds[i]).width;
			
			font.draw(batch, cameraBounds[i], 
				(screenCamera.viewportWidth - width) / 2f,
				font.getLineHeight() * (i + 1));
		}
		batch.end();
	}
}
