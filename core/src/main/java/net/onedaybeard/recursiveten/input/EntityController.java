package net.onedaybeard.recursiveten.input;

import net.onedaybeard.dominatrix.artemis.EntityFactoryManager;
import net.onedaybeard.recursiveten.component.JsonKey;
import net.onedaybeard.recursiveten.component.Position;
import net.onedaybeard.recursiveten.component.Renderable;
import net.onedaybeard.recursiveten.util.UnitCoordinates;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class EntityController
{
	private final UnitCoordinates unitCoordinates;
	
	private final World world;
	private final EntityFactoryManager entityFactory;
	private final GroupManager groupManager;

	private final ComponentMapper<Renderable> renderableMapper;
	private final ComponentMapper<Position> positionMapper;

	public EntityController(OrthographicCamera camera, World world)
	{
		this.world = world;
		unitCoordinates = new UnitCoordinates(camera);

		entityFactory = world.getManager(EntityFactoryManager.class);
		groupManager = world.getManager(GroupManager.class);
		
		renderableMapper = world.getMapper(Renderable.class);
		positionMapper = world.getMapper(Position.class);
	}
	
	Entity addEntity(String entityKey, float x, float y)
	{
		unitCoordinates.screenCoordinateAtPixel(x, y);
		
		Position position = new Position(unitCoordinates.coordinateAtPixel(x, y));
		Entity e = entityFactory.create(entityKey);
		e.addComponent(position);
		e.addComponent(new JsonKey(entityKey));
		
		groupManager.add(e, "level");
		
		return e;
	}
	
	Entity getEntityAt(float x, float y)
	{
		ImmutableBag<Entity> entities = groupManager.getEntities("level");
		Vector2 coordinates = unitCoordinates.coordinateAtPixel(x, y);
		return searchEntity(coordinates, entities);
	}
	
	void moveEntity(int entityId, int moveX, int moveY)
	{
		Entity e = world.getEntity(entityId);
		Position position = positionMapper.get(e);
		
		Vector2 displacement = unitCoordinates.screenCoordinateAtPixel(moveX, -moveY);
		position.pos.add(displacement);
	}
	
	private Entity searchEntity(Vector2 coord, ImmutableBag<Entity> entities)
	{
		Entity nearest = null;
		for (int i = 0, s = entities.size(); s > i; i++)
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
		
//		Vector2 distance = vector2(s.getX() + s.getWidth() / 2, s.getY() + s.getHeight() / 2);
//		float dst2 = distance.dst2(coord);
//		free(distance);
		
		return coord.dst2(s.getX() + s.getWidth() / 2, s.getY() + s.getHeight() / 2);
	}
}
