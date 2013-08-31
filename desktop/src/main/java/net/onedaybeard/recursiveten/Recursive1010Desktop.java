package net.onedaybeard.recursiveten;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Recursive1010Desktop
{
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.width = 1200;
		config.height = 800;
		new LwjglApplication(new Recursive1010(), config);
	}
}
