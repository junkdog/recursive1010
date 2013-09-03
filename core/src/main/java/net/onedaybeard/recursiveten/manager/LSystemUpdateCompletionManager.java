package net.onedaybeard.recursiveten.manager;

import lombok.ArtemisManager;
import net.onedaybeard.recursiveten.component.DeterministicLSystem;

import com.artemis.Entity;
import com.artemis.Manager;

@ArtemisManager(
	requires=DeterministicLSystem.class)
public class LSystemUpdateCompletionManager extends Manager
{
	@Override
	protected void initialize() {}

	@Override
	public void added(Entity e)
	{
		process(e);
	}
	
	@Override
	public void changed(Entity e)
	{
		process(e);
	}

	private void process(Entity e)
	{
		deterministicLSystemMapper.get(e).requestUpdate = false;
	}
}
