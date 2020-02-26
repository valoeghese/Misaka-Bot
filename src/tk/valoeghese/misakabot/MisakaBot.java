package tk.valoeghese.misakabot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tk.valoeghese.misakabot.command.Command;
import tk.valoeghese.misakabot.rpg.GuildTrackedInfo;
import tk.valoeghese.misakabot.rpg.UserTrackedInfo;
import tk.valoeghese.misakabot.rpg.character.Gender;
import tk.valoeghese.misakabot.rpg.character.RPGUserStage;
import tk.valoeghese.misakabot.rpg.character.UserCharacter;
import tk.valoeghese.misakabot.util.discord.DiscordMessage;

public class MisakaBot extends ListenerAdapter {
	public static void main(String[] args) throws LoginException {
		Command.builder() // test command 1
			.args("arg0")
			.name("echo")
			.simpleCallback(cmdArgs -> cmdArgs.apply("arg0"))
			.build();

		Command.builder()
			.name("ping") // test command 2
			.simpleCallback(cmdArgs -> "pong")
			.build();

		Command.builder() //
			.name("character")
			.callback((cmdArgs, sender, guild, channel) -> {
				UserTrackedInfo userInfo = GuildTrackedInfo.get(guild.getIdLong()).getUserInfo(sender.getIdLong());
				RPGUserStage userStage = userInfo.getStageOrSetNotStarted();

				switch (userStage) {
				case CREATING_CHARACTER:
					userInfo.put("userStage", RPGUserStage.NOT_STARTED);
					return DiscordMessage.createTextMessage("Cancelled character creation!");
				case IN_GAME:
					UserCharacter character = userInfo.getCharacter();
					return characterStatEmbed(character);
				case NOT_STARTED:
				default:
					userInfo.put("userStage", RPGUserStage.CREATING_CHARACTER);
					userInfo.put("characterCreationBound", channel.getIdLong());
					userInfo.put("characterCreationStage", 0);
					return DiscordMessage.createTextMessage("Started character creation! (bound to this channel)\nEnter your character's name:");
				}
			})
			.build();

		new JDABuilder(args[0])
			.addEventListeners(new MisakaBot())
			.build();
	}

	private static DiscordMessage characterStatEmbed(UserCharacter character) {
		character.calculateLevel();
		return DiscordMessage.createEmbedMessage(() -> new EmbedBuilder()
				.setTitle(character.name)
				.setDescription(new StringBuilder()
						.append("**Ability**: ").append(character.ability.getDisplayName()).append('\n')
						.append("**Level**: ").append(String.valueOf(character.abilityLevel)))
				.addField("potential", String.valueOf(character.potentialAbility), true)
				.build());
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		User author = event.getAuthor();

		if (author.isBot()) {
			return;
		}

		Guild guild = event.getGuild();
		GuildTrackedInfo gti = GuildTrackedInfo.get(guild.getIdLong());
		String message = event.getMessage().getContentRaw();
		final String commandPrefix = gti.getCommandPrefix();

		if (message.startsWith(commandPrefix)) {
			String commandString = message.substring(commandPrefix.length());
			int commandFinalIndex = commandString.indexOf(' ');
			String commandName = commandFinalIndex == -1 ? commandString : commandString.substring(0, commandFinalIndex);
			Command command = Command.get(commandName);

			if (command != null) {
				DiscordMessage reply = command.handle(commandFinalIndex == -1 ? "" : commandString.substring(commandFinalIndex), commandPrefix, author, event.getGuild(), event.getChannel());

				if (reply.embed()) {
					event.getChannel().sendMessage(reply.getMessageEmbed()).queue();
				} else {
					event.getChannel().sendMessage(reply.getMessageString()).queue();
				}
			}
		} else {
			UserTrackedInfo uti = gti.getUserInfo(author.getIdLong());

			if (uti.containsKey("userStage")) {
				if (uti.getEnum("userStage", RPGUserStage.values()) == RPGUserStage.CREATING_CHARACTER) {
					if (event.getChannel().getIdLong() == uti.getLong("characterCreationBound")) {
						int stage = uti.getInt("characterCreationStage");
						String msgDisplay = event.getMessage().getContentDisplay();

						switch (stage) {
						case 0:
							uti.put("characterName", msgDisplay);
							uti.put("characterCreationStage", 1);
							event.getChannel().sendMessage("**Character Name**: `" + msgDisplay + "`").queue();
							event.getChannel().sendMessage("Enter your character's gender: (M = male/F = female/O = other)").queue();
							break;
						case 1:
							Gender gender = Gender.ofEntered(msgDisplay);

							if (gender == null) {
								event.getChannel().sendMessage("Gender entered does not conform to (M = male/F = female/O = other)!");
							} else {
								uti.put("characterGender", gender);
								uti.put("characterCreationStage", 2);
								uti.put("userStage", RPGUserStage.IN_GAME);

								uti.setCharacter(UserCharacter.builder()
										.name(uti.getString("characterName"))
										.gender(uti.getEnum("characterGender", Gender.values()))
										.build());

								event.getChannel().sendMessage("Created Character!").queue();
								event.getChannel().sendMessage(characterStatEmbed(uti.getCharacter()).getMessageEmbed()).queue();
							}
							break;
						}
					}
				}
			}
		}
	}
}
