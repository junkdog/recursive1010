package net.onedaybeard.recursiveten.system.debug;

import static net.onedaybeard.dominatrix.pool.Vector2Pool.free;
import static net.onedaybeard.dominatrix.pool.Vector2Pool.vector2;
import lombok.ArtemisSystem;
import lombok.Getter;
import lombok.Profile;
import net.onedaybeard.recursiveten.Director;
import net.onedaybeard.recursiveten.artemis.TimerIntervalEntityProcessingSystem;
import net.onedaybeard.recursiveten.component.Position;
import net.onedaybeard.recursiveten.component.Size;
import net.onedaybeard.recursiveten.event.CommandEvent;
import net.onedaybeard.recursiveten.event.CommandEvent.Type;
import net.onedaybeard.recursiveten.profile.Profiler;
import net.onedaybeard.recursiveten.util.UnitCoordinates;

import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

@Profile(using=Profiler.class, enabled=Profiler.ENABLED)
@ArtemisSystem(
	requires={Position.class, Size.class},
	managers=TagManager.class)
public class InputHoverSystem extends TimerIntervalEntityProcessingSystem
{
	private Vector2 coordinate;

	private UnitCoordinates coordinates;
	private Entity hovered;
	
	@Getter private final InputProcessor inputProcessor;

	public InputHoverSystem(OrthographicCamera camera)
	{
		super(null, .1f);

		coordinates = new UnitCoordinates(camera);
		inputProcessor = new MouseClickListener();
	}

	@Override
	protected void begin()
	{
		hovered = null;
		coordinate = coordinates.coordinateAtPixel(Gdx.input.getX(), Gdx.input.getY());
	}
	
	@Override
	protected void process(Entity e)
	{
		Size size = sizeMapper.get(e);
		float radius = Math.max(size.width, size.height) / 2;
		
		float distanceToCursor = getDistanceToCursor(e);
		if (distanceToCursor < radius
			&& (hovered == null || distanceToCursor < getDistanceToCursor(hovered)))
		{
			hovered = e;
		}
	}

	private float getDistanceToCursor(Entity e)
	{
		Vector2 pos = vector2(positionMapper.get(e).pos);
		float len = pos.sub(coordinate).len();
		free(pos);
		
		return len;
	}
	
	@Override
	protected void end()
	{
		if (hovered != null)
			Director.instance.send(Type.ENTITY_HOVERED, hovered.getId());
		else
			Director.instance.send(Type.NO_HOVERED_ENTITY);
	}
	
	private class MouseClickListener implements InputProcessor 
	{
		@Override
		public boolean keyDown(int keycode)
		{
			return false;
		}

		@Override
		public boolean keyUp(int keycode)
		{
			return false;
		}

		@Override
		public boolean keyTyped(char character)
		{
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button)
		{
			if (button != 0 || hovered == null)
				return false;
			
			Director.instance.send(CommandEvent.Type.ENTITY_SELECTED, hovered.getId());
			return true;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button)
		{
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer)
		{
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY)
		{
			return false;
		}

		@Override
		public boolean scrolled(int amount)
		{
			return false;
		}
	}
}
