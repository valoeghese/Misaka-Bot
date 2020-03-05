package tk.valoeghese.misakabot.rpg.character;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntLists;
import tk.valoeghese.misakabot.MisakaBot;
import tk.valoeghese.misakabot.rpg.Esper;
import tk.valoeghese.misakabot.rpg.ability.EsperAbility;
import tk.valoeghese.misakabot.rpg.world.Location;
import tk.valoeghese.misakabot.rpg.world.setting.Setting;
import tk.valoeghese.misakabot.util.FloatRandom;
import tk.valoeghese.misakabot.util.RandomUtils;

public class UserCharacter extends AbstractCharacter implements Esper {
	private UserCharacter(Builder builder) {
		super(builder.name, builder.gender);
		this.potentialAbility = builder.potentialAbility;
		this.xp = builder.xp;
		this.userScores = builder.scores;
		this.perception = builder.perception;
		this.setting = builder.setting;
		this.location = builder.location;
		this.calculateLevel();

		if (builder.ability == null) {
			this.ability = this.abilityLevel == 0 ? EsperAbility.LEVEL_0 : EsperAbility.random();
		} else {
			this.ability = builder.ability;
		}
	}

	public final float potentialAbility;
	private EsperAbility ability;
	public int perception;

	private final int[] userScores; // stats = {SOCIAL, ATTACK, DEFENSE, SWIFTNESS}

	public float xp = 0.0f; // xp for calculations
	private int abilityLevel; // from 0 to 5

	public void calculateLevel() { // https://www.desmos.com/calculator/amkiawswds
		this.abilityLevel = (int) Math.floor((3 * this.potentialAbility * Math.log10(this.xp + 1.0f)) + (2 * this.potentialAbility));

		if (this.abilityLevel > 5) {
			this.abilityLevel = 5;
		}
	}

	public int getSocial() {
		return this.userScores[0];
	}

	public int getAttack() {
		return this.userScores[1];
	}

	public int getDefense() {
		return this.userScores[2];
	}

	public int getSwiftness() {
		return this.userScores[3];
	}

	public String describeWorldMessage() {
		return this.location.getDescription(this.perception);
	}

	@Override
	public EsperAbility getEsperAbility() {
		return this.ability;
	}

	@Override
	public int getLevel() {
		return this.abilityLevel;
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
		private int[] scores = null;
		private int perception;
		private Setting setting = null;
		private Location location = null;

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
		
		public Builder scores(int social, int atk, int def, int swiftness) { // used in loading data
			this.scores = new int[]{social, atk, def, swiftness};
			return this;
		}

		public Builder perception(int perception) { // used in loading data
			this.perception = perception;
			return this;
		}

		public Builder location(int setting, int location) { // used in loading data
			this.setting = MisakaBot.getWorld().getSetting(setting);
			this.location = this.setting.getLocation(location);
			return this;
		}

		public UserCharacter build() {
			if (this.potentialAbility == -1) {
				this.potentialAbility = 0.2f + ABILITY_RANDOM.nextFloat();
			}

			if (this.scores == null) {
				this.scores = IntLists.shuffle(new IntArrayList(new int[]{8, 7, 6, 4}), RandomUtils.RAND).toArray(new int[4]);
			}

			if (this.setting == null) {
				this.setting = MisakaBot.getWorld().getStartSetting();
			}

			if (this.location == null) {
				this.location = this.setting.getStartLocation();
			}

			return new UserCharacter(this);
		}
	}
}
