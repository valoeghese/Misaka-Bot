package tk.valoeghese.misakabot.interaction;

public final class S2CMessage {
	public S2CMessage(String rawContent, String displayContent) {
		this.rawContent = rawContent;
		this.displayContent = displayContent;
	}

	private final String rawContent;
	private final String displayContent;

	public String getRawContent() {
		return this.rawContent;
	}

	public String getDisplayContent() {
		return this.displayContent;
	}
}
