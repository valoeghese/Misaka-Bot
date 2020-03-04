package tk.valoeghese.misakabot.command;

import java.util.function.UnaryOperator;

import tk.valoeghese.misakabot.interaction.ServerGuild;
import tk.valoeghese.misakabot.interaction.ServerMember;

@FunctionalInterface
public interface CommandSimpleTextResponder extends CommandTextResponder {
	String getString(UnaryOperator<String> argGetter);

	@Override
	default String getString(UnaryOperator<String> argGetter, ServerMember sender, ServerGuild server) {
		return this.getString(argGetter);
	}
}
