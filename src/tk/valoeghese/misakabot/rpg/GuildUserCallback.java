package tk.valoeghese.misakabot.rpg;

@FunctionalInterface
public interface GuildUserCallback {
	void accept(long id, UserTrackedInfo info);
}
