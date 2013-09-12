package net.onedaybeard.recursiveten;

import net.onedaybeard.recursiveten.screen.RecursiveEditorScreen;
import net.onedaybeard.recursiveten.screen.RecursiveGameScreen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Recursive1010Editor implements ApplicationListener
{
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.width = 1400;
		config.height = 900;
		config.resizable = false;
		new LwjglApplication(new Recursive1010Editor(), config);
	}

	@Override
	public void create()
	{
		Director director = Director.instance;
		director.setScreen(new RecursiveEditorScreen());
	}

	@Override
	public void dispose()
	{
		Assets.getAssetManager().dispose();
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void render()
	{
		Director.instance.update();
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void resize(int width, int height)
	{

	}
}
