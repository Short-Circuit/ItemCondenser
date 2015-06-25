package com.shortcircuit.itemcondenser.commands;

import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.itemcondenser.utils.Utils;
import com.shortcircuit.shortcommands.command.CommandType;
import com.shortcircuit.shortcommands.command.CommandWrapper;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.exceptions.PlayerOnlyException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ShortCircuit908
 */
public class CondenseCommand extends ShortCommand {
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
		return new String[]{"condense"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.condense";
	}

	@Override
	public String[] getHelp() {
		return new String[]{
				ChatColor.GREEN + "Condenses all materials in the inventory into blocks",
				ChatColor.GREEN + "/${condense}"};
	}

	@SuppressWarnings("deprecation")
	@Override
	public String[] exec(CommandWrapper command) throws PlayerOnlyException {
		if (!(command.getSender() instanceof Player)) {
			throw new PlayerOnlyException();
		}
		Player player = (Player) command.getSender();
		Inventory inventory = Utils.getTargetInventory(player, 5);
		if (inventory == null) {
			inventory = player.getInventory();
		}
		ArrayList<ItemStack> contents = new ArrayList<>(inventory.getSize());
		do{
			for(ItemStack item : inventory.getContents()){
				contents.add(item);
			}
			inventory.clear();
			Utils.sortItems(contents);
			for(ItemStack item : contents){
				if(item != null) {
					inventory.addItem(item);
				}
			}
			contents.clear();
		}
		while (condenseInventory(player, inventory));
		return new String[]{ChatColor.GREEN + "Inventory condensed"};
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}

	private double getConversionRate(Material type) {
		switch (type) {
			/*
			case IRON_BLOCK:
			case GOLD_BLOCK:
			case DIAMOND_BLOCK:
			case EMERALD_BLOCK:
			case REDSTONE_BLOCK:
			case COAL_BLOCK:
				return 9;
				*/
			case IRON_INGOT:
			case GOLD_INGOT:
			case DIAMOND:
			case EMERALD:
			case REDSTONE:
			case COAL:
				return 1.0 / 9.0;
			/*
			case GLOWSTONE:
			case CLAY:
			case SNOW_BLOCK:
				return 4;
				*/
			case GLOWSTONE_DUST:
			case CLAY_BALL:
			case SNOW_BALL:
				return 0.25;
			default:
				return 1;
		}
	}

	private Material getConversionType(Material type) {
		switch (type) {
			case IRON_INGOT:
				return Material.IRON_BLOCK;
			case GOLD_INGOT:
				return Material.GOLD_BLOCK;
			case DIAMOND:
				return Material.DIAMOND_BLOCK;
			case EMERALD:
				return Material.EMERALD_BLOCK;
			case REDSTONE:
				return Material.REDSTONE_BLOCK;
			case COAL:
				return Material.COAL_BLOCK;
			case GLOWSTONE_DUST:
				return Material.GLOWSTONE;
			case CLAY_BALL:
				return Material.CLAY;
			case SNOW_BALL:
				return Material.SNOW_BLOCK;
			/*
			case IRON_BLOCK:
				return Material.IRON_INGOT;
			case GOLD_BLOCK:
				return Material.GOLD_INGOT;
			case DIAMOND_BLOCK:
				return Material.DIAMOND;
			case EMERALD_BLOCK:
				return Material.EMERALD;
			case REDSTONE_BLOCK:
				return Material.REDSTONE;
			case COAL_BLOCK:
				return Material.COAL;
			case GLOWSTONE:
				return Material.GLOWSTONE_DUST;
			case CLAY:
				return Material.CLAY_BALL;
			case SNOW_BLOCK:
				return Material.SNOW_BALL;
				*/
			default:
				return type;
		}
	}

	private boolean hasRoomForItem(Inventory inventory, ItemStack item) {
		int space_available = 0;
		for (ItemStack compare : inventory.getContents()) {
			if (item.isSimilar(compare)) {
				space_available += (compare.getMaxStackSize() - compare.getAmount());
			}
		}
		return space_available >= item.getAmount();
	}

	private boolean condenseInventory(Entity entity, Inventory inventory){
		boolean condensed = false;
		ArrayList<ItemStack> overflow = new ArrayList<>();
		ArrayList<ItemStack> to_remove = new ArrayList<>(inventory.getSize());
		for (ItemStack item : inventory.getContents()) {
			if(item == null){
				continue;
			}
			double conversion = getConversionRate(item.getType());
			if(conversion == 1){
				continue;
			}
			int amount = item.getAmount();
			int new_item_amount = (int) (amount * conversion);
			if(new_item_amount == 0){
				continue;
			}
			condensed = true;
			int old_item_amount = (int) (amount % (1.0 / conversion));
			ItemStack new_item = new ItemStack(getConversionType(item.getType()), new_item_amount, item.getDurability());
			new_item.setItemMeta(item.getItemMeta().clone());
			overflow.addAll(inventory.addItem(new_item).values());
			if (old_item_amount <= 0) {
				to_remove.add(item);
			}
			else {
				item.setAmount(old_item_amount);
			}
		}
		for (ItemStack item : to_remove) {
			inventory.removeItem(item);
		}
		Iterator<ItemStack> overflow_iter = overflow.iterator();
		while (overflow_iter.hasNext()) {
			ItemStack item = overflow_iter.next();
			if (!hasRoomForItem(inventory, item)) {
				entity.getWorld().dropItem(entity.getLocation(), item).setVelocity(new Vector(0, 0, 0));
			}
			else {
				inventory.addItem(item);
				overflow_iter.remove();
			}
		}
		return condensed;
	}
}
