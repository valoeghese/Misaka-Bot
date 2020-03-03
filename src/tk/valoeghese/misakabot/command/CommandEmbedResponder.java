package tk.valoeghese.misakabot.command;

import java.util.function.UnaryOperator;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import tk.valoeghese.misakabot.interaction.C2SMessage;

@FunctionalInterface
public interface CommandEmbedResponder extends CommandResponder {
	MessageEmbed getEmbed(UnaryOperator<String> argGetter, User sender, Guild server);

	@Override
	default C2SMessage get(UnaryOperator<String> argGetter, User sender, Guild server, MessageChannel channel) {
		return C2SMessage.createEmbedMessage(() -> getEmbed(argGetter, sender, server));
	}
}
