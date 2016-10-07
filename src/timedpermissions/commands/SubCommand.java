package timedpermissions.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {

	public void handle(CommandSender sender, String[] args);

}
