package timedpermissions.commands;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import timedpermissions.subscription.Storage;
import timedpermissions.subscription.Subscription;

public class SubCommandExtendAll implements SubCommand {

	private final Storage storage;
	public SubCommandExtendAll(Storage storage) {
		this.storage = storage;
	}

	@Override
	public void handle(CommandSender sender, String[] args) {
		String time = args[0];
		try {
			int days = Integer.parseInt(time);
			for (Subscription sub : storage.getSubscriptions()) {
				sub.modifyExpireTime(TimeUnit.DAYS.toMillis(days));
			}
			sender.sendMessage(ChatColor.YELLOW + MessageFormat.format("All subscriptions have been extended for {0} days", days));
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + MessageFormat.format("{0} is not a valid number", time));
		}
	}

}
