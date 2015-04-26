package com.shortcircuit.itemcondenser.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
public class EnchantCommand extends ShortCommand{
	public EnchantCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[] {"enchant", "etable"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.utility.enchant";
	}

	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Opens a portable enchanting table",
				ChatColor.GREEN + "/${command}"};
	}

	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		Player player = (Player)command.getSender();
		player.openEnchanting(player.getLocation(), true);
		return new String[] {};
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
