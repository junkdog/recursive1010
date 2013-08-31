package net.onedaybeard.recursiveten.util;

import com.badlogic.gdx.utils.Pool;

public final class StringBuilderPool
{
	private static Factory factory = new Factory();
	
	private StringBuilderPool()
	{
		
	}
	
	public static StringBuilder stringBuilder()
	{
		return factory.obtain();
	}
	
	public static StringBuilder stringBuilder(String s)
	{
		return factory.obtain().append(s);
	}
	
	public static void free(StringBuilder sb)
	{
		factory.free(sb);
	}
	
	private static class Factory extends Pool<StringBuilder>
	{
		@Override
		protected StringBuilder newObject()
		{
			return new StringBuilder();
		}

		@Override
		public void free(StringBuilder object)
		{
			object.setLength(0);
			super.free(object);
		}
	}
}
