package net.onedaybeard.recursiveten.system.render;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.component.Shader;
import net.onedaybeard.recursiveten.profile.Profiler;

import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires=Shader.class)
public final class ShaderUpdaterSystem extends EntityProcessingSystem
{
	public ShaderUpdaterSystem()
	{
		super(null);
	}

	@Override
	protected void process(Entity e)
	{
		shaderMapper.get(e).effect.refreshSettings();
	}
}
