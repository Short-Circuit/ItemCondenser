package com.shortcircuit.itemcondenser.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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
public class EnderchestCommand extends ShortCommand{
	public EnderchestCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}
	
	@Override
	public String[] getCommandNames() {
		return new String[] {"enderchest", "echest"};
	}
	
	@Override
	public String getPermissions() {
		return "itemcondenser.utility.enderchest";
	}
	
	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Opens a player's ender chest",
				ChatColor.GREEN + "/${command} [player]"};
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		Player player;
		if(command.getArgs().length > 0) {
			player = Bukkit.getServer().getPlayer(command.getArg(0));
			if(player == null) {
				return new String[] {ChatColor.RED + command.getArg(0) + " is not online"};
			}
			
		}
		else {
			player = (Player)command.getSender();
		}
		Inventory ender_chest = player.getEnderChest();
		player.openInventory(ender_chest);
		return new String[] {};
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
