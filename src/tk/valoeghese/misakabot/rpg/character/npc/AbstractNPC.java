package tk.valoeghese.misakabot.rpg.character.npc;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import tk.valoeghese.misakabot.rpg.character.AbstractCharacter;
import tk.valoeghese.misakabot.rpg.character.Gender;
import tk.valoeghese.misakabot.util.TrackedInfo;

public abstract class AbstractNPC extends AbstractCharacter {
	protected AbstractNPC(int id, String name, Gender gender) {
		super(name, gender);
		this.id = id;
	}

	private final int id;

	public int getId() {
		return this.id;
	}

	@Override
	public void serialize(TrackedInfo info) {
		info.put("name", this.name);
		info.put("gender", this.gender);
	}

	@Override
	public void deserialize(TrackedInfo info) {
		
	}

	static final Int2ObjectMap<AbstractNPC> REVERSE_ID_MAP = new Int2ObjectArrayMap<>();
}
