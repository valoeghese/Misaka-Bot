package tk.valoeghese.misakabot.util.discord;

import java.util.function.Supplier;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface DiscordMessage {
	MessageEmbed getMessageEmbed();
	String getMessageString();
	boolean embed();

	static DiscordMessage createTextMessage(Supplier<String> messageSupplier) {
		return new DiscordMessage() {
			@Override
			public String getMessageString() { return messageSupplier.get(); }
			@Override
			public MessageEmbed getMessageEmbed() { return null; }
			@Override
			public boolean embed() { return false; }
		};
	}

	static DiscordMessage createTextMessage(String message) {
		return new DiscordMessage() {
			@Override
			public String getMessageString() { return message; }
			@Override
			public MessageEmbed getMessageEmbed() { return null; }
			@Override
			public boolean embed() { return false; }
		};
	}

	static DiscordMessage createEmbedMessage(Supplier<MessageEmbed> embedSupplier) {
		return new DiscordMessage() {
			@Override
			public String getMessageString() { return null; }
			@Override
			public MessageEmbed getMessageEmbed() { return embedSupplier.get(); }
			@Override
			public boolean embed() { return true; }
		};
	}
}
