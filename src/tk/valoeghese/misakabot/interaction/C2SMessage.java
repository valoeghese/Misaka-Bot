package tk.valoeghese.misakabot.interaction;

import java.util.function.Supplier;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface C2SMessage {
	MessageEmbed getMessageEmbed();
	String getMessageString();
	boolean embed();

	static C2SMessage createEmbedMessage(Supplier<MessageEmbed> embedSupplier) {
		return new C2SMessage() {
			@Override
			public String getMessageString() { return null; }
			@Override
			public MessageEmbed getMessageEmbed() { return embedSupplier.get(); }
			@Override
			public boolean embed() { return true; }
		};
	}
}
