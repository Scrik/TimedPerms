package timedpermissions;

import java.util.Map.Entry;
import java.util.UUID;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ExpiryTask extends BukkitRunnable {

	private final Storage storage;
	public ExpiryTask(Storage storage) {
		this.storage = storage;
	}

	@Override
	public void run() {
		GroupManager groupManager = JavaPlugin.getPlugin(GroupManager.class);
		for (Entry<UUID, Long> entry : storage.getData().entrySet()) {
			if (System.currentTimeMillis() >= entry.getValue()) {
				try {
					for (OverloadedWorldHolder holder : groupManager.getWorldsHolder().allWorldsDataList()) {
						holder.getUser(entry.getKey().toString()).setGroup(holder.getDefaultGroup(), false);
					}
					storage.removeEntry(entry.getKey());
				} catch (Throwable t) {
				}
			}
		}
		groupManager.getWorldsHolder().saveChanges(true);
	}

}
