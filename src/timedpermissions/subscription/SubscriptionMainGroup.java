package timedpermissions.subscription;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import timedpermissions.perms.PermissionsProvider;

public class SubscriptionMainGroup extends Subscription {

	private String groupName;

	public SubscriptionMainGroup(long expireTime, UUID playerUUID, String group) {
		super(expireTime, playerUUID);
		this.groupName = group;
	}

	protected SubscriptionMainGroup() {
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
		PermissionsProvider.getInstance().setMainGroup(getPlayerUUID(), groupName);
	}

	@Override
	public void expire() {
		PermissionsProvider.getInstance().setMainGroup(getPlayerUUID(), PermissionsProvider.getInstance().getDefaultGroup());
	}

	@Override
	protected void saveData(ConfigurationSection section) {
		section.set("group", groupName);
	}

	@Override
	protected void loadData(ConfigurationSection section) {
		groupName = section.getString("group");
	}

	public String getGroupName() {
		return groupName;
	}

	@Override
	public String getInfoString() {
		return "Main group " +groupName;
	}

}
