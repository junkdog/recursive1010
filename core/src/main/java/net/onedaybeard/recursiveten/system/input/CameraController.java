package net.onedaybeard.recursiveten.system.input;

import net.onedaybeard.recursiveten.util.UnitCoordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CameraController
{
	private final OrthographicCamera camera;
	private final UnitCoordinates unitCoordinates;
	
	private float zoomBy;

	CameraController(OrthographicCamera camera)
	{
		this.camera = camera;
		unitCoordinates = new UnitCoordinates(camera);
	}
	
	void moveCamera(Vector2 displacement)
	{
		Vector2 units = unitCoordinates.screenCoordinateAtPixel(displacement);
		camera.position.add(units.x, -units.y, 0);
		camera.update();
	}
	
	public void zoom(int amount)
	{
		zoomBy += amount * 0.1f * camera.zoom;
	}
	
	void update()
	{
		if (zoomBy == 0)
			return;
		
		Vector2 coordinate = unitCoordinates.coordinateAtPixel(Gdx.input.getX(), Gdx.input.getY());
		Vector2 cursorPercent = getScreenCursorPercent();
		
		camera.zoom += zoomBy;
		zoomBy = 0;
		
		Vector2 dimensions =  new Vector2(camera.viewportWidth * camera.zoom,
			camera.viewportHeight * camera.zoom);
		
		camera.position.x = coordinate.x + (dimensions.x / 2) - (cursorPercent.x * dimensions.x);
		camera.position.y = coordinate.y - (dimensions.y / 2) + (cursorPercent.y * dimensions.y);
		camera.update();
	}
	
	private static Vector2 getScreenCursorPercent()
	{
		Vector2 percent = new Vector2(Gdx.input.getX() / (float)Gdx.graphics.getWidth(),
			Gdx.input.getY() / (float)Gdx.graphics.getHeight());
		
		return percent;
	}
}
