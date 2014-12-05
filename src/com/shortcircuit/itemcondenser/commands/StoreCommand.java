package com.shortcircuit.itemcondenser.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.yi.acru.bukkit.Lockette.Lockette;

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
public class StoreCommand extends ShortCommand{
	private ItemCondenser plugin;
	public StoreCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.plugin = owning_plugin;
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}
	
	@Override
	public String[] getCommandNames() {
		return new String[] {"store"};
	}
	
	@Override
	public String getPermissions() {
		return "itemcondenser.items.store";
	}
	
	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Stores the held item in a chest",
				ChatColor.GREEN + "/${command}"};
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		Player player = (Player)command.getSender();
		// Get the player's inventory, and the held item
		Inventory inv = player.getInventory();
		ItemStack item = player.getItemInHand();
		// Get the block the player is targeting
		Block block = player.getTargetBlock(null, 32);
		boolean isPrivate = false;
		// If Lockette is installed, check if the block is protected
		if(plugin.usingLockette()){
			isPrivate = Lockette.isProtected(block);
		}
		// Check if the targeted block is a chest
		if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST){
			// If the chest is private, check if the player is an allowed user
			if(isPrivate){
				if(!Lockette.isOwner(block, player.getName()) && !Lockette.isUser(block, player.getName(), true)
						&& !Lockette.isEveryone(block)){
					// If the player is not an allowed user, tell them to GTFO
					return new String[] {ChatColor.RED + "That chest is private!"};
				}
			}
			// Otherwise, get the inventory of the chest
			Chest chest = (Chest)block.getState();
			// Make sure the player is holding an item
			if(item.getType() != Material.AIR){
				// Make sure there is room in the chest
				if(chest.getInventory().firstEmpty() == -1){
					return new String[] {ChatColor.RED + "Chest is full"};
				}
				else{
					// Move an item into the chest
					inv.removeItem(item);
					chest.getInventory().addItem(item);
				}
			}
		}
		// Check if the targeted block is an ender chest
		else if(block.getType() == Material.ENDER_CHEST){
			// Get the player's ender chest
			Inventory enderInv = player.getEnderChest();
			// Make sure the player is holding an item
			if(item != null && item.getType() != Material.AIR){
				// Make sure there is room in the ender chest
				if(enderInv.firstEmpty() == -1){
					return new String[] {ChatColor.RED + "Ender chest is full"};
				}
				else{
					// Move an item into the ender chest
					inv.removeItem(item);
					enderInv.addItem(item);
				}
			}
		}
		else{
			return new String[] {ChatColor.RED + "No chest to store items in"};
		}
		return new String[] {};
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
