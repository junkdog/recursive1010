package net.onedaybeard.recursiveten;

import static net.onedaybeard.dominatrix.util.Disposer.free;
import lombok.Getter;
import lombok.Setter;
import net.onedaybeard.recursiveten.event.CommandEvent;
import net.onedaybeard.recursiveten.system.event.EventSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;


public enum Director implements Disposable
{
	instance;
	
	@Getter	private final SpriteBatch spriteBatch;
	@Getter private Screen screen;
	
//	private AssetManager assetManager;
	@Setter @Getter private EventSystem eventSystem;

	private Array<Screen> screenStack;
	
	private Director()
	{
		spriteBatch = new SpriteBatch();
//		assetManager = new AssetManager(fileResolver);
//		assetManager = new AssetManager(new ExternalFileHandleResolver());
//		assetManager = new AssetManager(new InternalFileHandleResolver());
//		TextureMap.loadAssetManager(assetManager);
//		audioPlayer = SoundManager.audioPlayer;
		
		screenStack = new Array<Screen>(true, 3);
		
		Assets.loadAssetManager();
	}
	
	public synchronized void setScreen(Screen screen)
	{
		this.screen = screen;
		screen.resume();
	}
	
	public synchronized void pushScreen(Screen screen)
	{
		screenStack.add(getScreen());
		setScreen(screen);
	}
	
	public synchronized void popScreen()
	{
		if (screenStack.size > 0)
			setScreen(screenStack.pop());
	}

	public synchronized void setScreenAndDisposeCurrent(Screen screen)
	{
		this.screen.dispose();
		setScreen(screen);
	}
	
	public void update()
	{
		if (screen == null)
			throw new RuntimeException("No screen to direct.");
		
		screen.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose()
	{
		if (screen != null)
			free(screen);
		
		free(spriteBatch);
		free(Assets.getAssetManager());
	}
	
	public void send(Event event)
	{
		eventSystem.send(event);
	}
	
	public void send(CommandEvent.Type type)
	{
		eventSystem.send(type, 0);
	}
		
	public void send(CommandEvent.Type type, float value)
	{
		eventSystem.send(type, value);
	}
	
	public void send(CommandEvent.Type type, int value)
	{
		eventSystem.send(type, value);
	}
//	
//	public void send(DebugEvent.Type type, int value)
//	{
//		broadcasterSystem.send(type, value);
//	}
//	
//	public void send(HudEvent.Type type, float value)
//	{
//		broadcasterSystem.send(type, value);
//	}
//
//	public void send(GameOverEvent.Type type)
//	{
//		broadcasterSystem.send(type);
//	}
//	
//	public void send(SoundEvent.Type type, AudioResource sound)
//	{
//		if (true)
//			return;
//		
//		if (broadcasterSystem != null)
//			broadcasterSystem.send(type, sound);
//		else
//			audioPlayer.playSoundEvent(type, sound);
//	}
//	
//	public void addEventReceiver(Actor actor)
//	{
//		broadcasterSystem.addReceiver(actor);
//	}
}
