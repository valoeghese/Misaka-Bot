package tk.valoeghese.misakabot.rpg.save;

import tk.valoeghese.misakabot.rpg.character.npc.AbstractNPC;
import tk.valoeghese.misakabot.rpg.character.npc.NPCs;
import tk.valoeghese.misakabot.util.TrackedInfo;
import tk.valoeghese.sod.DataSection;

public final class NPCTrackedInfo extends TrackedInfo {
	NPCTrackedInfo(int uuid) {
		this.uuid = uuid;
		this.put("uuid", this.uuid);
	}

	private int uuid;
	private AbstractNPC character = null;

	public AbstractNPC getCharacter() {
		if (this.character == null) {
			// load non player character from data
			this.character = NPCs.get(this.uuid);
			this.character.deserialize(this);
		}

		return this.character;
	}

	public void setCharacter(AbstractNPC character) {
		this.character = character;
	}

	public int getUUID() {
		return this.uuid;
	}

	@Override
	public void readData(DataSection data) {
		super.readData(data);
		this.uuid = this.getInt("uuid");
	}

	@Override
	public void writeData(DataSection data) {
		this.character.serialize(this);
		super.writeData(data);
	}
}
