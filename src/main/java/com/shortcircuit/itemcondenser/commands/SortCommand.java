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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.yi.acru.bukkit.Lockette.Lockette;

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
		// Get the player's inventory
		Inventory inv = player.getInventory();
		if(command.getArgs().length >= 1){
			if(command.getArg(0).equalsIgnoreCase("chest")){
				Block block = player.getTargetBlock(null, 5);
				boolean is_private = false;
				if(plugin.usingLockette()){
					is_private = Lockette.isProtected(block);
				}
				if(block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)){
					if(is_private){
						if(!Lockette.isOwner(block, player.getName()) && !Lockette.isUser(block, player.getName(), true)
								&& !Lockette.isEveryone(block)){
							return new String[] {ChatColor.RED + "That chest is private!"};
						}
					}
					Chest chest = (Chest)block.getState();
					inv = chest.getInventory();
				}
				else if(block.getType().equals(Material.ENDER_CHEST)){
					inv = player.getEnderChest();
				}
				else{
					return new String[] {ChatColor.RED + "No chest to sort"};
				}
			}
			else{
				throw new InvalidArgumentException(command.getCommandLabel(), command.getArg(0));
			}
		}
		Inventory new_inv = Bukkit.createInventory(player, 54);
		for(Material material : Material.values()){
			for(ItemStack item : inv.getContents()){
				if(item != null){
					if(item.getType().equals(material)){
						inv.removeItem(item);
						new_inv.addItem(item);
					}
				}
			}
		}
		for(ItemStack item : new_inv.getContents()){
			if(item != null){
				new_inv.removeItem(item);
				inv.addItem(item);
			}
		}
		return new String[] {ChatColor.GREEN + "Inventory sorted"};
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
