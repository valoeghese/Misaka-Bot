package tk.valoeghese.misakabot.command;

import java.util.function.UnaryOperator;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

@FunctionalInterface
public interface CommandSimpleTextResponder extends CommandTextResponder {
	String getString(UnaryOperator<String> argGetter);

	@Override
	default String getString(UnaryOperator<String> argGetter, User sender, Guild server) {
		return this.getString(argGetter);
	}
}
