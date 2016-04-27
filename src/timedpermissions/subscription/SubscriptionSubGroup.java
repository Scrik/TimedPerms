package timedpermissions.subscription;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import timedpermissions.perms.PermissionsProvider;

public class SubscriptionSubGroup extends Subscription {

	private String groupName;

	public SubscriptionSubGroup(long expireTime, UUID playerUUID, String group) {
		super(expireTime, playerUUID);
		this.groupName = group;
	}

	protected SubscriptionSubGroup() {
	}

	@Override
	public String validate() {
		if (!PermissionsProvider.getInstance().hasGroup(groupName)) {
			return "Group " + groupName + " doesn't exist";
		}
		return null;
	}

	@Override
	public void apply() {
		PermissionsProvider.getInstance().addSubGroup(getPlayerUUID(), groupName);
	}

	@Override
	public void expire() {
		PermissionsProvider.getInstance().removeSubGroup(getPlayerUUID(), groupName);
	}

	@Override
	public String getInfoString() {
		return "Sub group " + groupName;
	}

	@Override
	protected void saveData(ConfigurationSection section) {
		section.set("group", groupName);
	}

	@Override
	protected void loadData(ConfigurationSection section) {
		groupName = section.getString("group", "unknown");
	}

}
