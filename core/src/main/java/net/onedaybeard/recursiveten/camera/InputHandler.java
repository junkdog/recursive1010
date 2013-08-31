package net.onedaybeard.recursiveten.camera;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public final class InputHandler implements InputProcessor
{
	private final Vector2 lastPosition;
	@Getter private final CameraController cameraController;
	
	private final Map<Integer, CameraController.State> keys;
	
	private int numberOfFingers = 0;
	private int fingerOnePointer;
	private int fingerTwoPointer;
	private float lastPinchDistance;
	private Vector2 fingerOne = new Vector2();
	private Vector2 fingerTwo = new Vector2();
	private boolean isPinchZooming = false;
	
	public InputHandler(CameraController cameraController)
	{
		this.lastPosition = new Vector2();
		this.cameraController = cameraController;
		
		keys = new HashMap<Integer, CameraController.State>();
		keys.put(Keys.W, CameraController.State.MOVE_UP);
		keys.put(Keys.S, CameraController.State.MOVE_DOWN);
		keys.put(Keys.A, CameraController.State.MOVE_LEFT);
		keys.put(Keys.D, CameraController.State.MOVE_RIGHT);
		keys.put(Keys.E, CameraController.State.ZOOM_OUT);
		keys.put(Keys.Q, CameraController.State.ZOOM_IN);
		keys.put(Keys.SHIFT_LEFT, CameraController.State.FAST_CONTROLS);
		keys.put(Keys.SHIFT_RIGHT, CameraController.State.FAST_CONTROLS);
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		boolean consumed = true;
		
		if (keys.containsKey(keycode))
			cameraController.addState(keys.get(keycode));
		else if (Keys.ESCAPE == keycode)
			Gdx.app.exit();
		else
			consumed = false;
		
		return consumed;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		boolean consumed = keys.containsKey(keycode);
		if (consumed)
			cameraController.removeState(keys.get(keycode));
		
		return consumed;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		numberOfFingers++;
		
		if (numberOfFingers == 1)
		{
			fingerOnePointer = pointer;
			fingerOne.set(x, y);
			
		}
		else if (numberOfFingers == 2)
		{
			Vector2 tmpVector = new Vector2(x, y);
			
//			fingerTwoPointer = pointer;
//			fingerTwo.set(x, y);
//			fingerTwoPointer = pointer;
//			fingerTwo.set(x, y);
			tmpVector.set(x, y);
			if (tmpVector.dst(fingerOne) > tmpVector.dst(fingerTwo))
			{
				fingerTwo = tmpVector;
				fingerTwoPointer = pointer;
			}
			else
			{
				fingerOne = tmpVector;
				fingerOnePointer = pointer;
			}
				
			
			lastPinchDistance = fingerOne.dst(fingerTwo);
			isPinchZooming = true;
		}
		
		if (!isPinchZooming)
		{
			lastPosition.x = x;
			lastPosition.y = y;
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		numberOfFingers--;
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer)
	{
		if (pointer == fingerOnePointer)
			fingerOne.set(x, y);
		else if (pointer  == fingerTwoPointer)
			fingerTwo.set(x, y);
		
		if (numberOfFingers == 2)
		{
			float distance =  fingerOne.dst(fingerTwo);
			cameraController.zoom(lastPinchDistance / distance);
			lastPinchDistance = distance;
			
			return true;
		}
		
		// dpm't want to throw the panning around
		if (isPinchZooming && numberOfFingers == 1) // removed 2nd finger
		{
			lastPosition.set(x, y);
			isPinchZooming = false;
		}
		
		int displaceX = (int)(lastPosition.x) - x;
		int displaceY = (int)(lastPosition.y) - y;
		
		lastPosition.x = x;
		lastPosition.y = y;

		cameraController.touchMove(displaceX, -displaceY);
		return true;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}
}