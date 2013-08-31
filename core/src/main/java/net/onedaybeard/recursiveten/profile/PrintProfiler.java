package net.onedaybeard.recursiveten.profile;

import net.onedaybeard.agrotera.ArtemisProfiler;
import net.onedaybeard.dominatrix.util.SystemNTimer;

public class PrintProfiler implements ArtemisProfiler
{
	private SystemNTimer timer;
	
	@Override
	public void start()
	{
		timer.start();
	}

	@Override
	public void stop()
	{
		timer.stopAndPrintLog();
	}

	@Override
	public void setTag(Object tag)
	{
		if (tag instanceof Class<?>)
			timer = new SystemNTimer((Class<?>)tag);
		else
			timer = new SystemNTimer(null);
	}
}
