package tk.valoeghese.misakabot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MisakaBot extends ListenerAdapter {
	public static void main(String[] args) throws LoginException {
		new JDABuilder(args[0])
				.addEventListeners(new MisakaBot())
				.build();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
	}
}
