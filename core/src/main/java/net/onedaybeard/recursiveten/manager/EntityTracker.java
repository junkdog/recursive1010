package net.onedaybeard.recursiveten.manager;

import lombok.ArtemisManager;
import net.onedaybeard.recursiveten.component.Renderable;

import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

@ArtemisManager(
	requires={Renderable.class})
public class EntityTracker extends Manager
{
	private final Array<Entity> entities;
	
	public EntityTracker()
	{
		entities = new Array<Entity>();
	}

	@Override
	protected void initialize() {}
	

	@Override
	public void added(Entity e)
	{
		entities.add(e);
	}
	
	@Override
	public void changed(Entity e)
	{
		if (entities.contains(e, true))
			entities.add(e);
	}

	@Override
	public void deleted(Entity e)
	{
		assert entities.contains(e, true);
		entities.removeValue(e, true);
	}
	
	public Entity searchEntity(Vector2 coord)
	{
		Entity nearest = null;
		for (int i = 0, s = entities.size; s > i; i++)
		{
			Entity e = entities.get(i);
			if (!renderableMapper.has(e))
				continue;
			
			if (isHovered(coord, e) && distance(coord, e) < distance(coord, nearest))
				nearest = e;
		}
		
		return nearest;
	}
	
	private boolean isHovered(Vector2 coord, Entity e)
	{
		Sprite s = renderableMapper.get(e).sprite;
		return s.getBoundingRectangle().contains(coord.x, coord.y);
	}
	
	private float distance(Vector2 coord, Entity e)
	{
		if (e == null)
			return Float.MAX_VALUE;
		
		Sprite s = renderableMapper.get(e).sprite;
		
		return coord.dst2(
			s.getX() + (s.getWidth() / 2),
			s.getY() + (s.getHeight() / 2));
	}
}
