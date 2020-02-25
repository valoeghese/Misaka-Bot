package tk.valoeghese.misakabot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tk.valoeghese.misakabot.command.Command;
import tk.valoeghese.misakabot.rpg.GuildTrackedInfo;

public class MisakaBot extends ListenerAdapter {
	public static void main(String[] args) throws LoginException {
		Command.builder() // test command 1
			.args("arg0")
			.name("echo")
			.simpleCallback(cmdArgs -> cmdArgs.apply("arg0"))
			.build();
		
		Command.builder()
			.name("ping") // test command 2
			.simpleCallback(cmdArgs -> "pong")
			.build();

		Command.builder()
			.name("character")
			.build();

		new JDABuilder(args[0])
			.addEventListeners(new MisakaBot())
			.build();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Guild guild = event.getGuild();
		GuildTrackedInfo gti = GuildTrackedInfo.get(guild.getIdLong());
		String message = event.getMessage().getContentRaw();
		final String commandPrefix = gti.getCommandPrefix();

		if (message.startsWith(commandPrefix)) {
			String commandString = message.substring(commandPrefix.length());
			int commandFinalIndex = commandString.indexOf(' ');
			String commandName = commandString.substring(0, commandFinalIndex);
			Command command = Command.get(commandName);

			if (command != null) {
				event.getChannel().sendMessage(command.handle(commandString.substring(commandFinalIndex), commandPrefix));
			}
		}
	}
}
