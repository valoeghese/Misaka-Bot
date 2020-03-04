package tk.valoeghese.misakabot.interaction;

public class ServerGuild {
	public ServerGuild(long id, long ownerId) {
		this.id = id;
		this.ownerId = ownerId;
	}

	private final long id, ownerId;

	public long id() {
		return this.id;
	}

	public long getOwnerId() {
		return this.ownerId;
	}
}
