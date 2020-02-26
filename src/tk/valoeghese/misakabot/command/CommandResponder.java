package tk.valoeghese.misakabot.command;

import java.util.function.UnaryOperator;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import tk.valoeghese.misakabot.util.discord.DiscordMessage;

@FunctionalInterface
public interface CommandResponder {
	DiscordMessage get(UnaryOperator<String> argGetter, User sender, Guild server, MessageChannel channel);
}
