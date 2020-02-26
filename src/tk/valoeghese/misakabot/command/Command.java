package tk.valoeghese.misakabot.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import tk.valoeghese.misakabot.util.discord.DiscordMessage;

public class Command {
	private Command(Builder builder) {
		this.mandatoryArgs = builder.args.size();
		List<CommandEntry> list = new ArrayList<>(builder.args);
		list.addAll(builder.optionalArgs);
		this.args = list.toArray(new CommandEntry[0]);
		this.callback = builder.callback;
		this.name = builder.name;
		COMMAND_MAP.put(this.name, this);
	}

	private final CommandEntry[] args;
	private final CommandResponder callback;
	private final int mandatoryArgs;
	public final String name;

	public String getArgKey(int index) {
		if (index < this.args.length) {
			return this.args[index].name;
		} else {
			return "_"; // throwaway
		}
	}

	public DiscordMessage handle(String request, String prefix, User sender, Guild guild, MessageChannel channel) {
		char[] cArr = request.toCharArray();
		Map<String, String> values = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		char quote = ' ';
		int index = 0;

		for (char c : cArr) {
			switch (c) {
			case '"':
				if (quote == '"') {
					quote = ' ';
					values.put(getArgKey(index++), sb.toString());
					sb = new StringBuilder();
				} else if (quote == '\'') {
					sb.append(c);
				} else {
					quote = '"';
					sb = new StringBuilder();
				}
				break;
			case '\'':
				if (quote == '\'') {
					quote = ' ';
					values.put(getArgKey(index++), sb.toString());
					sb = new StringBuilder();
				} else if (quote == '"') {
					sb.append(c);
				} else {
					quote = '\'';
					sb = new StringBuilder();
				}
				break;
			case ' ':
				if (quote == ' ') {
					String value = sb.toString();

					if (!value.isEmpty()) {
						values.put(getArgKey(index++), value);
					}
					sb = new StringBuilder();
				} else {
					sb.append(c);
				}
				break;
			default:
				sb.append(c);
			}
		}

		String finalArg = sb.toString();

		if (!finalArg.isEmpty()) {
			values.put(getArgKey(index++), sb.toString());
		}

		if (index < this.mandatoryArgs) {
			final int indexx = index;
			DiscordMessage.createTextMessage(() -> new StringBuilder("Invalid number of args! ")
					.append(String.valueOf(indexx))
					.append("/")
					.append(String.valueOf(mandatoryArgs))
					.append(" required args.\nSyntax: ")
					.append(prefix)
					.append(this.toString())
					.toString());
		}

		return this.callback.get(values::get, sender, guild, channel);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.name);

		for (CommandEntry arg : args) {
			sb.append(' ');
			sb.append(arg);
		}

		return sb.toString();
	}

	private static final Map<String, Command> COMMAND_MAP = new HashMap<>();

	public static Command get(String name) {
		return COMMAND_MAP.get(name);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Builder() {
		}

		private List<CommandEntry> args = new ArrayList<>();
		private List<CommandEntry> optionalArgs = new ArrayList<>();
		private String name;
		private CommandResponder callback;

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder args(String... names) {
			this.args = new ArrayList<>();

			for (String name : names) {
				this.args.add(new CommandEntry(name, false));
			}
			return this;
		}

		public Builder optionalArgs(String... names) {
			this.optionalArgs = new ArrayList<>();

			for (String name : names) {
				this.optionalArgs.add(new CommandEntry(name, true));
			}
			return this;
		}

		public Builder textCallback(CommandTextResponder callback) {
			this.callback = callback;
			return this;
		}

		public Builder embedCallback(CommandEmbedResponder callback) {
			this.callback = callback;
			return this;
		}

		public Builder simpleCallback(CommandSimpleTextResponder callback) {
			this.callback = callback;
			return this;
		}

		public Builder callback(CommandResponder callback) {
			this.callback = callback;
			return this;
		}

		public Command build() {
			return new Command(this);
		}
	}

	public static class CommandEntry {
		public CommandEntry(String name, boolean optional) {
			this.name = name;
			this.optional = optional;
			this.op = optional ? '[' : '<';
			this.cl = optional ? ']' : '>';
		}

		public final String name;
		public final boolean optional;
		private final char op, cl;

		@Override
		public String toString() {
			return new StringBuilder()
					.append(this.op)
					.append(this.name)
					.append(this.cl)
					.toString();
		}
	}
}
