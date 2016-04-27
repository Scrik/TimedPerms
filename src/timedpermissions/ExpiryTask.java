package timedpermissions;

import org.bukkit.scheduler.BukkitRunnable;

import timedpermissions.subscription.Storage;
import timedpermissions.subscription.Subscription;

public class ExpiryTask extends BukkitRunnable {

	private final Storage storage;
	public ExpiryTask(Storage storage) {
		this.storage = storage;
	}

	@Override
	public void run() {
		storage
		.getSubscriptions()
		.stream()
		.filter(Subscription::isExpired)
		.forEach(entry -> {
			entry.expire();
			storage.removeSubscription(entry);
		});
	}

}
