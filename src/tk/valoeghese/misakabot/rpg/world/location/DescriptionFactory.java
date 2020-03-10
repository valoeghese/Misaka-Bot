package tk.valoeghese.misakabot.rpg.world.location;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;

public final class DescriptionFactory {
	public DescriptionFactory(List<DescriptionEntry> list) {
		this.list = list;
	}

	private final Iterable<DescriptionEntry> list;

	public String getDescription(int perception) {
		StringBuilder sb = new StringBuilder();
		this.list.forEach(e -> e.addToString(perception, sb));
		return sb.toString();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Builder() {
		}

		public List<DescriptionEntry> list = new ArrayList<>();

		public Builder append(String text) {
			return this.append(i -> true, text);
		}

		public Builder append(int minPerception, String text) {
			return this.append(i -> i > minPerception, text);
		}

		public Builder append(IntPredicate perceptionCheck, String text) {
			this.list.add(new DescriptionEntry(perceptionCheck, text));
			return this;
		}

		public DescriptionFactory build() {
			return new DescriptionFactory(this.list);
		}
	}

	private static class DescriptionEntry {
		public DescriptionEntry(IntPredicate predicate, String text) {
			this.predicate = predicate;
			this.text = text;
		}

		private final IntPredicate predicate;
		private final String text;

		public void addToString(int perception, StringBuilder builder) {
			if (this.predicate.test(perception)) {
				builder.append(this.text);
			}
		}
	}
}
