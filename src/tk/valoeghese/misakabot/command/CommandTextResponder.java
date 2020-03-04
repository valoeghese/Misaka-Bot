package tk.valoeghese.misakabot.command;

import java.util.function.UnaryOperator;

import tk.valoeghese.misakabot.interaction.C2SMessage;
import tk.valoeghese.misakabot.interaction.ServerChannel;
import tk.valoeghese.misakabot.interaction.ServerGuild;
import tk.valoeghese.misakabot.interaction.ServerMember;

@FunctionalInterface
public interface CommandTextResponder extends CommandResponder {
	String getString(UnaryOperator<String> argGetter, ServerMember sender, ServerGuild server);

	@Override
	default C2SMessage get(UnaryOperator<String> argGetter, ServerMember sender, ServerGuild server, ServerChannel channel) {
		return C2SMessage.createTextMessage(() -> getString(argGetter, sender, server));
	}
}
