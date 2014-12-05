package com.shortcircuit.itemcondenser.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
public class InvlistCommand extends ShortCommand{
	private InventoryHandler inventory_manager;
	public InvlistCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.inventory_manager = owning_plugin.getInventoryHandler();
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
		Set<String> inventories = inventory_manager.getInventories(player);
		if(inventories.size() > 0){
			String[] messages = new String[inventories.size() + 1];
			messages[0] = ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN + " Your inventories:";
			for(int i = 0; i < inventories.size(); i++){
				messages[i + 1] = ChatColor.LIGHT_PURPLE + "[" + (i + 1) + "] " + ChatColor.GREEN
						+ inventories.toArray()[i];
			}
			return messages;
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
