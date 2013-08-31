package net.onedaybeard.recursiveten.component;

import java.util.HashMap;
import java.util.Map;

import net.onedaybeard.recursiveten.lsystem.LSystem;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.artemis.Component;
import com.badlogic.gdx.utils.IntMap;

@ToString @NoArgsConstructor @AllArgsConstructor
public final class DeterministicLSystem extends Component
{
	public String axiom;
	public String[] productions;
	public int iteration = 0;
	public boolean requestUpdate = true;
	
	public transient String result;
}
