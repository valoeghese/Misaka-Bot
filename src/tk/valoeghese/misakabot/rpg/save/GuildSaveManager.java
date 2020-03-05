package tk.valoeghese.misakabot.rpg.save;

import java.io.File;
import java.io.IOException;

import tk.valoeghese.sod.BinaryData;
import tk.valoeghese.sod.DataSection;

public class GuildSaveManager {
	public static GuildTrackedInfo loadOrCreate(long guildUUID) {
		File file = new File("./guild_" + String.valueOf(guildUUID) + ".sod");

		if (file.exists()) {
			GuildTrackedInfo result = new GuildTrackedInfo(guildUUID);
			BinaryData data = BinaryData.read(file);
			result.setBinaryDataForUserLoading(data);
			result.readData(data.get("GuildTracked"));
			return result;
		} else {
			return new GuildTrackedInfo(guildUUID);
		}
	}

	public static void save(long guildUUID, GuildTrackedInfo guildInfo) {
		BinaryData data = new BinaryData();
		DataSection guildTracked = new DataSection();
		guildInfo.writeData(guildTracked);
		data.put("GuildTracked", guildTracked);

		// user data
		guildInfo.forEachUser((id, userInfo) -> {
			DataSection userInfoSection = new DataSection();
			userInfo.writeData(userInfoSection);
			data.put("User" + String.valueOf(id), userInfoSection);
		});

		// npc data
		guildInfo.forEachNPC((id, npcInfo) -> {
			DataSection npcInfoSection = new DataSection();
			npcInfo.writeData(npcInfoSection);
			data.put("NPC" + String.valueOf(id), npcInfoSection);
		});

		File file = new File("./guild_" + String.valueOf(guildUUID) + ".sod");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		data.write(file);
	}
}
