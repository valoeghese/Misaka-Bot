package tk.valoeghese.misakabot.rpg.character;

import tk.valoeghese.misakabot.rpg.ability.EsperAbility;
import tk.valoeghese.misakabot.util.FloatRandom;
import tk.valoeghese.misakabot.util.RandomUtils;

public class UserCharacter {
	private UserCharacter(Builder builder) {
		this.gender = builder.gender;
		this.name = builder.name;
		this.potentialAbility = builder.potentialAbility;
		this.xp = builder.xp;
		this.calculateLevel();

		if (builder.ability != null) {
			this.ability = builder.ability;
		} else {
			this.ability = this.abilityLevel == 0 ? EsperAbility.LEVEL_0 : EsperAbility.random();
		}
	}

	public final Gender gender;
	public final String name;
	public final float potentialAbility;
	public EsperAbility ability;
	public float xp = 0.0f; // xp for calculations
	public int abilityLevel;

	public void calculateLevel() { // https://www.desmos.com/calculator/amkiawswds
		this.abilityLevel = (int) Math.floor((3 * this.potentialAbility * Math.log10(this.xp + 1.0f)) + (2 * this.potentialAbility));
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
		private float potentialAbility = -1;
		private float xp = 0.0f;
		private EsperAbility ability = null;

		public Builder gender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder potentialAbility(float potentialAbility) { // used in loading data
			this.potentialAbility = potentialAbility;
			return this;
		}

		public Builder xp(float xp) { // used in loading data
			this.xp = xp;
			return this;
		}

		public Builder ability(EsperAbility ability) { // used in loading data
			this.ability = ability;
			return this;
		}

		public UserCharacter build() {
			if (this.potentialAbility == -1) {
				this.potentialAbility = 0.2f + skewDistribution(ABILITY_RANDOM.nextFloat());
			}

			return new UserCharacter(this);
		}
	}

	private static float skewDistribution(float f0to1) {
		return f0to1;
	}
}
