package com.shortcircuit.itemcondenser.commands;

import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.itemcondenser.inventories.InventoryManager;
import com.shortcircuit.itemcondenser.inventories.ItemWrapper;
import com.shortcircuit.itemcondenser.inventories.MultiInventory;
import com.shortcircuit.shortcommands.command.CommandType;
import com.shortcircuit.shortcommands.command.CommandWrapper;
import com.shortcircuit.shortcommands.command.PermissionComparator;
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

/**
 * @author ShortCircuit908
 */
public class InvcreateCommand extends ShortCommand {
	private ItemCondenser plugin;
	private InventoryManager inventory_manager;

	public InvcreateCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.plugin = owning_plugin;
		this.inventory_manager = owning_plugin.getInventoryManager();
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[]{"invcreate"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.inventories.create";
	}

	@Override
	public String[] getHelp() {
		return new String[]{
				ChatColor.GREEN + "Creates an additional inventory",
				ChatColor.GREEN + "/${command} <inventoryName>"};
	}

	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		if (command.getArgs().length < 1) {
			throw new TooFewArgumentsException(command.getCommandLabel());
		}
		Player player = (Player) command.getSender();
		if(inventory_manager.hasInventory(player.getUniqueId(), command.getArg(0))) {
			return new String[]{ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
					+ " You already have an inventory named " + ChatColor.LIGHT_PURPLE + command.getArg(0)};
		}
		MultiInventory inventories = inventory_manager.getInventories(player.getUniqueId());
		if(inventories.getInventoryCount() < plugin.getConfig().getInt("Inventories.MaximumPerPlayer")
				&& !PermissionComparator.hasWildcardPermission(player, "itemcondenser.inventories.create.infinite")){
			return new String[]{ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
					+ " You have reached your maximum inventory count"};
		}
		inventories.createOrUpdateInventory(command.getArg(0), new ItemWrapper[36]);
		inventory_manager.saveInventories(inventories);
		player.openInventory(inventory_manager.loadInventory(player, command.getArg(0)));
		return new String[]{};
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
