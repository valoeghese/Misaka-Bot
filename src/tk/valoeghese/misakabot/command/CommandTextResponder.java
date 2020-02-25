package tk.valoeghese.misakabot.command;

import java.util.function.UnaryOperator;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.entities.DataMessage;

@FunctionalInterface
public interface CommandTextResponder extends CommandResponder {
	String getString(UnaryOperator<String> argGetter);

	@Override
	default Message get(UnaryOperator<String> argGetter) {
		return new DataMessage(false, this.getString(argGetter), null, null);
	}
}
