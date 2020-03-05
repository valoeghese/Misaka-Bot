package tk.valoeghese.misakabot.rpg.world.setting;

import tk.valoeghese.misakabot.rpg.world.Location;

public final class TestSetting extends Setting implements Location {
	public TestSetting(String name) {
		super(name);
		this.addLocation(this);
	}

	@Override
	public Setting[] getSettingsForTravel() {
		return new Setting[0];
	}

	@Override
	public String getDescription(int perception) {
		return "You are in a location which looks suspiciously like it was just created for testing the RPG. You shouldn't even be here";
	}

	@Override
	public int index() {
		return 0;
	}

	@Override
	public Location getLocation(int location) {
		// TODO Auto-generated method stub
		return null;
	}
}