package tk.valoeghese.misakabot.rpg.character;

import java.util.Locale;

public enum Gender {
	MALE("He", "Him", "His", "His"),
	FEMALE("She", "Her", "Hers", "Her"),
	OTHER("They", "Them", "Theirs", "Their");

	private Gender(String subjPron, String objPron, String genPron, String posAdj) {
		this.subjPron = subjPron;
		this.objPron = objPron;
		this.genPron = genPron;
		this.posAdj = posAdj;
		this.subjPronLower = subjPron.toLowerCase(Locale.ROOT);
		this.objPronLower = objPron.toLowerCase(Locale.ROOT);
		this.genPronLower = genPron.toLowerCase(Locale.ROOT);
		this.posAdjLower = posAdj.toLowerCase(Locale.ROOT);
	}

	public final String subjPron, objPron, genPron, posAdj;
	public final String subjPronLower, objPronLower, genPronLower, posAdjLower;

	public static Gender ofEntered(String msgDisplay) {
		msgDisplay = msgDisplay.toUpperCase();

		if (msgDisplay.equals("M") || msgDisplay.equals("MALE")) {
			return MALE;
		} else if (msgDisplay.equals("F") || msgDisplay.equals("FEMALE")) {
			return FEMALE;
		} else if (msgDisplay.equals("O") || msgDisplay.equals("OTHER")) {
			return OTHER;
		} else {
			return null;
		}
	}
}
