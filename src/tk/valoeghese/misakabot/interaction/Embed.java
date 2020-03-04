package tk.valoeghese.misakabot.interaction;

import java.util.List;
import java.util.function.Consumer;

public final class Embed {
	public Embed() {
		this(null);
	}

	public Embed(String title) {
		this.title = title;
	}

	private final String title;
	private String description;
	private String footer;
	private List<Field> fields;

	public Embed setDescription(String description) {
		this.description = description;
		return this;
	}

	public Embed setFooter(String footer) {
		this.footer = footer;
		return this;
	}

	public Embed addField(String key, String value, boolean displayInline) {
		this.fields.add(new Field(key, value, displayInline));
		return this;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}
	
	public String getFooter() {
		return this.footer;
	}

	public void forEachField(Consumer<Field> arg0) {
		if (this.fields != null) {
			this.fields.forEach(arg0);
		}
	}

	public C2SMessage wrap() {
		final Embed embed = this;
		return new C2SMessage() {
			@Override
			public String getMessageString() { return null; }
			@Override
			public Embed getMessageEmbed() { return embed; }
			@Override
			public boolean embed() { return true; }
		};
	}

	public static class Field {
		public Field(String key, String value, boolean displayInline) {
			this.key = key;
			this.value = value;
			this.displayInline = displayInline;
		}

		public final String key, value;
		public final boolean displayInline;
	}
}
