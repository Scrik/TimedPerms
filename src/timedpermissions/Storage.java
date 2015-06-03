package timedpermissions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import org.bukkit.configuration.file.YamlConfiguration;

public class Storage {

	private final File datafile;
	public Storage(TimedPermissions plugin) {
		datafile = new File(plugin.getDataFolder(), "data.yml");
	}

	private final ConcurrentHashMap<UUID, Long> expirytime = new ConcurrentHashMap<UUID, Long>();

	public HashMap<UUID, Long> getData() {
		return new HashMap<UUID, Long>(expirytime);
	}

	public void addEntry(UUID uniqueId, long time) {
		expirytime.put(uniqueId, System.currentTimeMillis() + time);
	}

	public void removeEntry(UUID uuid) {
		expirytime.remove(uuid);
	}

	public long getValue(UUID uuid) {
		return expirytime.getOrDefault(uuid, -1L);
	}

	public Iterator<Entry<UUID, Long>> directIterator() {
		return expirytime.entrySet().iterator();
	}

	public boolean modify(UUID uuid, BiFunction<UUID, Long, Long> function) {
		return expirytime.computeIfPresent(uuid, function) != null;
	}

	public void load() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(datafile);
		for (String uuid : config.getKeys(false)) {
			expirytime.put(UUID.fromString(uuid), config.getLong(uuid));
		}
	}

	public void save() {
		YamlConfiguration config = new YamlConfiguration();
		for (Entry<UUID, Long> entry : expirytime.entrySet()) {
			config.set(entry.getKey().toString(), entry.getValue());
		}
		try {
			config.save(datafile);
		} catch (IOException e) {
		}
	}

}
