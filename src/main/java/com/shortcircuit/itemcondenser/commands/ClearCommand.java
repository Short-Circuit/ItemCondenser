package com.shortcircuit.itemcondenser.commands;

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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.yi.acru.bukkit.Lockette.Lockette;

/**
 * @author ShortCircuit908
 *
 */
public class ClearCommand extends ShortCommand{
	private ItemCondenser plugin;
	public ClearCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.plugin = owning_plugin;
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[] {"clear", "clearinventory", "ci"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.clear";
	}

	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Clears the inventory",
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
		// Allow the player to target a chest using /clear chest
		if(command.getArgs().length >= 1){
			if(command.getArg(0).equalsIgnoreCase("chest")){
				// Get the targeted block
				Block block = player.getTargetBlock(null, 5);
				boolean is_private = false;
				// If Lockette is installed, check if the block is protected
				if(plugin.usingLockette()){
					is_private = Lockette.isProtected(block);
				}
				// Check if the targeted block is a chest
				if(block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)){
					// If the chest is private, check if the player is an allowed user
					if(is_private){
						if(!Lockette.isOwner(block, player.getName()) && !Lockette.isUser(block, player.getName(), true)
								&& !Lockette.isEveryone(block)){
							// If the player is not an allowed user, tell them to GTFO
							return new String[] {ChatColor.RED + "That chest is private!"};
						}
					}
					// Otherwise, get the inventory of the chest
					Chest chest = (Chest)block.getState();
					inv = chest.getInventory();
				}
				// Check if the targeted block is an ender chest
				else if(block.getType().equals(Material.ENDER_CHEST)){
					inv = player.getEnderChest();
				}
				else{
					return new String[] {ChatColor.RED + "No chest to clear"};
				}
			}
			else{
				throw new InvalidArgumentException(command.getCommandLabel(), command.getArg(0));
			}
		}
		// Clear the inventory
		inv.clear();
		return new String[] {ChatColor.GREEN + "Inventory cleared"};
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
