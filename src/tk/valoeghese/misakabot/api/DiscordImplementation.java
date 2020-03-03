package tk.valoeghese.misakabot.api;

import java.util.function.Supplier;

import net.dv8tion.jda.api.entities.MessageEmbed;
import tk.valoeghese.misakabot.interaction.C2SMessage;

// TODO move JDA related stuff to here, abstracting the bot from discord
public final class DiscordImplementation implements Implementation {
	public DiscordImplementation(String botConnection) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public C2SMessage createTextMessage(String message) {
		return new C2SMessage() {
			@Override
			public String getMessageString() { return message; }
			@Override
			public MessageEmbed getMessageEmbed() { return null; }
			@Override
			public boolean embed() { return false; }
		};
	}

	@Override
	public C2SMessage createTextMessage(Supplier<String> messageSupplier) {
		return new C2SMessage() {
			@Override
			public String getMessageString() { return messageSupplier.get(); }
			@Override
			public MessageEmbed getMessageEmbed() { return null; }
			@Override
			public boolean embed() { return false; }
		};
	}
}
