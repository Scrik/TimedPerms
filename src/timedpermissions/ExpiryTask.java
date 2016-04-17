package timedpermissions;

import org.bukkit.scheduler.BukkitRunnable;

import simpleuserperms.SimpleUserPerms;
import simpleuserperms.storage.User;

public class ExpiryTask extends BukkitRunnable {

	private final Storage storage;
	public ExpiryTask(Storage storage) {
		this.storage = storage;
	}

	@Override
	public void run() {
		long timeMillis = System.currentTimeMillis();
		storage.getData().entrySet()
		.stream()
		.filter(entry -> timeMillis >= entry.getValue())
		.forEach(entry -> {
			User user = SimpleUserPerms.getUsersStorage().getUserIfPresent(entry.getKey());
			if (user != null) {
				user.setMainGroup(SimpleUserPerms.getGroupsStorage().getDefaultGroup());
			}
			storage.removeEntry(entry.getKey());
		});
	}

}
