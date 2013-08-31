package net.onedaybeard.recursiveten.system.spatial;

import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.component.Position;
import net.onedaybeard.recursiveten.component.Renderable;
import net.onedaybeard.recursiveten.profile.Profiler;

import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires={Position.class, Renderable.class})
public final class SpritePositionUpdateSystem extends EntityProcessingSystem
{
	public SpritePositionUpdateSystem()
	{
		super(null);
	}

	@Override
	protected void process(Entity e)
	{
		Renderable renderable = renderableMapper.get(e);
		Vector2 position = positionMapper.get(e).pos;

		Sprite s = renderable.sprite;
		s.setPosition((position.x - s.getOriginX()), (position.y - s.getOriginY()));
	}
}
