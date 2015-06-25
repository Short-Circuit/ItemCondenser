package com.shortcircuit.itemcondenser.utils;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.yi.acru.bukkit.Lockette.Lockette;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * @author ShortCircuit908
 */
public class Utils {
	public static final HashSet<Material> TRANSPARENT_BLOCKS = new HashSet<>();

	static {
		for (Material material : Material.values()) {
			if (material.isTransparent()) {
				TRANSPARENT_BLOCKS.add(material);
			}
		}
	}

	public static Inventory getTargetInventory(Player player, int max_distance) {
		Block block = player.getTargetBlock(Utils.TRANSPARENT_BLOCKS, 32);
		boolean is_private = false;
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("Lockette")) {
			is_private = Lockette.isProtected(block);
		}
		if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
			if (is_private
					&& !Lockette.isOwner(block, player.getName())
					&& !Lockette.isUser(block, player.getName(), true)
					&& !Lockette.isEveryone(block)) {
				return null;
			}
			if (block.getState() instanceof Chest) {
				return ((Chest) block.getState()).getInventory();
			}
			if (block.getState() instanceof DoubleChest) {
				return ((DoubleChest) block.getState()).getInventory();
			}
		}
		else if (block.getType() == Material.ENDER_CHEST) {
			return player.getEnderChest();
		}
		return null;
	}

	public static void sortItems(List<ItemStack> items){
		Collections.sort(items, ItemStackComparator.INSTANCE);
	}

	private static class ItemStackComparator implements Comparator<ItemStack>{
		public static final ItemStackComparator INSTANCE = new ItemStackComparator();
		public int compare(ItemStack i1, ItemStack i2){
			if(i1 == null && i2 != null){
				return 1;
			}
			else if(i1 != null && i2 == null){
				return -1;
			}
			else if(i1 == null && i2 == null){
				return 0;
			}
			else if(i1.getType().getId() < i2.getType().getId()){
				return -1;
			}
			else if(i1.getType().getId() > i2.getType().getId()){
				return 1;
			}
			else{
				if(i1.getDurability() < i2.getDurability()){
					return -1;
				}
				else if(i1.getDurability() > i2.getDurability()){
					return 1;
				}
				return 0;
			}
		}
	}
}
