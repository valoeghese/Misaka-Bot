package tk.valoeghese.misakabot.interaction;

import java.util.function.Consumer;

public final class ServerChannel {
	public ServerChannel(long id, Consumer<C2SMessage> messagePoster, Consumer<String> messagePosterRaw) {
		this.messagePoster = messagePoster;
		this.messagePosterRaw = messagePosterRaw;
		this.id = id;
	}

	private final Consumer<C2SMessage> messagePoster;
	private final Consumer<String> messagePosterRaw;
	private final long id;

	public void sendMessage(C2SMessage message) {
		this.messagePoster.accept(message);
	}

	public void sendMessage(String message) {
		this.messagePosterRaw.accept(message);
	}

	public long id() {
		return this.id;
	}
}
