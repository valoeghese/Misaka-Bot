package tk.valoeghese.misakabot.rpg.character.npc;

import tk.valoeghese.misakabot.rpg.Esper;
import tk.valoeghese.misakabot.rpg.ability.EsperAbility;
import tk.valoeghese.misakabot.rpg.character.Gender;

public final class MisakaMikoto extends AbstractNPC implements Esper {
	public MisakaMikoto(int id) {
		// Most users will be from countries where the given name comes before the family name
		super(id, "Mikoto Misaka", Gender.FEMALE);
	}

	@Override
	public EsperAbility getEsperAbility() {
		return EsperAbility.ELECTROMASTER;
	}

	@Override
	public int getLevel() {
		return 5;
	}
}
