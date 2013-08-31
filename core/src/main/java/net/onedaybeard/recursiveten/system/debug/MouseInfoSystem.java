package net.onedaybeard.recursiveten.system.debug;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.Assets;
import net.onedaybeard.recursiveten.ResourceAsset;
import net.onedaybeard.recursiveten.profile.Profiler;
import net.onedaybeard.recursiveten.util.UnitCoordinates;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem
public final class MouseInfoSystem extends VoidEntitySystem
{
	private final SpriteBatch batch;
	private final BitmapFont font;

	private final OrthographicCamera screenCamera;
	
	private final UnitCoordinates coordinates;

	public MouseInfoSystem(OrthographicCamera camera, SpriteBatch batch)
	{
		super();
		this.batch = batch; 
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		screenCamera = new OrthographicCamera(w, h);
		screenCamera.position.x += w/2;
		screenCamera.position.y += h/2;
		screenCamera.update();
		
		font = Assets.getFont(ResourceAsset.SMALL_FONT);
		
		coordinates = new UnitCoordinates(camera);
	}

	@Override
	protected void processSystem()
	{
		Vector2 cursor = coordinates.coordinateAtPixel(Gdx.input.getX(), Gdx.input.getY());
		
		String mousePosition = String.format("[x: %.2f y:%.2f]",
			cursor.x, cursor.y);
		float width = font.getBounds(mousePosition).width;
		
		screenCamera.update();
		batch.setProjectionMatrix(screenCamera.combined);
		batch.begin();
			
		font.draw(batch, mousePosition, 
			(screenCamera.viewportWidth - width),
			font.getLineHeight());
		
		batch.end();
	}
}
