package tk.valoeghese.misakabot.rpg.world.setting;

import java.util.ArrayList;
import java.util.List;

import tk.valoeghese.misakabot.rpg.world.Location;
import tk.valoeghese.misakabot.rpg.world.World;

public abstract class Setting {
	protected Setting(String name) {
		this.name = name;
	}

	protected final String name;
	private final List<Location> locations = new ArrayList<>();

	protected void addLocation(Location location) {
		this.locations.add(location);
	}

	public String getName() {
		return this.name;
	}

	public Location getStartLocation() {
		return this.locations.get(0);
	}

	public int index(World world) {
		return world.indexOf(this.name);
	}

	public abstract Setting[] getSettingsForTravel();
	public abstract Location getLocation(int location);
}
