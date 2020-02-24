package tk.valoeghese.misakabot.rpg.character;

import tk.valoeghese.misakabot.rpg.ability.EsperAbility;
import tk.valoeghese.misakabot.util.FloatRandom;
import tk.valoeghese.misakabot.util.RandomUtils;

public class Character {
	private Character(Builder builder) {
		this.gender = builder.gender;
		this.name = builder.name;
		this.potentialAbility = builder.potentialAbility;
	}

	public final Gender gender;
	public final String name;
	public final float potentialAbility;
	public EsperAbility ability;
	public float xp = 1.0f; // xp for calculations
	public int abilityLevel;
	
	public void calculateLevel() {
		this.abilityLevel = (int) Math.floor(3 * this.potentialAbility + Math.log10(1.0f + this.xp) + (this.potentialAbility / 3.0f));
	}

	public static Builder builder() {
		return new Builder();
	}

	private static final FloatRandom ABILITY_RANDOM = RandomUtils.naturalDistribution(0.5f, 0.5f);

	public static class Builder {
		private Builder() {
		}

		private Gender gender;
		private String name;
		private float potentialAbility = 0.2f + ABILITY_RANDOM.nextFloat();

		public Builder gender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Character build() {
			return new Character(this);
		}
	}
}
