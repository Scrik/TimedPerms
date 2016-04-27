package timedpermissions.subscription;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import timedpermissions.perms.PermissionsProvider;

public class SubscriptionPermission extends Subscription {

	private String permission;

	public SubscriptionPermission(long expireTime, UUID playerUUID, String permission) {
		super(expireTime, playerUUID);
		this.permission = permission;
	}

	protected SubscriptionPermission() {
	}

	@Override
	public String validate() {
		return null;
	}

	@Override
	public void apply() {
		PermissionsProvider.getInstance().addPermission(getPlayerUUID(), permission);
	}

	@Override
	public void expire() {
		PermissionsProvider.getInstance().removePermission(getPlayerUUID(), permission);
	}

	@Override
	public String getInfoString() {
		return "Permission " + permission;
	}

	@Override
	protected void saveData(ConfigurationSection section) {
		section.set("permission", permission);
	}

	@Override
	protected void loadData(ConfigurationSection section) {
		permission = section.getString("permission", "unknown");
	}

}
