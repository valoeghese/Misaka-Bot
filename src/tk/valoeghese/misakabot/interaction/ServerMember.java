package tk.valoeghese.misakabot.interaction;

public final class ServerMember {
	public ServerMember(long id, boolean bot) {
		this.bot = bot;
		this.id = id;
	}

	private final boolean bot;
	private final long id;

	public boolean isBot() {
		return this.bot;
	}

	public long id() {
		return this.id;
	}
}
