package timedpermissions.subscription;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import timedpermissions.TimedPermissions;

public class Storage {

	private final File datafile;
	public Storage(TimedPermissions plugin) {
		datafile = new File(plugin.getDataFolder(), "storage.yml");
	}

	private final Set<Subscription> subscriptions = Collections.newSetFromMap(new ConcurrentHashMap<Subscription, Boolean>()); 

	public List<Subscription> getSubscriptions() {
		return new ArrayList<Subscription>(subscriptions);
	} 

	public void removeSubscription(Subscription subscription) {
		subscriptions.remove(subscription);
	}

	public void addSubscription(Subscription subscription) {
		subscriptions.add(subscription);
	}

	public void load() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(datafile);
		for (String entry : config.getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection(entry);
			String type = section.getString("type");
			Subscription sub = Subscription.create(type);
			sub.load(section);
			subscriptions.add(sub);
		}
	}

	public void save() {
		YamlConfiguration config = new YamlConfiguration();
		for (Subscription sub : subscriptions) {
			ConfigurationSection section = config.createSection(sub.getTempUID());
			section.set("type", Subscription.getType(sub.getClass()));
			sub.save(section);
		}
		try {
			config.save(datafile);
		} catch (IOException e) {
		}
	}

}
