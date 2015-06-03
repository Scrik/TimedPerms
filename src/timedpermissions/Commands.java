package timedpermissions;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Commands implements CommandExecutor {

	private final Storage storage;
	public Commands(Storage storage) {
		this.storage = storage;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("timedperms.admin")) {
			sender.sendMessage(ChatColor.RED + "No perms");
			return true;
		}
		try {
			GroupManager groupManager = JavaPlugin.getPlugin(GroupManager.class);
			OverloadedWorldHolder holder = groupManager.getWorldsHolder().getDefaultWorld();
			switch (args[0].toLowerCase()) {
				case "help": {
					sender.sendMessage(ChatColor.YELLOW + "/timedperms set {username} {group} {days}");
					sender.sendMessage(ChatColor.YELLOW + "/timedperms remove {username}");
					sender.sendMessage(ChatColor.YELLOW + "/timedperms extendall {days}");
					sender.sendMessage(ChatColor.YELLOW + "/timedperms extend {username} {days}");
					sender.sendMessage(ChatColor.YELLOW + "/timedperms list");
					sender.sendMessage(ChatColor.YELLOW + "/timedperms info {username}");
					return true;
				}
				case "set": {
					String username = args[1];
					OfflinePlayer player = Bukkit.getOfflinePlayer(username);
					if (!player.isOnline() && !player.hasPlayedBefore()) {
						sender.sendMessage(ChatColor.RED + "Player " + username + " never played on server before");
						return true;
					}
					String group = args[2].toLowerCase();
					if (!holder.getGroups().containsKey(group)) {
						sender.sendMessage(ChatColor.RED + "Group " + group + " doesn't exist");
						return true;
					}
					String time = args[3];
					try {
						int days = Integer.parseInt(time);
						storage.addEntry(player.getUniqueId(), TimeUnit.DAYS.toMillis(days));
						User user = holder.getUser(player.getUniqueId().toString());
						user.setLastName(username);
						user.setGroup(holder.getGroup(group), player.isOnline());
						groupManager.getWorldsHolder().saveChanges(true);
						sender.sendMessage(ChatColor.YELLOW + "Changed " + username + " group to " + group + " for " + days + " days");
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + time + " is not a valid number");
					}
					return true;
				}
				case "remove": {
					String username = args[1];
					OfflinePlayer player = Bukkit.getOfflinePlayer(username);
					storage.removeEntry(player.getUniqueId());
					if (holder.isUserDeclared(player.getUniqueId().toString())) {
						User user = holder.getUser(player.getUniqueId().toString());
						user.setGroup(holder.getDefaultGroup());
						groupManager.getWorldsHolder().saveChanges(true);
						sender.sendMessage(ChatColor.YELLOW + "Changed player " + username + " group to default");
					} else {
						sender.sendMessage(ChatColor.RED + "Player doesn't exist");
					}
					return true;
				}
				case "extendall": {
					String time = args[1];
					try {
						int days = Integer.parseInt(time);
						Iterator<Entry<UUID, Long>> iterator = storage.directIterator();
						while (iterator.hasNext()) {
							Entry<UUID, Long> entry = iterator.next();
							entry.setValue(entry.getValue() + TimeUnit.DAYS.toMillis(days));
						}
						sender.sendMessage(ChatColor.YELLOW + "All subscriptions have been extended for " + days + " days");
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + time + " is not a valid number");
					}
					return true;
				}
				case "extend": {
					String username = args[1];
					String time = args[2];
					OfflinePlayer player = Bukkit.getOfflinePlayer(username);
					if (!player.isOnline() && !player.hasPlayedBefore()) {
						sender.sendMessage(ChatColor.RED + "Player " + username + " never played on server before");
						return true;
					}
					try {
						int days = Integer.parseInt(time);
						boolean has = storage.modify(player.getUniqueId(), (uuid, etime) -> etime + TimeUnit.DAYS.toMillis(days));
						if (has) {
							sender.sendMessage(ChatColor.YELLOW + "Subscription for player " + username + " has been extended for " + days + " days");
						} else {
							sender.sendMessage(ChatColor.RED + "Player " + username + " doesn't have a subscription");
						}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + time + " is not a valid number");
					}
					return true;
				}
				case "list": {
					long ctime = System.currentTimeMillis();
					Iterator<Entry<UUID, Long>> iterator = storage.directIterator();
					while (iterator.hasNext()) {
						Entry<UUID, Long> entry = iterator.next();
						sender.sendMessage(ChatColor.YELLOW + Bukkit.getOfflinePlayer(entry.getKey()).getName() + " has " + TimeUnit.MILLISECONDS.toDays(entry.getValue() - ctime) + " days left");
					}
					return true;
				}
				case "info": {
					String username = args[1];
					long expirytime = storage.getValue(Bukkit.getOfflinePlayer(username).getUniqueId());
					if (expirytime == -1) {
						sender.sendMessage(ChatColor.RED + "Player " + username + " doesn't have a subscription");
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Player " + username + " has " + TimeUnit.MILLISECONDS.toDays(expirytime - System.currentTimeMillis()) + " days remaining");
					}
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			sender.sendMessage(ChatColor.RED + "Not enough args");
			return true;
		}
		return false;
	}

}