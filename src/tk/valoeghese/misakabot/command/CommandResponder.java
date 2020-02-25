package tk.valoeghese.misakabot.command;

import java.util.function.UnaryOperator;

import net.dv8tion.jda.api.entities.Message;

@FunctionalInterface
public interface CommandResponder {
	Message get(UnaryOperator<String> argGetter);
}
