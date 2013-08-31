package net.onedaybeard.recursiveten.lsystem;

import lombok.ToString;
import net.onedaybeard.recursiveten.util.StringBuilderPool;

import com.badlogic.gdx.utils.IntMap;

@ToString
public class LSystem
{
	private final String axiom; 
	private final IntMap<String> productions;
	
	public LSystem(String axiom)
	{
		this.axiom = axiom;
		productions = new IntMap<String>();
	}
	
	public LSystem addProduction(char symbol, String rule)
	{
		productions.put(symbol, rule);
		return this;
	}
	
	public LSystem addProduction(String symbol, String rule)
	{
		productions.put(symbol.charAt(0), rule);
		return this;
	}
	
	public LSystem addProduction(String production)
	{
		assert production.charAt(1) == '=';
		
		addProduction(production.charAt(0), production.substring(2));
		return this;
	}
	
	public String getIteration(int iteration)
	{
		StringBuilder sb = StringBuilderPool.stringBuilder(axiom);
		
		for (int i = 0; iteration > i; i++)
			apply(sb);
		
		String result = sb.toString();
		StringBuilderPool.free(sb);
		return result;
	}

	private void apply(StringBuilder sb)
	{
		char[] chars = sb.toString().toCharArray();
		sb.setLength(0);
		
		for (int i = 0; chars.length > i; i++)
			sb.append(productions.containsKey(chars[i]) ? productions.get(chars[i]) : chars[i]);
	}
}
