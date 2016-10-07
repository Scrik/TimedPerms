package timedpermissions.commands;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import timedpermissions.subscription.Storage;
import timedpermissions.subscription.Subscription;

public class SubCommandList implements SubCommand {

	private final Storage storage;
	public SubCommandList(Storage storage) {
		this.storage = storage;
	}

	@Override
	public void handle(CommandSender sender, String[] args) {
		long ctime = System.currentTimeMillis();
		for (Subscription sub : storage.getSubscriptions()) {
			sender.sendMessage(ChatColor.YELLOW + MessageFormat.format(
				"{0}) {1} has {2} days left of: {3}",
				sub.getTempUID(), Bukkit.getOfflinePlayer(sub.getPlayerUUID()).getName(),
				TimeUnit.MILLISECONDS.toDays(sub.getExpireTime() - ctime), sub.getInfoString()
			));
		}
	}

}
