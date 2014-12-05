package com.shortcircuit.itemcondenser.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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

/**
 * @author ShortCircuit908
 * 
 */
public class InvcreateCommand extends ShortCommand{
	private ItemCondenser plugin;
	private InventoryHandler inventory_manager;
	public InvcreateCommand(ItemCondenser owning_plugin) {
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
		return new String[] {"invcreate"};
	}
	
	@Override
	public String getPermissions() {
		return "itemcondenser.inventories.create";
	}
	
	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Creates an additional inventory",
				ChatColor.GREEN + "/${command} <inventoryName>"};
	}
	
	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		if(command.getArgs().length < 1) {
			throw new TooFewArgumentsException();
		}
		Player player = (Player)command.getSender();
		if(inventory_manager.getInventories(player).size() < plugin.getConfig().getInt("Inventories.MaximumPerPlayer")
				|| player.hasPermission("itemcondenser.inventories.create.infinite")){
			if(!inventory_manager.hasInventory(player, command.getArg(0))){
				player.openInventory(Bukkit.createInventory(player, 36, command.getArg(0)));
				player.setMetadata("invIsOpen", new EntityMetadata(plugin, true));
			}
			else{
				return new String[] {ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
						+ " You already have an inventory named " + ChatColor.LIGHT_PURPLE + command.getArg(0)};
			}
		}
		else{
			return new String[] {ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
					+ " You have reached your maximum inventory count"};
		}
		return new String[] {};
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
