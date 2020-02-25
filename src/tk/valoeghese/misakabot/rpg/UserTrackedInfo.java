package tk.valoeghese.misakabot.rpg;

import tk.valoeghese.misakabot.util.TrackedInfo;
import tk.valoeghese.sod.BinaryData;
import tk.valoeghese.sod.DataSection;

public final class UserTrackedInfo extends TrackedInfo {
	UserTrackedInfo(long uuid) {
		this.uuid = uuid;
	}

	private long uuid;

	public long getUUID() {
		return this.uuid;
	}

	@Override
	public void readData(BinaryData data) {
		super.readData(data);
		DataSection user = data.get("user");

		if (user != null) {
			this.uuid = user.readLong(0);
		}
	}

	@Override
	public void writeData(BinaryData data) {
		super.writeData(data);
		DataSection user = new DataSection();
		user.writeLong(this.uuid);
		data.put("user", user);
	}
}
