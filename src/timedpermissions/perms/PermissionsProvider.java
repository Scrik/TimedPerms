package timedpermissions.perms;

import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import simpleuserperms.SimpleUserPerms;

public abstract class PermissionsProvider {

	private static PermissionsProvider provider = new VoidProvider();

	public static PermissionsProvider getInstance() {
		return provider;
	}

	public static void detectInstalledPermissionProvider() {
		try {
			JavaPlugin.getPlugin(SimpleUserPerms.class);
			provider = new SimpleUserPermsProvider();
		} catch (Throwable t) {
		}
	}

	public abstract String getDefaultGroup();

	public abstract boolean hasGroup(String string);

	public abstract void setMainGroup(UUID player, String group);

	public abstract void addSubGroup(UUID player, String group);

	public abstract void removeSubGroup(UUID player, String group);

	public abstract void addPermission(UUID player, String permission);

	public abstract void removePermission(UUID player, String permission);

}
