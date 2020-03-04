package tk.valoeghese.misakabot.interaction;

public final class RecieveMessageEvent {
	public RecieveMessageEvent(ServerGuild guild, ServerChannel channel, S2CMessage message, ServerMember messageAuthor) {
		this.guild = guild;
		this.channel = channel;
		this.message = message;
		this.messageAuthor = messageAuthor;
	}

	private final ServerGuild guild;
	private final ServerChannel channel;
	private final S2CMessage message;
	private final ServerMember messageAuthor;

	public ServerGuild getGuild() {
		return this.guild;
	}

	public ServerChannel getChannel() {
		return this.channel;
	}

	public S2CMessage getMessage() {
		return this.message;
	}
	
	public ServerMember getMessageAuthor() {
		return this.messageAuthor;
	}
}
