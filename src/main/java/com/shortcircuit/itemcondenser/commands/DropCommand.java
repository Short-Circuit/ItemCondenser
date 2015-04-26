package com.shortcircuit.itemcondenser.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
public class DropCommand extends ShortCommand{
	public DropCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[] {"drop"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.items.drop";
	}

	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Drops the item in hand at the targeted location",
				ChatColor.GREEN + "/${command}"};
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		Player player = (Player)command.getSender();
		Inventory inv = player.getInventory();
		ItemStack item = player.getItemInHand();
		// Make sure the player is holding and item
		if(item.getType() != Material.AIR){
			// Drop the item at the targeted location
			inv.removeItem(item);
			Location lookAt = player.getTargetBlock(null, 32).getLocation().add(0.5, 1.5, 0.5);
			player.getWorld().dropItem(lookAt, item);
		}
		return new String[] {};
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
