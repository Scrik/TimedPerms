package timedpermissions.perms;

import java.util.UUID;

public class VoidProvider extends PermissionsProvider {

	@Override
	public String getDefaultGroup() {
		return "default";
	}

	@Override
	public boolean hasGroup(String string) {
		return false;
	}

	@Override
	public void setMainGroup(UUID player, String group) {
	}

	@Override
	public void addSubGroup(UUID player, String group) {
	}

	@Override
	public void removeSubGroup(UUID player, String group) {
	}

	@Override
	public void addPermission(UUID player, String permission) {
	}

	@Override
	public void removePermission(UUID player, String permission) {
	}

}
