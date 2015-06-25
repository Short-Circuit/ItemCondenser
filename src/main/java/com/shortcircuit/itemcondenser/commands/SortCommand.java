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
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * @author ShortCircuit908
 *
 */
public class SortCommand extends ShortCommand{
	private ItemCondenser plugin;
	public SortCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.plugin = owning_plugin;
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[] {"sort"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.sort";
	}

	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Sorts all items in the inventory numerically",
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
		if(inventory == null){
			inventory = player.getInventory();
		}
		ArrayList<ItemStack> contents = new ArrayList<>(inventory.getSize());
		for(ItemStack item : inventory.getContents()){
			if(item != null) {
				contents.add(item);
			}
		}
		inventory.clear();
		Utils.sortItems(contents);
		for(ItemStack item : contents){
			inventory.addItem(item);
		}
		return new String[] {ChatColor.GREEN + "Inventory sorted"};
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
