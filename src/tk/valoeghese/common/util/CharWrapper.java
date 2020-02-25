package tk.valoeghese.common.util;

public class CharWrapper {
	public CharWrapper() {
		this.value = 0;
	}

	public CharWrapper(char startValue) {
		this.value = startValue;
	}

	private char value;

	public void setValue(char value) {
		this.value = value;
	}

	public char charValue() {
		return this.value;
	}
}
