package tk.valoeghese.misakabot.rpg.world;

import java.util.ArrayList;
import java.util.List;

public abstract class Setting {
	protected Setting() {
	}

	protected void addLocation(Location location) {
		this.locations.add(location);
	}

	public abstract Setting[] getSettingsForTravel();

	public Location getStartLocation() {
		return this.locations.get(0);
	}

	private final List<Location> locations = new ArrayList<>();
}
