package net.onedaybeard.recursiveten.input;

import net.onedaybeard.recursiveten.Director;
import net.onedaybeard.recursiveten.event.CommandEvent;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class EntityHandler implements InputProcessor
{
	private final EntityController controller;

	private String currentArchetype;

	private int movingEntityId = -1;
	
	public EntityHandler(EntityController controller, Actor receiver)
	{
		this.controller = controller;
		
//		receiver.addListener(new CommandEventListener()
//		{
//			
//			@Override
//			protected boolean onReceive(CommandEvent event, Type type)
//			{
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			protected boolean isAccepting(CommandEvent event, Type type)
//			{
//				// TODO Auto-generated method stub
//				return super.isAccepting(event, type);
//			}
//		});
	}

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
		if (button != 0)
			return false;
		
		Entity e = controller.getEntityAt(screenX, screenY);
		if (e != null)
		{
			movingEntityId = e.getId();
			Gdx.input.setCursorCatched(true);
		}
//		else if (currentArchetype != null)
//			e = controller.addEntity(currentArchetype, screenX, screenY);
//		else
//			return false;
		
		if (e != null)
			Director.instance.send(CommandEvent.Type.ENTITY_SELECTED, e.getId());
		
		return e != null;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if (button == 0)
		{
			Gdx.input.setCursorCatched(false);
			Gdx.input.setCursorPosition(screenX, Gdx.graphics.getHeight() - screenY);
			movingEntityId = -1;
		}
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		if (movingEntityId != -1)
			controller.moveEntity(movingEntityId, Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
			
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		Entity e = controller.getEntityAt(screenX, screenY);
		int entityId = (e != null) ? e.getId() : -1;
		Director.instance.send(CommandEvent.Type.ENTITY_HOVERED, entityId);

		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}
