package tk.valoeghese.misakabot.command;

import java.util.function.UnaryOperator;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import tk.valoeghese.misakabot.MisakaBot;
import tk.valoeghese.misakabot.interaction.C2SMessage;

@FunctionalInterface
public interface CommandTextResponder extends CommandResponder {
	String getString(UnaryOperator<String> argGetter, User sender, Guild server);

	@Override
	default C2SMessage get(UnaryOperator<String> argGetter, User sender, Guild server, MessageChannel channel) {
		return MisakaBot.getImplementation().createTextMessage(() -> getString(argGetter, sender, server));
	}
}
