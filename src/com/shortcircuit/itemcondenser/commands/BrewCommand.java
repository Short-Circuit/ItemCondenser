package com.shortcircuit.itemcondenser.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import com.shortcircuit.itemcondenser.EntityMetadata;
import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.itemcondenser.utilities.UtilityBlock;
import com.shortcircuit.itemcondenser.utilities.UtilityManager;
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
public class BrewCommand extends ShortCommand{
	private ItemCondenser plugin;
	private UtilityManager utility_manager;
	public BrewCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.plugin = owning_plugin;
		this.utility_manager = owning_plugin.getUtilityManager();
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}
	
	@Override
	public String[] getCommandNames() {
		return new String[] {"brew"};
	}
	
	@Override
	public String getPermissions() {
		return "itemcondenser.utility.brew";
	}
	
	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Opens a portable brewing stand",
				ChatColor.GREEN + "/${command}"};
	}
	
	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		Player player = (Player)command.getSender();
		if(utility_manager.hasUtility(player.getName())){
			// Get the player's utility
			UtilityBlock utility = utility_manager.getUtility(player.getName());
			if(utility.getUtilityType().equals(InventoryType.BREWING)){
				// Open the inventory
				Block block = utility.getBlock();
				block.setMetadata("isUtility", new EntityMetadata(plugin, true));
				BrewingStand brew = (BrewingStand)block.getState();
				player.openInventory(brew.getInventory());
			}
			else{
				return new String[] {ChatColor.RED + "You already have a " + utility.getUtilityType()
						+ " utility open"};
			}
		}
		else{
			// Get the block at y=1 below the player
			Location location = new Location(player.getWorld(), player.getLocation().getBlockX(), 1,
					player.getLocation().getBlockZ());
			Block block = player.getWorld().getBlockAt(location);
			// Make sure that the block isn't someone else's utility
			while(block.hasMetadata("isUtility")){
				location = location.add(0, 0, 1);
				block = player.getWorld().getBlockAt(location);
			}
			// Add the utility
			block.setMetadata("isUtility", new EntityMetadata(plugin, true));
			utility_manager.addUtility(player.getName(), block, InventoryType.BREWING);
			block.setType(Material.BREWING_STAND);
			// Open the inventory
			BrewingStand brew = (BrewingStand)block.getState();
			player.openInventory(brew.getInventory());
		}
		return new String[] {};
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
