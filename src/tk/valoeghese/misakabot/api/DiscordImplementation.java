package tk.valoeghese.misakabot.api;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tk.valoeghese.misakabot.interaction.Embed;
import tk.valoeghese.misakabot.interaction.MessageListener;
import tk.valoeghese.misakabot.interaction.RecieveMessageEvent;
import tk.valoeghese.misakabot.interaction.S2CMessage;
import tk.valoeghese.misakabot.interaction.ServerChannel;
import tk.valoeghese.misakabot.interaction.ServerGuild;
import tk.valoeghese.misakabot.interaction.ServerMember;

public final class DiscordImplementation extends ListenerAdapter implements Implementation {
	public DiscordImplementation(String botConnection) {
		this.botConnection = botConnection;
	}

	public static JDA jda;
	private final String botConnection;
	private final List<MessageListener> listeners = new ArrayList<>();
	private Runnable shutdownMethod = () -> {};

	@Override
	public void subscribeOnReceiveMessage(MessageListener e) {
		this.listeners.add(e);
	}

	@Override
	public void onShutdown(ShutdownEvent event) {
		this.shutdownMethod .run();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		RecieveMessageEvent wrapper = wrap(event);
		this.listeners.forEach(listener -> listener.onMessageReceived(wrapper));
	}

	private static RecieveMessageEvent wrap(MessageReceivedEvent event) {
		Guild jdaGuild = event.getGuild();
		MessageChannel jdaChannel = event.getChannel();
		User jdaAuthor = event.getAuthor();
		Message jdaMessage = event.getMessage();

		ServerGuild guild = new ServerGuild(jdaGuild.getIdLong(), jdaGuild.getOwnerIdLong());
		ServerChannel channel = new ServerChannel(jdaChannel.getIdLong(), msg -> {
			if (msg.embed()) {
				jdaChannel.sendMessage(wrap(msg.getMessageEmbed())).queue();
			} else {
				jdaChannel.sendMessage(msg.getMessageString()).queue();
			}
		}, msg -> jdaChannel.sendMessage(msg).queue());
		ServerMember messageAuthor = new ServerMember(jdaAuthor.getIdLong(), jdaAuthor.isBot());
		S2CMessage message = new S2CMessage(jdaMessage.getContentRaw(), jdaMessage.getContentDisplay());

		return new RecieveMessageEvent(guild, channel, message, messageAuthor);
	}

	private static MessageEmbed wrap(Embed embed) {
		EmbedBuilder eb = new EmbedBuilder();
		String title = embed.getTitle();
		String description = embed.getDescription();
		String footer = embed.getFooter();

		if (title != null) {
			eb.setTitle(title);
		}

		if (description != null) {
			eb.setDescription(description);
		}

		if (footer != null) {
			eb.setFooter(footer);
		}

		embed.forEachField(f -> eb.addField(f.key, f.value, f.displayInline));

		return eb.build();
	}

	@Override
	public void start() {
		try {
			jda = new JDABuilder(this.botConnection)
				.addEventListeners(this)
				.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		jda.shutdown();
	}

	@Override
	public void setOnShutdown(Runnable onShutdown) {
		this.shutdownMethod = onShutdown;
	}
}
