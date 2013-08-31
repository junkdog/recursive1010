package net.onedaybeard.recursiveten.component;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.artemis.Component;

@ToString @EqualsAndHashCode(callSuper=false)
public final class Cullable extends Component
{
	public boolean culled;
}
