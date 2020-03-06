package tk.valoeghese.misakabot;

import java.util.Locale;

import javax.security.auth.login.LoginException;

import tk.valoeghese.misakabot.api.DiscordImplementation;
import tk.valoeghese.misakabot.api.Implementation;
import tk.valoeghese.misakabot.command.Command;
import tk.valoeghese.misakabot.interaction.C2SMessage;
import tk.valoeghese.misakabot.interaction.Embed;
import tk.valoeghese.misakabot.interaction.MessageListener;
import tk.valoeghese.misakabot.interaction.RecieveMessageEvent;
import tk.valoeghese.misakabot.interaction.ServerGuild;
import tk.valoeghese.misakabot.interaction.ServerMember;
import tk.valoeghese.misakabot.rpg.character.Gender;
import tk.valoeghese.misakabot.rpg.character.RPGUserStage;
import tk.valoeghese.misakabot.rpg.character.UserCharacter;
import tk.valoeghese.misakabot.rpg.save.GuildSaveManager;
import tk.valoeghese.misakabot.rpg.save.GuildTrackedInfo;
import tk.valoeghese.misakabot.rpg.save.UserTrackedInfo;
import tk.valoeghese.misakabot.rpg.world.World;
import tk.valoeghese.misakabot.rpg.world.setting.Settings;

public class MisakaBot implements MessageListener {
	public static boolean online = true;
	private static Implementation implementation;
	private static World world;

	public static void main(String[] args) throws LoginException {
		//Pingme.setupJDA(args[0]);
		//return;
		startMainService(args);
	}

	private static void startMainService(String[] args) {
		setImplementation(new DiscordImplementation(args[0]));

		world = World.builder()
				.initialSetting(Settings.TEST)
				.build();

		Command.builder() // test command 1
			.args("arg0")
			.name("echo")
			.simpleCallback(cmdArgs -> cmdArgs.apply("arg0"))
			.build();

		Command.builder()
			.name("ping") // test command 2
			.simpleCallback(cmdArgs -> "pong")
			.build();

		Command.builder() // character creation, display, and cancelling command
			.name("character")
			.callback((cmdArgs, sender, guild, channel) -> {
				UserTrackedInfo userInfo = GuildTrackedInfo.get(guild.id()).getUserInfo(sender.id());
				RPGUserStage userStage = userInfo.getStageOrSetNotStarted();

				switch (userStage) {
				case CREATING_CHARACTER:
					userInfo.put("userStage", RPGUserStage.NOT_STARTED);
					return C2SMessage.createTextMessage("Cancelled character creation!");
				case IN_GAME:
					UserCharacter character = userInfo.getCharacter();
					return characterStatEmbed(character);
				case NOT_STARTED:
				default:
					userInfo.put("userStage", RPGUserStage.CREATING_CHARACTER);
					userInfo.put("characterCreationBound", channel.id());
					userInfo.put("characterCreationStage", 0);
					return C2SMessage.createTextMessage("Started character creation! (bound to this channel)\nEnter your character's name:");
				}
			})
			.build();

		Command.builder()
			.name("saveRPGData")
			.simpleCallback(cmdArgs -> {
				try {
					GuildTrackedInfo.forEach(GuildSaveManager::save);
					return "Successful Save!";
				} catch (Throwable e) {
					return e.getMessage();
				}
			})
			.build();

		Command.builder()
			.name("help")
			.embedCallback((cmdArgs, sender, guild) -> {
				Embed embed = new Embed("Commands")
						.setFooter("Prefix: " + GuildTrackedInfo.get(guild.id()).getCommandPrefix());

				Command.forEach(command -> {
					embed.addField(command.name, command.toString(), true);
				});

				return embed;
			})
			.build();

		Command.builder()
			.name("shutdown")
			.callback((cmdArgs, sender, guild, channel) -> {
				if (guild.getOwnerId() == 521522396856057876L) {
					online = false;
					channel.sendMessage("さよなら！");
					implementation.shutdown();
					return C2SMessage.createTextMessage("");
				}

				return C2SMessage.createTextMessage("Only Valoeghese#3216 can shut down the bot!");
			})
			.build();

		implementation.subscribeOnReceiveMessage(new MisakaBot());
		implementation.setOnShutdown(() -> GuildTrackedInfo.forEach(GuildSaveManager::save));
		implementation.start();
	}

	public static World getWorld() {
		return world;
	}

	private static void setImplementation(Implementation impl) {
		implementation = impl;
	}

	public static Implementation getImplementation() {
		return implementation;
	}

	private static C2SMessage characterStatEmbed(UserCharacter character) {
		character.calculateLevel();
		return new Embed(character.getName())
				.setDescription(new StringBuilder()
						.append("**Gender**: ").append(character.getGender().toString().toLowerCase(Locale.ROOT)).append('\n')
						.append("**Ability**: ").append(character.getEsperAbility().getDisplayName()).append('\n')
						.append("**Level**: ").append(String.valueOf(character.getLevel())).toString())
				.wrap();
	}

	@Override
	public void onMessageReceived(RecieveMessageEvent event) {
		ServerMember author = event.getMessageAuthor();

		if (author.isBot()) {
			return;
		}

		ServerGuild guild = event.getGuild();
		GuildTrackedInfo gti = GuildTrackedInfo.get(guild.id());
		String message = event.getMessage().getRawContent();
		final String commandPrefix = gti.getCommandPrefix();

		if (message.startsWith(commandPrefix)) {
			String commandString = message.substring(commandPrefix.length());
			int commandFinalIndex = commandString.indexOf(' ');
			String commandName = commandFinalIndex == -1 ? commandString : commandString.substring(0, commandFinalIndex);
			Command command = Command.get(commandName);

			if (command != null) {
				C2SMessage reply = command.handle(commandFinalIndex == -1 ? "" : commandString.substring(commandFinalIndex), commandPrefix, author, event.getGuild(), event.getChannel());

				if (online) {
					event.getChannel().sendMessage(reply);
				}
			}
		} else {
			UserTrackedInfo uti = gti.getUserInfo(author.id());

			if (uti.containsKey("userStage")) {
				if (uti.getEnum("userStage", RPGUserStage.values()) == RPGUserStage.CREATING_CHARACTER) {
					if (event.getChannel().id() == uti.getLong("characterCreationBound")) {
						int stage = uti.getInt("characterCreationStage");
						String msgDisplay = event.getMessage().getDisplayContent();

						switch (stage) {
						case 0:
							uti.put("characterName", msgDisplay);
							uti.put("characterCreationStage", 1);
							event.getChannel().sendMessage("**Character Name**: `" + msgDisplay + "`");
							event.getChannel().sendMessage("Enter your character's gender: (M = male/F = female/O = other)");
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

								event.getChannel().sendMessage("Created Character!");
								event.getChannel().sendMessage(characterStatEmbed(uti.getCharacter()));
								event.getChannel().sendMessage(uti.getCharacter().describeWorldMessage());
							}
							break;
						}
					}
				}
			}
		}
	}
}
