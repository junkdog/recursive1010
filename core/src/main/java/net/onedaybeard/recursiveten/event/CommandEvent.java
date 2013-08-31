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
	private float value;
	
	@Override
	public void reset()
	{
		type = null;
		value = 0;
		super.reset();
	}
	
	public void setValue(float value)
	{
		this.value = value;
	}
	
	public void setValue(int value)
	{
		this.value = Float.intBitsToFloat(value);
	}
	
	public int getIntvalue()
	{
		return Float.floatToIntBits(value);
	}
}
