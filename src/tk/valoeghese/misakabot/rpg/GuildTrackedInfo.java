package tk.valoeghese.misakabot.rpg;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import tk.valoeghese.misakabot.util.TrackedInfo;
import tk.valoeghese.sod.BinaryData;
import tk.valoeghese.sod.DataSection;

public final class GuildTrackedInfo extends TrackedInfo {
	public GuildTrackedInfo(long guild) {
		this.guild = guild;
		this.prefix = "\u26a1";
		this.put("prefix", this.prefix);
	}

	private long guild;
	private final Long2ObjectMap<UserTrackedInfo> userInfoMap = new Long2ObjectArrayMap<>();
	private String prefix;

	public String getCommandPrefix() {
		return this.prefix;
	}
	
	public void setCommandPrefix(String newPrefix) {
		this.prefix = newPrefix;
		this.put("prefix", this.prefix);
	}

	public UserTrackedInfo getUserInfo(long id) {
		return this.userInfoMap.computeIfAbsent(id, UserTrackedInfo::new);
	}

	public long getGuild() {
		return this.guild;
	}

	@Override
	public void readData(BinaryData data) {
		super.readData(data);
		DataSection guild = data.get("guild_userinfo");

		if (guild != null) {
			this.guild = guild.readLong(0);
		}
	}

	@Override
	public void writeData(BinaryData data) {
		super.writeData(data);
		DataSection guild = new DataSection();
		guild.writeLong(this.guild);
		data.put("guild", guild);
	}

	public static GuildTrackedInfo get(long guild) {
		return FROM_GUILD.computeIfAbsent(guild, GuildTrackedInfo::new);
	}

	private static final Long2ObjectMap<GuildTrackedInfo> FROM_GUILD = new Long2ObjectArrayMap<>();
}
