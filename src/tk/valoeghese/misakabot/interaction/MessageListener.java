package tk.valoeghese.misakabot.interaction;

@FunctionalInterface
public interface MessageListener {
	void onMessageReceived(RecieveMessageEvent e);
}
