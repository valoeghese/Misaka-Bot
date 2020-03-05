package tk.valoeghese.misakabot.rpg.character;

import tk.valoeghese.misakabot.rpg.world.Location;
import tk.valoeghese.misakabot.rpg.world.setting.Setting;
import tk.valoeghese.misakabot.util.TrackedInfo;

public abstract class AbstractCharacter {
	protected AbstractCharacter(String name, Gender gender) {
		this.name = name;
		this.gender = gender;
	}

	protected String name;
	protected Gender gender;
	public Setting setting;
	public Location location;

	public String getName() {
		return this.name;
	}

	public Gender getGender() {
		return this.gender;
	}

	public void serialize(TrackedInfo info) {
		info.put("characterName", this.name);
		info.put("characterGender", this.gender);
	}

	public void deserialize(TrackedInfo info) {
		this.name = info.getString("characterName");
		this.gender = info.getEnum("characterGender", Gender.values());
	}
}
