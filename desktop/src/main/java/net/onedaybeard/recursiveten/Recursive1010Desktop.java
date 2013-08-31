package net.onedaybeard.recursiveten;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Recursive1010Desktop
{
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.width = 900;
		config.height = 600;
		new LwjglApplication(new Recursive1010(), config);
	}
}
