package tk.valoeghese.misakabot.api;

import java.util.function.Supplier;

import tk.valoeghese.misakabot.interaction.C2SMessage;

public interface Implementation {
	C2SMessage createTextMessage(String message);
	C2SMessage createTextMessage(Supplier<String> messageSupplier);
}
