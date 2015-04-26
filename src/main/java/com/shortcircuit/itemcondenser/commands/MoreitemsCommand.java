package com.shortcircuit.itemcondenser.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
public class MoreitemsCommand extends ShortCommand{
	public MoreitemsCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[] {"more", "moreitems"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.items.more";
	}

	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Gives a full stack of an item",
				ChatColor.GREEN + "/${command}"};
	}

	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		Player player = (Player)command.getSender();
		ItemStack item = player.getItemInHand();
		if(item.getType() != Material.AIR){
			item.setAmount(64);
		}
		return new String[] {};
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
