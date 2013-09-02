package net.onedaybeard.recursiveten.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.badlogic.gdx.scenes.scene2d.Event;

@Data @EqualsAndHashCode(callSuper=false)
public final class CommandEvent extends Event
{
	public static enum Type
	{
		ENTITY_SELECTED,
		ENTITY_HOVERED,
		NO_HOVERED_ENTITY,
		ENTITY_COPIED_TO_CLIPBOARD,
		CHANGE_ITERATION_COUNT;
	}

	private Type type;
	private int value;
	
	@Override
	public void reset()
	{
		type = null;
		value = 0;
		super.reset();
	}
	
	public void setValue(float value)
	{
		this.value = Float.floatToIntBits(value);
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	public int getIntValue()
	{
		return value;
	}
	
	public float getFloatValue()
	{
		return Float.intBitsToFloat(value);
	}
}
