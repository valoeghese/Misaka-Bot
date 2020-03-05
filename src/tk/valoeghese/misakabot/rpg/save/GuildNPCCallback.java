package tk.valoeghese.misakabot.rpg.save;

@FunctionalInterface
public interface GuildNPCCallback {
	void accept(int id, NPCTrackedInfo info);
}
