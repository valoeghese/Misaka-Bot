package tk.valoeghese.misakabot.rpg.save;

@FunctionalInterface
public interface GuildUserCallback {
	void accept(long id, UserTrackedInfo info);
}
