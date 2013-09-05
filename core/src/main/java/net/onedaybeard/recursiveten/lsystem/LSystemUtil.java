package net.onedaybeard.recursiveten.lsystem;

import net.onedaybeard.recursiveten.component.DeterministicLSystem;

public class LSystemUtil
{
	public static LSystem toLSystem(DeterministicLSystem ls)
	{
		LSystem lSystem  = new LSystem(ls.axiom);
		for (int i = 0, s = ls.productions.length; s > i; i++)
		{
			String production = ls.productions[i];
			assert production.charAt(1) == '=';
			
			if (production.length() > 2)
				lSystem.addProduction(production.charAt(0), production.substring(2));
		}
		return lSystem;
	}
}
