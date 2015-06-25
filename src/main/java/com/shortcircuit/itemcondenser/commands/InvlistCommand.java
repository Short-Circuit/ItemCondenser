package com.shortcircuit.itemcondenser.commands;

import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.itemcondenser.inventories.InventoryManager;
import com.shortcircuit.itemcondenser.inventories.MultiInventory;
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

import java.util.ArrayList;

/**
 * @author ShortCircuit908
 * 
 */
public class InvlistCommand extends ShortCommand{
	private InventoryManager inventory_manager;
	public InvlistCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.inventory_manager = owning_plugin.getInventoryManager();
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}
	
	@Override
	public String[] getCommandNames() {
		return new String[] {"invlist", "inventories", "invs"};
	}
	
	@Override
	public String getPermissions() {
		return "itemcondenser.inventories.list";
	}
	
	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Lists all additional inventories",
				ChatColor.GREEN + "/${command}"};
	}
	
	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		Player player = (Player)command.getSender();
		MultiInventory inventories = inventory_manager.getInventories(player.getUniqueId());
		if(inventories.getInventoryCount() > 0){
			ArrayList<String> messages = new ArrayList<>(inventories.getInventoryCount() + 1);
			messages.add(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN + " Your inventories:");
			for(String name : inventories.listInventories()){
				messages.add(ChatColor.GREEN + name);
			}
			return messages.toArray(new String[0]);
		}
		else{
			return new String[] {ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
					+ " You do not have any inventories"};
		}
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
