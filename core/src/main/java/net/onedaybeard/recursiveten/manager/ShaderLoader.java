package net.onedaybeard.recursiveten.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import lombok.ArtemisManager;
import lombok.Getter;
import net.onedaybeard.recursiveten.component.Shader;

import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.utils.IdentityMap;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.CameraMotion;
import com.bitfire.postprocessing.effects.CrtMonitor;
import com.bitfire.postprocessing.effects.Curvature;
import com.bitfire.postprocessing.effects.EffectSettings;
import com.bitfire.postprocessing.effects.Vignette;
import com.bitfire.postprocessing.effects.Zoomer;

@ArtemisManager(
	requires=Shader.class)
public class ShaderLoader extends Manager
{
	private final IdentityMap<Class<? extends EffectSettings>, Class<? extends PostProcessorEffect<?>>> effectsMap;
	@Getter private final PostProcessor postProcessor; 
	
	public ShaderLoader()
	{
		effectsMap = new IdentityMap<Class<? extends EffectSettings>,Class<? extends PostProcessorEffect<?>>>();
		effectsMap.put(Bloom.Settings.class, Bloom.class);
		effectsMap.put(CameraMotion.Settings.class, CameraMotion.class);
		effectsMap.put(CrtMonitor.Settings.class, CrtMonitor.class);
		effectsMap.put(Curvature.Settings.class, Curvature.class);
		effectsMap.put(Vignette.Settings.class, Vignette.class);
		effectsMap.put(Zoomer.Settings.class, Zoomer.class);
		
		postProcessor = new PostProcessor(false, false, (Gdx.app.getType() == ApplicationType.Desktop));
		com.bitfire.utils.ShaderLoader.BasePath = "shaders/";
	}
	
	@Override
	protected void initialize() {}

	@Override @SuppressWarnings("rawtypes")
	public void added(Entity e)
	{
		Shader shader = shaderMapper.get(e);
		Class<? extends PostProcessorEffect> klazz = effectsMap.get(shader.settings.getClass());
		try
		{
			Constructor<? extends PostProcessorEffect> constructor = klazz.getConstructor(shader.settings.getClass());
			shader.effect = constructor.newInstance(shader.settings);
			postProcessor.addEffect(shader.effect);
		}
		catch (NoSuchMethodException e1)
		{
			e1.printStackTrace();
		}
		catch (SecurityException e1)
		{
			e1.printStackTrace();
		}
		catch (InstantiationException e1)
		{
			e1.printStackTrace();
		}
		catch (IllegalAccessException e1)
		{
			e1.printStackTrace();
		}
		catch (IllegalArgumentException e1)
		{
			e1.printStackTrace();
		}
		catch (InvocationTargetException e1)
		{
			e1.printStackTrace();
		}
	}
	
	@Override
	public void deleted(Entity e)
	{
		Shader shader = shaderMapper.get(e);
		postProcessor.removeEffect(shader.effect);
		shader.effect.dispose();
	}
}
