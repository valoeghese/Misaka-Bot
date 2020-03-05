package tk.valoeghese.misakabot.rpg.save;

import tk.valoeghese.misakabot.MisakaBot;
import tk.valoeghese.misakabot.rpg.ability.EsperAbility;
import tk.valoeghese.misakabot.rpg.character.Gender;
import tk.valoeghese.misakabot.rpg.character.RPGUserStage;
import tk.valoeghese.misakabot.rpg.character.UserCharacter;
import tk.valoeghese.misakabot.util.TrackedInfo;
import tk.valoeghese.sod.DataSection;

public final class UserTrackedInfo extends TrackedInfo {
	UserTrackedInfo(long uuid) {
		this.uuid = uuid;
		this.put("uuid", this.uuid);
	}

	private long uuid;
	private UserCharacter character = null;

	public RPGUserStage getStageOrSetNotStarted() {
		RPGUserStage stage = this.getEnum("userStage", RPGUserStage.values());

		if (stage == null) {
			this.put("userStage", RPGUserStage.NOT_STARTED);
			return RPGUserStage.NOT_STARTED;
		}

		return stage;
	}

	public UserCharacter getCharacter() {
		if (this.character == null) {
			RPGUserStage stage = this.getEnum("userStage", RPGUserStage.values());

			if (stage == RPGUserStage.IN_GAME) { // load character from data
				this.character = UserCharacter.builder()
						.name(this.getString("characterName"))
						.gender(this.getEnum("characterGender", Gender.values()))
						.potentialAbility(this.getFloat("characterPotential"))
						.xp(this.getFloat("characterXp"))
						.ability(this.getEnum("characterAbility", EsperAbility.values()))
						.scores(this.getInt("social"), this.getInt("atk"), this.getInt("def"), this.getInt("speed"))
						.perception(this.getInt("sight"))
						.location(this.getInt("setting"), this.getInt("location"))
						.build();
			}
		}

		return this.character;
	}

	public void setCharacter(UserCharacter character) {
		this.character = character;
		this.put("characterPotential", character.potentialAbility);
	}

	public long getUUID() {
		return this.uuid;
	}

	@Override
	public void readData(DataSection data) {
		super.readData(data);
		this.uuid = this.getLong("uuid");
	}

	@Override
	public void writeData(DataSection data) {
		this.put("characterXp", this.character.xp);
		this.put("characterAbility", this.character.getEsperAbility());
		this.put("social", this.character.getSocial());
		this.put("atk", this.character.getAttack());
		this.put("def", this.character.getDefense());
		this.put("speed", this.character.getSwiftness());
		this.put("sight", this.character.perception);
		this.put("setting", this.character.setting.index(MisakaBot.getWorld()));
		this.put("location", this.character.location.index());
		super.writeData(data);
	}
}
