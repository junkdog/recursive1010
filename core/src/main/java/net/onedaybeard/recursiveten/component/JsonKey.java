package net.onedaybeard.recursiveten.component;

import net.onedaybeard.dominatrix.artemis.JsonId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.artemis.Component;

@ToString @EqualsAndHashCode(callSuper=false) @NoArgsConstructor @AllArgsConstructor
public final class JsonKey extends Component implements JsonId
{
	private String name;

	public String name()
	{
		return name;
	}

	@Override
	public Component name(String name)
	{
		this.name = name;
		return this;
	}
}
