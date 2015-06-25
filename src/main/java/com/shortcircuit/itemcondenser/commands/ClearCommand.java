package com.shortcircuit.itemcondenser.commands;

import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.itemcondenser.utils.Utils;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author ShortCircuit908
 *
 */
public class ClearCommand extends ShortCommand{
	private ItemCondenser plugin;
	public ClearCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.plugin = owning_plugin;
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[] {"clear", "clearinventory", "ci"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.clear";
	}

	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Clears the inventory",
				ChatColor.GREEN + "/${command}"};
	}

	@SuppressWarnings("deprecation")
	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		Player player = (Player)command.getSender();
		Inventory inventory = Utils.getTargetInventory(player, 5);
		if(inventory != null){
			inventory.clear();
			return new String[]{ChatColor.GREEN + "Chest cleared"};
		}
		player.getInventory().clear();
		return new String[] {ChatColor.GREEN + "Inventory cleared"};
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
