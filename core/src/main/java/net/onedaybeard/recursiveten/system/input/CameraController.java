package net.onedaybeard.recursiveten.system.input;

import java.util.EnumSet;

import com.badlogic.gdx.graphics.OrthographicCamera;

public final class CameraController
{
	public static enum State {ZOOM_IN, ZOOM_OUT, MOVE_RIGHT, MOVE_LEFT, MOVE_UP, MOVE_DOWN, FAST_CONTROLS}
	
	private static final float MOVE_AMOUNT = 150f;
	private static final float ZOOM_AMOUNT = .02f;

	private final EnumSet<State> stateSet;
	final OrthographicCamera camera;
	
	private boolean scheduledTouchMove = false;
	private float fastControlMultiplier;
	
	public CameraController(OrthographicCamera camera)
	{
		this.camera = camera;
		stateSet = EnumSet.noneOf(State.class);
	}
	
	public void addState(State state)
	{
		stateSet.add(state);
	}
	
	public void removeState(State state)
	{
		stateSet.remove(state);
	}
	
	public void touchMove(float x, float y)
	{
		camera.position.x += x * camera.zoom;
		camera.position.y += y * camera.zoom;
		
		scheduledTouchMove = true;
	}
	
	public void update(float delta)
	{
		if ((stateSet.isEmpty() || (stateSet.size() == 1 && stateSet.contains(State.FAST_CONTROLS)))
			&& !scheduledTouchMove)
			return;
		
		fastControlMultiplier = (stateSet.contains(State.FAST_CONTROLS)) ? 3f : 1f;
		
		if (stateSet.contains(State.MOVE_UP))
			camera.position.y += MOVE_AMOUNT * delta * fastControlMultiplier;
		else if (stateSet.contains(State.MOVE_DOWN))
			camera.position.y -= MOVE_AMOUNT * delta * fastControlMultiplier;
		
		if (stateSet.contains(State.MOVE_LEFT))
			camera.position.x -= MOVE_AMOUNT * delta * fastControlMultiplier;
		else if (stateSet.contains(State.MOVE_RIGHT))
			camera.position.x += MOVE_AMOUNT * delta * fastControlMultiplier;
		
		if (stateSet.contains(State.ZOOM_IN))
			camera.zoom = normalizeZoom(camera.zoom + ZOOM_AMOUNT * fastControlMultiplier);
		else if (stateSet.contains(State.ZOOM_OUT))
			camera.zoom = normalizeZoom(camera.zoom - ZOOM_AMOUNT * fastControlMultiplier);
		
		scheduledTouchMove = false; 
		camera.update();
	}

	public void zoom(float factor)
	{
		camera.zoom = normalizeZoom(camera.zoom * factor);
		scheduledTouchMove = true;
	}
	
	private float normalizeZoom(float zoomFactor)
	{
		if (zoomFactor > 5f)
			zoomFactor = 5f;
		
		if (zoomFactor < .5f)
			zoomFactor = .5f;
		
		return zoomFactor;
	}
}