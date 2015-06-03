package timedpermissions;

import org.bukkit.plugin.java.JavaPlugin;

public class TimedPermissions extends JavaPlugin {

	private static final long INITIAL_DELAY_TICKS = 10 * 60 * 20;
	private static final long INTERVAL_TICKS = INITIAL_DELAY_TICKS;

	private Storage storage;

	@Override
	public void onEnable() {
		storage = new Storage(this);
		storage.load();
		getServer().getScheduler().runTaskTimerAsynchronously(this, new ExpiryTask(storage), INITIAL_DELAY_TICKS, INTERVAL_TICKS);
		getCommand("timedperms").setExecutor(new Commands(storage));
	}

	@Override
	public void onDisable() {
		storage.save();
	}

}