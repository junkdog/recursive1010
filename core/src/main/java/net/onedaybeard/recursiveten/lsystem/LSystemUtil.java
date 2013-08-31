package net.onedaybeard.recursiveten.lsystem;

import net.onedaybeard.recursiveten.component.DeterministicLSystem;

public class LSystemUtil
{
	public static LSystem toLSystem(DeterministicLSystem ls)
	{
		LSystem lSystem  = new LSystem(ls.axiom);
		for (String production : ls.productions)
		{
			assert production.charAt(1) == '=';
			
			lSystem.addProduction(production.charAt(0), production.substring(2));
		}
		return lSystem;
	}
}
