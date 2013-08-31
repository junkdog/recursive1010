package net.onedaybeard.recursiveten.system.render;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.component.BackgroundColor;
import net.onedaybeard.recursiveten.profile.Profiler;

import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires=BackgroundColor.class)
public final class BackgroundRenderSystem extends EntityProcessingSystem
{
	private float totalTime;
	private float maxTime = 10f;
	
	public BackgroundRenderSystem()
	{
		super(null);
	}
	
	@Override
	protected void begin()
	{
		totalTime += world.getDelta();
	}

	@Override
	protected void process(Entity e)
	{
		Color bg = backgroundColorMapper.get(e).value;
		bg.r = 1 * Math.max(0, (1 - totalTime/maxTime));
		bg.g = 1 * Math.max(0, (1 - totalTime/maxTime));
		bg.b = 1 * Math.max(0, (1 - totalTime/maxTime));
		
		Gdx.gl.glClearColor(bg.r, bg.g, bg.b, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}
}
