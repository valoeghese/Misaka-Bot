package tk.valoeghese.misakabot.rpg.world;

import java.util.ArrayList;
import java.util.List;

public final class World {
	public World(Builder builder) {
		this.settings = builder.settings.toArray(new Setting[0]);
		this.starter = builder.initialSetting;
	}

	private final Setting[] settings;
	private final Setting starter;

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder { // world builder
		private Builder() {
		}

		private List<Setting> settings = new ArrayList<>();
		private Setting initialSetting;

		public Builder addSetting(Setting setting) {
			this.settings.add(setting);
			return this;
		}

		public Builder initialSetting(Setting setting) {
			this.settings.add(setting);
			this.initialSetting = setting;
			return this;
		}

		public World build() {
			return new World(this);
		}
	}
}
