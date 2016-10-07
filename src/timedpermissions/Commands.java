package timedpermissions;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import timedpermissions.commands.SubCommand;
import timedpermissions.commands.SubCommandAddSubGroup;
import timedpermissions.commands.SubCommandExtendAll;
import timedpermissions.commands.SubCommandHelp;
import timedpermissions.commands.SubCommandList;
import timedpermissions.commands.SubCommandRemove;
import timedpermissions.commands.SubCommandSetMainGroup;
import timedpermissions.subscription.Storage;

public class Commands implements CommandExecutor {

	public Commands(Storage storage) {
		register("help", new SubCommandHelp());
		register("setmaingroup", new SubCommandSetMainGroup(storage));
		register("addgroup", new SubCommandAddSubGroup(storage));
		register("extendall", new SubCommandExtendAll(storage));
		register("remove", new SubCommandRemove(storage));
		register("list", new SubCommandList(storage));
	}

	private final HashMap<String, SubCommand> subcommands = new HashMap<>();

	private void register(String command, SubCommand subinst) {
		subcommands.put(command, subinst);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("timedperms.help")) {
			sender.sendMessage(ChatColor.RED + "You have no power here");
			return true;
		}
		SubCommand subcommand = subcommands.get(args[0]);
		if (subcommand != null) {
			subcommand.handle(sender, Arrays.copyOfRange(args, 1, args.length));
			return true;
		}
		return false;
	}

}
