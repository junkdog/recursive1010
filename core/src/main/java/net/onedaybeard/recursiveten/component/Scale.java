package net.onedaybeard.recursiveten.component;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.artemis.Component;

@ToString @EqualsAndHashCode(callSuper=false)
public class Scale extends Component
{
	public float value = 1;
}
