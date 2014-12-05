package com.shortcircuit.itemcondenser.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.shortcircuit.itemcondenser.ItemCondenser;
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
public class ItemnameCommand extends ShortCommand{
	public ItemnameCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}
	
	@Override
	public String[] getCommandNames() {
		return new String[] {"itemname"};
	}
	
	@Override
	public String getPermissions() {
		return "itemcondenser.items.name";
	}
	
	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Renames an item",
				ChatColor.GREEN + "/${command}",
				ChatColor.GREEN + "Use \"/${command} remove\" to reset the item's name"};
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
		String name = "";
		for(String arg : command.getArgs()){
			name += " " + arg;
		}
		name = name.trim();
		ItemStack item = player.getItemInHand();
		if(item.getType() != Material.AIR){
			ItemMeta meta = item.getItemMeta();
			if(name.equalsIgnoreCase("remove")){
				meta = null;
			}
			else{
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			}
			item.setItemMeta(meta);
		}
		return new String[] {};
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
