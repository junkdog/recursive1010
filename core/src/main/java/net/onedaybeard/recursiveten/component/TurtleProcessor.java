package net.onedaybeard.recursiveten.component;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.onedaybeard.recursiveten.lsystem.TurtleCommand;

import com.artemis.Component;

@ToString @NoArgsConstructor @AllArgsConstructor
public class TurtleProcessor extends Component
{
	public CommandBinding[] commands = new CommandBinding[0];
	
	public float moveAmount;
	public float turnAmount;
	public float turnDeviation;
	
	public TurtleProcessor put(char key, TurtleCommand value)
	{
		// for easier json parsing...
		commands = Arrays.copyOf(commands, commands.length + 1);
		commands[commands.length - 1] = new CommandBinding(key, value);
		return this;
	}
	
	@AllArgsConstructor @NoArgsConstructor @ToString
	public static class CommandBinding
	{
		public char key;
		public TurtleCommand command;
	}

	@Override
	public String toString()
	{
		return "TurtleProcessor(commands:types=" + commands.length + ", moveAmount=" + moveAmount +
			", turnAmount=" + turnAmount + ", turnDeviation=" + turnDeviation + ")";
	}
	
	
}
