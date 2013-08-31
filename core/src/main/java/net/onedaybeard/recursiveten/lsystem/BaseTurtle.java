package net.onedaybeard.recursiveten.lsystem;

import lombok.ToString;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class BaseTurtle
{

	private final Array<TurtleData> data;
	private int index;
	
	protected abstract void forward(float amount, boolean draw);

	public BaseTurtle()
	{
		 data = new Array<TurtleData>(true, 32);
		 data.add(new TurtleData());
	}
	

	public void drawForward(float amount)
	{
		forward(amount, true);
	}

	public void rotate(float amount)
	{
		data.get(index).angle += amount;
	}

	public void moveForward(float amount)
	{
		forward(amount, false);
	}

	public void push()
	{
		data.add(new TurtleData(data.get(index)));
		index++;
	}
	
	public void pop()
	{
		data.removeIndex(index);
		index--;
	}
	
	public void reset()
	{
		data.clear();
		data.add(new TurtleData());
	}
	
	@ToString
	protected static class TurtleData
	{
		public Vector2 position = new Vector2();
		public float angle = 90;
		
		public TurtleData() {}
		
		public TurtleData(TurtleData other)
		{
			position.set(other.position);
			angle = other.angle;
		}
	}

	public void setPosition(Vector2 pos)
	{
		data.get(index).position.set(pos);
	}
	
	protected TurtleData getTurtle()
	{
		return data.get(index);
	}
}
