package net.onedaybeard.recursiveten.component;

import java.util.Arrays;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.Bloom.Settings;
import com.bitfire.postprocessing.effects.EffectSettings;
import com.bitfire.postprocessing.filters.Blur.BlurType;

@ToString @EqualsAndHashCode(callSuper=false)
public class Shader extends Component
{
	public EffectSettings settings;
	public transient PostProcessorEffect<?> effect;

	public Shader()
	{
	}
	
	public static void main(String[] args)
	{
		Settings s = new Bloom.Settings();
		s.baseIntensity = 2;
		s.baseSaturation = 3;
		s.bloomIntensity = 4;
		s.blurAmount = 5;
		s.blurPasses = 6;
		s.blurType = BlurType.Gaussian3x3b;
		s.bloomThreshold = 7;
		Json json = new Json(OutputType.json);
		String json2 = json.toJson(Arrays.asList(s, s));
		
		@SuppressWarnings("unchecked")
		Array<Settings> fromJson = json.fromJson(Array.class, json2);
		System.out.println(json2);
		System.out.println(json2.equals(json.toJson(fromJson)));
	}

}
