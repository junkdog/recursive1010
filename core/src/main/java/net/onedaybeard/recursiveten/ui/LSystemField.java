package net.onedaybeard.recursiveten.ui;

import java.lang.reflect.Field;

import net.onedaybeard.recursiveten.component.DeterministicLSystem;
import net.onedaybeard.recursiveten.component.TurtleProcessor;

import com.artemis.Component;

public enum LSystemField implements TextfieldBinding
{
	AXIOM("axiom", DeterministicLSystem.class),
	ITERATIONS("iteration", DeterministicLSystem.class),
	PRODUCTIONS("productions", DeterministicLSystem.class),
	
	COMMANDS("commands", TurtleProcessor.class),
	TURN_AMOUNT("turn amount", "turnAmount", TurtleProcessor.class);
	
	private final String label;
	private final Field field;

	private LSystemField(String labelAndField, Class<? extends Component> declaringClass) 
	{
		this(labelAndField, labelAndField, declaringClass);
	}

	private LSystemField(String label, String field, Class<? extends Component> declaringClass)
	{
		this.label = label + ": ";
		this.field = getField(field, declaringClass);
	}

	private Field getField(String field, Class<? extends Component> declaringClass)
	{
		try
		{
			return declaringClass.getField(field);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String label()
	{
		return label;
	}

	@Override
	public Field field()
	{
		return field;
	}
}
