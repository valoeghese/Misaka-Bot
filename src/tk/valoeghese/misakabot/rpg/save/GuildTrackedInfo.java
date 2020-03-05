package tk.valoeghese.misakabot.rpg.save;

import java.util.Map;
import java.util.function.BiConsumer;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import tk.valoeghese.misakabot.util.TrackedInfo;
import tk.valoeghese.sod.BinaryData;
import tk.valoeghese.sod.DataSection;

public final class GuildTrackedInfo extends TrackedInfo {
	public GuildTrackedInfo(long guild) {
		this.guild = guild;
		this.prefix = "\u26a1";
		this.put("prefix", this.prefix);
		this.put("guild", this.guild);
	}

	private long guild;
	private final Long2ObjectMap<UserTrackedInfo> userInfoMap = new Long2ObjectArrayMap<>();
	private final Int2ObjectMap<NPCTrackedInfo> npcInfoMap = new Int2ObjectArrayMap<>();
	private String prefix;
	private BinaryData loadedData = new BinaryData();

	public String getCommandPrefix() {
		return this.prefix;
	}

	public void setCommandPrefix(String newPrefix) {
		this.prefix = newPrefix;
		this.put("prefix", this.prefix);
	}

	public UserTrackedInfo getUserInfo(long id) {
		return this.userInfoMap.computeIfAbsent(id, uuid -> {
			UserTrackedInfo result = new UserTrackedInfo(uuid);
			DataSection userData = this.loadedData.get("User" + String.valueOf(uuid));

			if (userData != null) {
				result.readData(userData);
			}

			return result;
		});
	}

	public long getGuild() {
		return this.guild;
	}

	public void setBinaryDataForUserLoading(BinaryData data) {
		this.loadedData = data;
	}

	@Override
	public void readData(DataSection data) {
		super.readData(data);
		this.guild = this.getLong("guild");
	}

	public static GuildTrackedInfo get(long guild) {
		return FROM_GUILD.computeIfAbsent(guild, GuildSaveManager::loadOrCreate);
	}

	public static void forEach(BiConsumer<Long, GuildTrackedInfo> callback) {
		FROM_GUILD.forEach(callback);
	}

	private static final Long2ObjectMap<GuildTrackedInfo> FROM_GUILD = new Long2ObjectArrayMap<>();

	public void forEachUser(GuildUserCallback callback) {
		LongSet trackedUsers = new LongArraySet();

		// Add loaded data
		this.userInfoMap.forEach((id, userInfo) -> {
			trackedUsers.add((long) id);
			callback.accept(id, userInfo);
		});

		// Add data that hasn't been loaded yet but is in memory
		for (Map.Entry<String, DataSection> userData : this.loadedData) {
			String trackedUser = userData.getKey();

			if (trackedUser.startsWith("U")) { // if it is a User<id> data section
				long id = Long.parseLong(trackedUser.substring(4)); // get id

				if (!trackedUsers.contains(id)) { // if the callback has not already consumed this id
					trackedUsers.add(id); // remember that this has been consumed

					UserTrackedInfo userInfo = new UserTrackedInfo(id); // create and load user info
					userInfo.readData(userData.getValue());
					callback.accept(id, userInfo);
				}
			}
		}
	}

	public void forEachNPC(GuildNPCCallback callback) {
		IntSet trackedNPCS = new IntArraySet();

		// Add loaded data
		this.npcInfoMap.forEach((id, userInfo) -> {
			trackedNPCS.add((int) id);
			callback.accept(id, userInfo);
		});

		// Add data that hasn't been loaded yet but is in memory
		for (Map.Entry<String, DataSection> userData : this.loadedData) {
			String trackedUser = userData.getKey();

			if (trackedUser.startsWith("N")) { // if it is a NPC<id> data section
				int id = Integer.parseInt(trackedUser.substring(3)); // get id

				if (!trackedNPCS.contains(id)) { // if the callback has not already consumed this id
					trackedNPCS.add(id); // remember that this has been consumed

					NPCTrackedInfo userInfo = new NPCTrackedInfo(id); // create and load user info
					userInfo.readData(userData.getValue());
					callback.accept(id, userInfo);
				}
			}
		}
	}
}
