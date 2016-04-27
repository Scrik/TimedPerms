package timedpermissions.subscription;

import java.io.File;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.file.YamlConfiguration;

import timedpermissions.TimedPermissions;

public class LegacyStorage {

	private final File datafile;
	private final Storage storage;
	public LegacyStorage(TimedPermissions plugin, Storage storage) {
		this.datafile = new File(plugin.getDataFolder(), "data.yml");
		this.storage = storage;
	}

	private final ConcurrentHashMap<UUID, Long> expirytime = new ConcurrentHashMap<UUID, Long>();

	public void load() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(datafile);
		for (String uuid : config.getKeys(false)) {
			expirytime.put(UUID.fromString(uuid), config.getLong(uuid));
		}
	}

	public void migrate() {
		for (Entry<UUID, Long> entry : expirytime.entrySet()) {
			storage.addSubscription(new SubscriptionMainGroup(entry.getValue(), entry.getKey(), "legacy unknown"));
		}
	}

}
