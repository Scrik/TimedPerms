package timedpermissions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SubCommandHelp implements SubCommand {

	@Override
	public void handle(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.YELLOW + "timedperms setmaingroup {username} {group} {days}");
		sender.sendMessage(ChatColor.YELLOW + "timedperms addgroup {username} {group} {days}");
		sender.sendMessage(ChatColor.YELLOW + "timedperms extendall {days}");
		sender.sendMessage(ChatColor.YELLOW + "timedperms remove {tempuid}");
		sender.sendMessage(ChatColor.YELLOW + "timedperms list");
	}

}
