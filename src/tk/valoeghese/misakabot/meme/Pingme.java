package tk.valoeghese.misakabot.meme;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class Pingme extends ListenerAdapter { // for @pingme in the cursed pomf discord
	private Pingme() {
	}

	public static void setupJDA(String botKey) {
		try {
			new JDABuilder(botKey)
			.addEventListeners(new Pingme())
			.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!this.started) {
			this.started = true;
			event.getChannel().sendMessage(PINGME).queue(this::sendAnother);
		}
	}

	private void sendAnother(Message message) {
		message.getChannel().sendMessage(PINGME).queue(this::sendAnother);
	}

	private boolean started = false;
	private static String PINGME = "<@&637898602336878594>";
}
