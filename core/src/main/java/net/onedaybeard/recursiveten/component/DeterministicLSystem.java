package net.onedaybeard.recursiveten.component;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import com.artemis.Component;

@NoArgsConstructor @AllArgsConstructor
public final class DeterministicLSystem extends Component
{
	public String axiom;
	public String[] productions;
	public int iteration = 0;
	public boolean requestUpdate = true;
	
	public transient char[] result;

	@Override
	public String toString()
	{
		return "DeterministicLSystem(axiom=" + axiom + ", productions=" + Arrays.toString(productions) +
			", iteration=" + iteration + ", requestUpdate=" + requestUpdate + ", result.length=" + result.length + ")";
	}
	
	
}
