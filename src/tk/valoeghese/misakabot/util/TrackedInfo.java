package tk.valoeghese.misakabot.util;

import java.util.HashMap;
import java.util.Map;

import tk.valoeghese.common.util.Flag;
import tk.valoeghese.common.util.CharWrapper;
import tk.valoeghese.common.util.Wrapper;
import tk.valoeghese.sod.BinaryData;
import tk.valoeghese.sod.DataSection;

public abstract class TrackedInfo {
	private final Map<String, Object> trackedInfo = new HashMap<>();

	public final String getString(String key) {
		return (String) this.trackedInfo.get(key);
	}

	public final int getInt(String key) {
		return (int) this.trackedInfo.get(key);
	}

	public final char getChar(String key) {
		return (char) this.trackedInfo.get(key);
	}

	public final boolean getBoolean(String key) {
		return (boolean) this.trackedInfo.get(key);
	}

	public final float getFloat(String key) {
		return (float) this.trackedInfo.get(key);
	}

	public final boolean containsKey(String key) {
		return this.trackedInfo.containsKey(key);
	}

	/**
	 * @param <T> Boolean, String, Integer, Character, or Float.
	 * @param key the key for the data hashmap.
	 * @param value the value for the data hashmap.
	 */
	public final <T> void put(String key, T value) {
		this.trackedInfo.put(key, value);
	}

	public void writeData(BinaryData data) {
		DataSection tracked = new DataSection();
		this.trackedInfo.forEach((key, value) -> {
			if (value instanceof Boolean) // New lines for ease of reading this long if else statement
			{
				tracked.writeString("z" + key);
				tracked.writeBoolean((boolean) value);
			}
			else if (value instanceof Integer)
			{
				tracked.writeString("i" + key);
				tracked.writeInt((int) value);
			}
			else if (value instanceof Character)
			{
				tracked.writeString("c" + key);
				tracked.writeShort((short) (char) value); // yes I actually need to cast to char, then short
			}
			else if (value instanceof Float)
			{
				tracked.writeString("f" + key);
				tracked.writeFloat((float) value);
			}
			else if (value instanceof String)
			{
				tracked.writeString("s" + key);
				tracked.writeString((String) value);
			}
		});

		data.put("tracked", tracked);
	}

	public void readData(BinaryData data) {
		DataSection tracked = data.getOrCreate("tracked");

		Flag readKey = new Flag(true);
		Wrapper<String> key = new Wrapper<>("");
		CharWrapper type = new CharWrapper();

		tracked.forEach((value) -> {
			if (readKey.booleanValue()) {
				String rawKey = (String) value;
				key.setValue(rawKey.substring(1));
				type.setValue(rawKey.charAt(0)); // type char
				readKey.unflag();
			} else {
				switch (type.charValue()) {
				case 'z':
					this.trackedInfo.put(key.getValue(), (boolean) value);
					break;
				case 'i':
					this.trackedInfo.put(key.getValue(), (int) value);
					break;
				case 'c':
					this.trackedInfo.put(key.getValue(), (char) value);
					break;
				case 'f':
					this.trackedInfo.put(key.getValue(), (float) value);
					break;
				case 's':
					this.trackedInfo.put(key.getValue(), (String) value);
					break;
				}
				readKey.flag();
			}
		});

		data.put("tracked", tracked);
	}
}
