package tk.valoeghese.misakabot.command;

import java.util.function.UnaryOperator;

import tk.valoeghese.misakabot.interaction.C2SMessage;
import tk.valoeghese.misakabot.interaction.Embed;
import tk.valoeghese.misakabot.interaction.ServerChannel;
import tk.valoeghese.misakabot.interaction.ServerGuild;
import tk.valoeghese.misakabot.interaction.ServerMember;

@FunctionalInterface
public interface CommandEmbedResponder extends CommandResponder {
	Embed getEmbed(UnaryOperator<String> argGetter, ServerMember sender, ServerGuild server);

	@Override
	default C2SMessage get(UnaryOperator<String> argGetter, ServerMember sender, ServerGuild server, ServerChannel channel) {
		return getEmbed(argGetter, sender, server).wrap();
	}
}
