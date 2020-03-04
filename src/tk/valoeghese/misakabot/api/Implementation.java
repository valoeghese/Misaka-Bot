package tk.valoeghese.misakabot.api;

import tk.valoeghese.misakabot.interaction.MessageListener;

public interface Implementation {
	void subscribeOnReceiveMessage(MessageListener listener);
	void start();
	void shutdown();
	void setOnShutdown(Runnable onShutdown);
}
