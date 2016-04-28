package timedpermissions;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import timedpermissions.subscription.Storage;
import timedpermissions.subscription.Subscription;
import timedpermissions.subscription.SubscriptionMainGroup;
import timedpermissions.subscription.SubscriptionPermission;
import timedpermissions.subscription.SubscriptionSubGroup;

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
			switch (args[0].toLowerCase()) {
				case "help": {
					sender.sendMessage(ChatColor.YELLOW + label + " set {username} {group} {days}");
					sender.sendMessage(ChatColor.YELLOW + label + " addgroup {username} {group} {days}");
					sender.sendMessage(ChatColor.YELLOW + label + " addperm {username} {permission} {days}");
					sender.sendMessage(ChatColor.YELLOW + label + " extendall {days}");
					sender.sendMessage(ChatColor.YELLOW + label + " remove {tempuid}");
					sender.sendMessage(ChatColor.YELLOW + label + " list");
					return true;
				}
				case "set": {
					String username = args[1];
					OfflinePlayer player = Bukkit.getOfflinePlayer(username);
					if (!player.isOnline() && !player.hasPlayedBefore()) {
						sender.sendMessage(ChatColor.RED + "Player " + username + " never played on server before");
						return true;
					}
					String groupName = args[2];
					String time = args[3];
					try {
						int days = Integer.parseInt(time);
						Subscription sub = new SubscriptionMainGroup(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days), player.getUniqueId(), groupName);
						String errMsg = sub.validate();
						if (errMsg != null) {
							sender.sendMessage(ChatColor.RED + "Error: " + errMsg);
						} else {
							storage.addSubscription(sub);
							sub.apply();
							sender.sendMessage(ChatColor.YELLOW + "Changed " + username + " main group to " + groupName + " for " + days + " days");
						}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + time + " is not a valid number");
					}
					return true;
				}
				case "addgroup": {
					String username = args[1];
					OfflinePlayer player = Bukkit.getOfflinePlayer(username);
					if (!player.isOnline() && !player.hasPlayedBefore()) {
						sender.sendMessage(ChatColor.RED + "Player " + username + " never played on server before");
						return true;
					}
					String groupName = args[2];
					String time = args[3];
					try {
						int days = Integer.parseInt(time);
						Subscription sub = new SubscriptionSubGroup(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days), player.getUniqueId(), groupName);
						String errMsg = sub.validate();
						if (errMsg != null) {
							sender.sendMessage(ChatColor.RED + "Error: " + errMsg);
						} else {
							storage.addSubscription(sub);
							sub.apply();
							sender.sendMessage(ChatColor.YELLOW + "Added sub group " + groupName + " to " + username + " for " + days + " days");
						}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + time + " is not a valid number");
					}
					return true;
				}
				case "addperm": {
					String username = args[1];
					OfflinePlayer player = Bukkit.getOfflinePlayer(username);
					if (!player.isOnline() && !player.hasPlayedBefore()) {
						sender.sendMessage(ChatColor.RED + "Player " + username + " never played on server before");
						return true;
					}
					String permName = args[2];
					String time = args[3];
					try {
						int days = Integer.parseInt(time);
						Subscription sub = new SubscriptionPermission(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days), player.getUniqueId(), permName);
						String errMsg = sub.validate();
						if (errMsg != null) {
							sender.sendMessage(ChatColor.RED + "Error: " + errMsg);
						} else {
							storage.addSubscription(sub);
							sub.apply();
							sender.sendMessage(ChatColor.YELLOW + "Added permission " + permName + " to " + username + " for " + days + " days");
						}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + time + " is not a valid number");
					}
					return true;
				}
				case "extendall": {
					String time = args[1];
					try {
						int days = Integer.parseInt(time);
						for (Subscription sub : storage.getSubscriptions()) {
							sub.modifyExpireTime(TimeUnit.DAYS.toMillis(days));
						}
						sender.sendMessage(ChatColor.YELLOW + "All subscriptions have been extended for " + days + " days");
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + time + " is not a valid number");
					}
					return true;
				}
				case "remove": {
					String id = args[1];
					for (Subscription sub : storage.getSubscriptions()) {
						if (sub.getTempUID().equals(id)) {
							sub.expire();
							storage.removeSubscription(sub);
							sender.sendMessage(ChatColor.YELLOW + "Removed subscription with temp uid " + id);
							break;
						}
					}
					return true;
				}
				case "list": {
					long ctime = System.currentTimeMillis();
					for (Subscription sub : storage.getSubscriptions()) {
						sender.sendMessage(
							ChatColor.YELLOW +
							sub.getTempUID() +
							") " +
							Bukkit.getOfflinePlayer(sub.getPlayerUUID()).getName() +
							" has " +
							TimeUnit.MILLISECONDS.toDays(sub.getExpireTime() - ctime) +
							" days left: "+sub.getInfoString()
						);
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
