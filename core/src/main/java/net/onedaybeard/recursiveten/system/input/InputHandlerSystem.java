package net.onedaybeard.recursiveten.system.input;

import static net.onedaybeard.dominatrix.pool.Vector2Pool.free;
import static net.onedaybeard.dominatrix.pool.Vector2Pool.vector2;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class InputHandlerSystem extends VoidEntitySystem implements InputProcessor
{
	private final CameraController controller;
	private final Vector2 lastPosition;
	
	private boolean isDragging;

	public InputHandlerSystem(OrthographicCamera camera)
	{
		this.controller = new CameraController(camera);
		lastPosition = new Vector2();
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
		if (button != 1)
			return false;
		
		lastPosition.set(screenX, screenY);
		isDragging = true;
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if (button != 1)
			return false;
		
		isDragging = false;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		if (!isDragging)
			return false;
		
		Vector2 vector22 = vector2(lastPosition).sub(screenX, screenY);
		controller.moveCamera(vector22);
		free(vector22);
		
		lastPosition.set(screenX, screenY);
		
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		controller.zoom(amount);
		return false;
	}

	@Override
	protected void processSystem()
	{
		controller.update();
	}
}
