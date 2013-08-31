package net.onedaybeard.recursiveten.html;

import net.onedaybeard.recursiveten.core.Recursive1010;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class Recursive1010Html extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new Recursive1010();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
