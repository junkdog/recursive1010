package net.onedaybeard.recursiveten.system.render;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.component.Cullable;
import net.onedaybeard.recursiveten.component.Renderable;
import net.onedaybeard.recursiveten.profile.Profiler;

import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.utils.ShaderLoader;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires={Renderable.class, Cullable.class})
public final class SpriteRenderSystem extends EntityProcessingSystem
{
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private PostProcessor postProcessor;
	private Bloom bloom;

	public SpriteRenderSystem(SpriteBatch batch, OrthographicCamera camera)
	{
		super(null);
		this.batch = batch;
		this.camera = camera;
		
		ShaderLoader.BasePath = "shaders/";
		postProcessor = new PostProcessor(false, false, (Gdx.app.getType() == ApplicationType.Desktop));
		bloom = new Bloom((int)(Gdx.graphics.getWidth() * 0.25f), (int)(Gdx.graphics.getHeight() * 0.25f));
//		bloom.setBaseIntesity(bloom.getBaseIntensity() * 2);
		bloom.setBloomIntesity(bloom.getBaseIntensity() * 2);
		bloom.setBlurAmount(bloom.getBlurAmount() * 2);
		postProcessor.addEffect(bloom);
	}

	@Override
	protected void begin()
	{
		postProcessor.capture();
		
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
		postProcessor.render();
	}
}
