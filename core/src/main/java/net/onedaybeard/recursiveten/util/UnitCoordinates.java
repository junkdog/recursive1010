package net.onedaybeard.recursiveten.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import net.onedaybeard.dominatrix.annotation.Sloppy;

@Sloppy("it's useful, but fucking suxxors - confuses the brogrammer in me")
public class UnitCoordinates
{
	private int screenWidth;
	private int screenHeight;
	private float width;
	private float height;
	private float ppuX; // pixels per unit
	private float ppuY;
	
	private Vector3 position;
	private Vector2 coordinates;
	private OrthographicCamera camera;
	
	public UnitCoordinates(OrthographicCamera camera)
	{
		this.camera = camera;
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		
		width = camera.viewportWidth;
		height = camera.viewportHeight;
		
		ppuX = screenWidth / width;
		ppuY = screenHeight / height;
		
		position = camera.position;
		coordinates = new Vector2();
	}

	@Deprecated
	public Vector2 asUnits(float screenX, float screenY)
	{
		coordinates.x = position.x + (screenX / ppuX) - (width / 2);
		coordinates.y = position.y - (screenY / ppuY) + (height / 2);
		return coordinates;
	}
	
	@Sloppy("this one is buggy... - doesn't use ppu") @Deprecated
	public Vector2 asScreenUnits(int screenX, int screenY)
	{
		coordinates.x = screenX / width;
		coordinates.y = screenY / height;
		return coordinates;
	}
	
	/**
	 * Converts pixel coordinates into screen units. 
	 * 
	 * @param screenXY Cursor coordinates in pixel space.
	 * @return SCreen units.
	 */
	public Vector2 screenCoordinateAtPixel(Vector2 screenXY)
	{
		return screenCoordinateAtPixel(screenXY.x, screenXY.y);
	}
	
	public Vector2 screenCoordinateAtPixel(float x, float y)
	{
		coordinates.x = x / ppuX;
		coordinates.y = y / ppuY;
		return coordinates.mul(camera.zoom);
	}
	
	public Vector2 coordinateAtPixel(Vector2 pixelXY)
	{
		return coordinateAtPixel(pixelXY.x, pixelXY.y);
	}
	
	public Vector2 coordinateAtPixel(float x, float y)
	{
		coordinates.x = position.x + (camera.zoom * x / ppuX) - (width * camera.zoom / 2 );
		coordinates.y = position.y - (camera.zoom * y / ppuX) + (height * camera.zoom / 2);
		return coordinates;
	}
}
