package timedpermissions.commands;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import timedpermissions.subscription.Storage;
import timedpermissions.subscription.Subscription;

public class SubCommandRemove implements SubCommand {

	private final Storage storage;
	public SubCommandRemove(Storage storage) {
		this.storage = storage;
	}

	@Override
	public void handle(CommandSender sender, String[] args) {
		String id = args[0];
		for (Subscription sub : storage.getSubscriptions()) {
			if (sub.getTempUID().equals(id)) {
				sub.expire();
				storage.removeSubscription(sub);
				sender.sendMessage(ChatColor.YELLOW + MessageFormat.format("Removed subscription with temp uid {0}", id));
				break;
			}
		}
	}

}
