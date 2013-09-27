package net.onedaybeard.recursiveten.system.render;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.component.Cullable;
import net.onedaybeard.recursiveten.component.Renderable;
import net.onedaybeard.recursiveten.profile.Profiler;

import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires={Renderable.class, Cullable.class})
public final class SpriteRenderSystem extends EntityProcessingSystem
{
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	public SpriteRenderSystem(SpriteBatch batch, OrthographicCamera camera)
	{
		super(null);
		this.batch = batch;
		this.camera = camera;
	}

	@Override
	protected void begin()
	{
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	@Override
	protected void process(Entity e)
	{
		if (cullableMapper.get(e).culled)
			return;
		
		Sprite sprite = renderableMapper.get(e).sprite;
		sprite.draw(batch);
	}

	@Override
	protected void end()
	{
		batch.end();
	}
}
