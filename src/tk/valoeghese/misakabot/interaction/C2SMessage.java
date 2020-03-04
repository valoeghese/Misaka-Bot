package tk.valoeghese.misakabot.interaction;

import java.util.function.Supplier;

public interface C2SMessage {
	String getMessageString();
	Embed getMessageEmbed();
	boolean embed();

	static C2SMessage createTextMessage(String message) {
		return new C2SMessage() {
			@Override
			public String getMessageString() { return message; }
			@Override
			public Embed getMessageEmbed() { return null; }
			@Override
			public boolean embed() { return false; }
		};
	}

	static C2SMessage createTextMessage(Supplier<String> messageSupplier) {
		return new C2SMessage() {
			@Override
			public String getMessageString() { return messageSupplier.get(); }
			@Override
			public Embed getMessageEmbed() { return null; }
			@Override
			public boolean embed() { return false; }
		};
	}
}
