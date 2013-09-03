package net.onedaybeard.recursiveten.lsystem;

import static net.onedaybeard.dominatrix.pool.Vector2Pool.free;
import static net.onedaybeard.dominatrix.pool.Vector2Pool.vector2;
import lombok.ToString;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

@ToString
public class PixmapTurtle extends BaseTurtle
{
	private Pixmap pixmap;
	private float originX;
	private float originY;

	public PixmapTurtle(float width, float height, float originX, float originY)
	{
		super();
		this.originX = originX * 2; // magic number?
//		this.originX = originX;
		this.originY = originY * 2;
		pixmap = new Pixmap((int)width, (int)height, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		getTurtle().position.set(originX, originY);
	}
	
	@Override
	protected void forward(float amount, boolean draw)
	{
		TurtleData td = getTurtle();
		
		Vector2 length = vector2(amount, 0).rotate(td.angle);
		Vector2 newPosition = vector2(td.position).add(length);

		Vector2 xy = td.position;
		
		if (draw) pixmap.drawLine(
			(int)(xy.x - originX),
			(int)(xy.y - originY),
			(int)(newPosition.x - originX),
			(int)(newPosition.y - originY));
		
		td.position.set(newPosition);
		
		free(newPosition);
		free(length);
	}
	
	public Sprite getSprite()
	{
		Texture texture = new Texture(pixmap);
		Sprite s = new Sprite(texture);
		s.flip(false, true);
		
		pixmap.dispose();
		return s;
	}
}
