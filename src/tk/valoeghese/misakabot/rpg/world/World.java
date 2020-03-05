package tk.valoeghese.misakabot.rpg.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.valoeghese.misakabot.rpg.world.setting.Setting;

public final class World {
	public World(Builder builder) {
		this.settings = builder.settings.toArray(new Setting[0]);
		this.starter = builder.initialSetting;
		this.indices = builder.indices;
	}

	private final Setting[] settings;
	private final Map<String, Integer> indices;
	private final Setting starter;

	public Setting getStartSetting() {
		return this.starter;
	}

	public Setting getSetting(int index) {
		return this.settings[index];
	}

	public int indexOf(String setting) {
		return indices.get(setting);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder { // world builder
		private Builder() {
		}

		private List<Setting> settings = new ArrayList<>();
		private final Map<String, Integer> indices = new HashMap<>();
		private int e = 0;
		private Setting initialSetting;

		public Builder addSetting(Setting setting) {
			this.settings.add(setting);
			this.indices.put(setting.getName(), e++);
			return this;
		}

		public Builder initialSetting(Setting setting) {
			this.settings.add(setting);
			this.initialSetting = setting;
			this.indices.put(setting.getName(), e++);
			return this;
		}

		public World build() {
			return new World(this);
		}
	}
}
