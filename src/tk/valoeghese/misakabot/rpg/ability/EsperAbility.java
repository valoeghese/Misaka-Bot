package tk.valoeghese.misakabot.rpg.ability;

import tk.valoeghese.misakabot.util.RandomUtils;

public enum EsperAbility { // "norokyusha"
	LEVEL_0,       // esper without ability
	ELECTROMASTER,
	AEROKINESIS, // aero hand
	HYDROKINESIS, // hydro hand
	TELEKINESIS,
	TELEPORT,
	MOVE_POINT,
	AIM_STALKER,
	PYROKINESIS,
	TELEPATHY,
	FLOAT_DIAL,
	CLAIRVOYANCE,
	ELECTRON_BEAM ;// meltdowner

	private EsperAbility() {
		this.name = this.toString().replace('_', ' ');
	}

	private final String name;

	public String getName() {
		return this.name;
	}

	public static EsperAbility random() {
		EsperAbility[] values = EsperAbility.values();
		return values[RandomUtils.RAND.nextInt(values.length - 1) + 1];
	}
}
