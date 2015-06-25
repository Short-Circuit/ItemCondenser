package com.shortcircuit.itemcondenser.commands;

import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.itemcondenser.utilities.UtilityManager;
import com.shortcircuit.shortcommands.command.CommandType;
import com.shortcircuit.shortcommands.command.CommandWrapper;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.exceptions.BlockOnlyException;
import com.shortcircuit.shortcommands.exceptions.ConsoleOnlyException;
import com.shortcircuit.shortcommands.exceptions.InvalidArgumentException;
import com.shortcircuit.shortcommands.exceptions.NoPermissionException;
import com.shortcircuit.shortcommands.exceptions.PlayerOnlyException;
import com.shortcircuit.shortcommands.exceptions.TooFewArgumentsException;
import com.shortcircuit.shortcommands.exceptions.TooManyArgumentsException;

import org.bukkit.ChatColor;

/**
 * @author ShortCircuit908
 */
public class RepairCommand extends ShortCommand {
	private ItemCondenser plugin;
	private UtilityManager utility_manager;

	public RepairCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.plugin = owning_plugin;
		this.utility_manager = owning_plugin.getUtilityManager();
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[]{"repair", "anvil"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.utility.repair";
	}

	@Override
	public String[] getHelp() {
		return new String[]{
				ChatColor.GREEN + "Opens a portable anvil",
				ChatColor.GREEN + "/${command}"};
	}

	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		return new String[]{ChatColor.RED + "This command has not been implemented"};
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
