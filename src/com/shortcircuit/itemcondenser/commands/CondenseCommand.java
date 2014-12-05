package com.shortcircuit.itemcondenser.commands;

import org.bukkit.Bukkit;
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
public class CondenseCommand extends ShortCommand{
	private ItemCondenser plugin;
	public CondenseCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
		this.plugin = owning_plugin;
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}
	
	@Override
	public String[] getCommandNames() {
		return new String[] {"condense"};
	}
	
	@Override
	public String getPermissions() {
		return "itemcondenser.condense";
	}
	
	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Condenses all materials in the inventory into blocks",
				ChatColor.GREEN + "/${condense}"};
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		Player player = (Player)command.getSender();
		Inventory inv = player.getInventory();
		if(command.getArgs().length >= 1){
			if(command.getArg(0).equalsIgnoreCase("chest")){
				Block block = player.getTargetBlock(null, 5);
				boolean isPrivate = false;
				if(plugin.usingLockette()){
					isPrivate = Lockette.isProtected(block);
				}
				if(block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)){
					if(isPrivate){
						if(!Lockette.isOwner(block, player.getName()) && !Lockette.isUser(block,
								player.getName(), true)){
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
					return new String[] {ChatColor.RED + "No chest to condense"};
				}
			}
			else{
				throw new InvalidArgumentException(command.getArg(0));
			}
			return new String[] {};
		}
		Inventory secondaryInv = Bukkit.createInventory(player, 36);
		for(Material material : Material.values()){
			for(ItemStack item : inv.getContents()){
				if(item != null){
					if(item.getType().equals(material)){
						inv.removeItem(item);
						secondaryInv.addItem(item);
					}
				}
			}
		}
		ItemStack[] items = secondaryInv.getContents();
		for(ItemStack item : items){
			if(secondaryInv.firstEmpty() != -1){
				if(item != null){
					int amount = item.getAmount();
					if(amount >= 9){
						boolean condensed = false;
						switch(item.getType()){
						case GOLD_NUGGET:
							secondaryInv.addItem(new ItemStack(Material.GOLD_INGOT, (int)item.getAmount() / 9));
							condensed = true;
							break;
						case EMERALD:
							secondaryInv.addItem(new ItemStack(Material.EMERALD_BLOCK, (int)item.getAmount() / 9));
							condensed = true;
							break;
						case IRON_INGOT:
							secondaryInv.addItem(new ItemStack(Material.IRON_BLOCK, (int)item.getAmount() / 9));
							condensed = true;
							break;
						case COAL:
							secondaryInv.addItem(new ItemStack(Material.COAL_BLOCK, (int)item.getAmount() / 9));
							condensed = true;
							break;
						case DIAMOND:
							secondaryInv.addItem(new ItemStack(Material.DIAMOND_BLOCK, (int)item.getAmount() / 9));
							condensed = true;
							break;
						case WHEAT:
							secondaryInv.addItem(new ItemStack(Material.HAY_BLOCK, (int)item.getAmount() / 9));
							condensed = true;
							break;
						case REDSTONE:
							secondaryInv.addItem(new ItemStack(Material.REDSTONE_BLOCK, (int)item.getAmount() / 9));
							condensed = true;
							break;
						case GOLD_INGOT:
							secondaryInv.addItem(new ItemStack(Material.GOLD_BLOCK, (int)item.getAmount() / 9));
							condensed = true;
							break;
						case INK_SACK:
							if(item.getDurability() == (short) 4) {
								secondaryInv.addItem(new ItemStack(Material.LAPIS_BLOCK, (int)item.getAmount() / 9));
								condensed = true;
								break;
							}
						default:
							break;
						}
						if(condensed){
							if(amount == 9 || amount % 9 == 0){
								secondaryInv.removeItem(item);
							}
							else{
								item.setAmount(amount % 9);
							}
						}
					}
					else if(amount >= 4){
						boolean condensed = false;
						switch(item.getType()){
						case CLAY_BALL:
							secondaryInv.addItem(new ItemStack(Material.CLAY, (int)item.getAmount() / 4));
							condensed = true;
							break;
						case SNOW_BALL:
							secondaryInv.addItem(new ItemStack(Material.SNOW_BLOCK, (int)item.getAmount() / 4));
							condensed = true;
							break;
						case GLOWSTONE_DUST:
							secondaryInv.addItem(new ItemStack(Material.GLOWSTONE, (int)item.getAmount() / 4));
							condensed = true;
							break;
						default:
							break;
						}
						if(condensed){
							if(amount % 4 == 0 || amount % 4 == 0){
								secondaryInv.removeItem(item);
							}
							else{
								item.setAmount(amount % 4);
							}
						}
					}
				}
			}
			else{
				return new String[] {ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
						+ " Inventory is full!"};
			}
		}
		for(Material material : Material.values()){
			for(ItemStack item : secondaryInv.getContents()){
				if(item != null){
					if(item.getType().equals(material)){
						secondaryInv.removeItem(item);
						inv.addItem(item);
					}
				}
			}
		}
		return new String[] {};
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
