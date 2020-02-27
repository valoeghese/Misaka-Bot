package tk.valoeghese.misakabot.util;

import java.util.HashMap;
import java.util.Map;

import tk.valoeghese.common.util.CharWrapper;
import tk.valoeghese.common.util.Flag;
import tk.valoeghese.common.util.Wrapper;
import tk.valoeghese.sod.DataSection;

public abstract class TrackedInfo {
	private final Map<String, Object> trackedInfo = new HashMap<>();

	public final String getString(String key) {
		return (String) this.trackedInfo.get(key);
	}

	public final int getInt(String key) {
		return (int) this.trackedInfo.get(key);
	}

	public final long getLong(String key) {
		try {
			return (long) this.trackedInfo.get(key);
		} catch (NullPointerException e) {
			System.out.println(key);
			System.out.println(this.trackedInfo);
			throw e;
		}
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

	public final <T extends Enum<?>> T getEnum(String key, T[] values) {
		Integer index = (Integer) this.trackedInfo.get(key);

		if (index == null) {
			return null;
		}

		return index < values.length ? values[index] : null;
	}

	public final boolean containsKey(String key) {
		return this.trackedInfo.containsKey(key);
	}

	/**
	 * @param <T> Boolean, String, Integer, Long, Character, Enum, or Float.
	 * @param key the key for the data hashmap.
	 * @param value the value for the data hashmap.
	 */
	public final <T> void put(String key, T value) {
		if (value instanceof Enum) {
			this.trackedInfo.put(key, ((Enum<?>) value).ordinal());
		} else {
			this.trackedInfo.put(key, value);
		}
	}

	public void writeData(DataSection data) {
		this.trackedInfo.forEach((key, value) -> {
			if (value instanceof Boolean) // New lines for ease of reading this long if else statement
			{
				data.writeString("z" + key);
				data.writeBoolean((boolean) value);
			}
			else if (value instanceof Integer)
			{
				data.writeString("i" + key);
				data.writeInt((int) value);
			}
			else if (value instanceof Long)
			{
				data.writeString("j" + key);
				data.writeLong((long) value);
			}
			else if (value instanceof Character)
			{
				data.writeString("c" + key);
				data.writeShort((short) (char) value); // yes I actually need to cast to char, then short
			}
			else if (value instanceof Float)
			{
				data.writeString("f" + key);
				data.writeFloat((float) value);
			}
			else if (value instanceof String)
			{
				data.writeString("s" + key);
				data.writeString((String) value);
			}
		});
	}

	public void readData(DataSection data) {
		Flag readKey = new Flag(true);
		Wrapper<String> key = new Wrapper<>("");
		CharWrapper type = new CharWrapper();

		data.forEach((value) -> {
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
				case 'j':
					this.trackedInfo.put(key.getValue(), (long) value);
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
	}
}
