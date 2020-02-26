package tk.valoeghese.misakabot.rpg;

import tk.valoeghese.misakabot.rpg.ability.EsperAbility;
import tk.valoeghese.misakabot.rpg.character.Gender;
import tk.valoeghese.misakabot.rpg.character.RPGUserStage;
import tk.valoeghese.misakabot.rpg.character.UserCharacter;
import tk.valoeghese.misakabot.util.TrackedInfo;
import tk.valoeghese.sod.BinaryData;
import tk.valoeghese.sod.DataSection;

public final class UserTrackedInfo extends TrackedInfo {
	UserTrackedInfo(long uuid) {
		this.uuid = uuid;
	}

	private long uuid;
	private UserCharacter character = null;

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
						.build();
			}
		}

		return this.character;
	}

	public void setCharacter(UserCharacter character) {
		this.character = character;
	}

	public long getUUID() {
		return this.uuid;
	}

	@Override
	public void readData(BinaryData data) {
		super.readData(data);
		DataSection user = data.get("user");

		if (user != null) {
			this.uuid = user.readLong(0);
		}
	}

	@Override
	public void writeData(BinaryData data) {
		super.writeData(data);
		DataSection user = new DataSection();
		user.writeLong(this.uuid);
		data.put("user", user);
	}
}
