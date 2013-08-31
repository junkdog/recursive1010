package net.onedaybeard.recursiveten.system.spatial;

import static net.onedaybeard.dominatrix.pool.Vector2Pool.free;
import static net.onedaybeard.dominatrix.pool.Vector2Pool.vector2;
import lombok.ArtemisSystem;
import lombok.Profile;
import net.onedaybeard.recursiveten.component.Position;
import net.onedaybeard.recursiveten.component.Velocity;
import net.onedaybeard.recursiveten.profile.Profiler;

import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires={Position.class, Velocity.class})
public final class PositionUpdateSystem extends EntityProcessingSystem
{
	public PositionUpdateSystem()
	{
		super(null);
	}
	
	@Override
	protected void process(Entity e)
	{
		Vector2 displace = vector2(velocityMapper.get(e).vector).scl(world.delta);
		positionMapper.get(e).pos.add(displace);
		free(displace);
	}
}
