package tk.valoeghese.misakabot.rpg.character.npc;

public final class NPCs {
	private NPCs() {
	}

	public static AbstractNPC get(int id) {
		return AbstractNPC.REVERSE_ID_MAP.apply(id);
	}

	public static final AbstractNPC MISAKA_MIKOTO = new MisakaMikoto(0);
}
