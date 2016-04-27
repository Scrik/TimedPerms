package timedpermissions.perms;

import java.util.UUID;

import simpleuserperms.SimpleUserPerms;
import simpleuserperms.storage.Group;

public class SimpleUserPermsProvider extends PermissionsProvider {

	@Override
	public String getDefaultGroup() {
		return SimpleUserPerms.getGroupsStorage().getDefaultGroup().getName();
	}

	@Override
	public boolean hasGroup(String groupName) {
		return SimpleUserPerms.getGroupsStorage().getGroup(groupName) != null;
	}

	@Override
	public void setMainGroup(UUID player, String groupName) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group != null) {
			SimpleUserPerms.getUsersStorage().getUser(player).setMainGroup(group);
		}
	}

	@Override
	public void addSubGroup(UUID player, String groupName) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group != null) {
			SimpleUserPerms.getUsersStorage().getUser(player).addSubGroup(group);
		}
	}

	@Override
	public void removeSubGroup(UUID player, String groupName) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group != null) {
			SimpleUserPerms.getUsersStorage().getUser(player).removeSubGroup(group);
		}
	}

	@Override
	public void addPermission(UUID player, String permission) {
		SimpleUserPerms.getUsersStorage().getUser(player).addAdditionalPermission(permission);
	}

	@Override
	public void removePermission(UUID player, String permission) {
		SimpleUserPerms.getUsersStorage().getUser(player).removeAdditionalPermission(permission);
	}

}
