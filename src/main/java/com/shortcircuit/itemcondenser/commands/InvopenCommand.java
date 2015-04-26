package com.shortcircuit.itemcondenser.commands;

import com.shortcircuit.itemcondenser.EntityMetadata;
import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.itemcondenser.configuration.InventoryHandler;
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
public class InvopenCommand extends ShortCommand{
	private ItemCondenser plugin;
	private InventoryHandler inventory_manager;
	public InvopenCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.plugin = owning_plugin;
		this.inventory_manager = owning_plugin.getInventoryHandler();
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[] {"invopen"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.inventories.open";
	}

	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Opens an additional inventory",
				ChatColor.GREEN + "/${command} <InventoryName>"};
	}

	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		if(command.getArgs().length < 1) {
			throw new TooFewArgumentsException(command.getCommandLabel());
		}
		Player player = (Player)command.getSender();
		if(inventory_manager.hasInventory(player, command.getArg(0))){
			Inventory inv = inventory_manager.loadInventory(player, command.getArg(0));
			player.openInventory(inv);
			player.setMetadata("invIsOpen", new EntityMetadata(plugin, true));
		}
		else{
			return new String[] {ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
					+ " You do not have an inventory named " + ChatColor.LIGHT_PURPLE + command.getArg(0)};
		}
		return new String[] {};
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
