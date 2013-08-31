package net.onedaybeard.recursiveten;

import net.onedaybeard.recursiveten.screen.RecursiveGameScreen;

import com.badlogic.gdx.ApplicationListener;

public class Recursive1010 implements ApplicationListener
{
	public static final boolean PROFILE = true;

	@Override
	public void create()
	{
		Director director = Director.instance;
		director.setScreen(new RecursiveGameScreen());
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
