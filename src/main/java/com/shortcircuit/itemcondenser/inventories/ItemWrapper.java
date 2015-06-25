package com.shortcircuit.itemcondenser.inventories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ShortCircuit908
 *
 */
public class ItemWrapper{
	private Material type;
	private int amount;
	private short durability;
	private String displayName;
	private List<String> lore;
	private Map<String, Integer> enchantments = new HashMap<>();
	public ItemWrapper(ItemStack item){
		this.type = item.getType();
		this.amount = item.getAmount();
		this.durability = item.getDurability();
		for(Enchantment enchant : item.getEnchantments().keySet()){
			enchantments.put(enchant.getName(), item.getEnchantments().get(enchant));
		}
		this.displayName = item.getItemMeta().getDisplayName();
		this.lore = item.getItemMeta().getLore();
	}
	public ItemStack toItemStack(){

		ItemStack item = new ItemStack(type, amount, durability);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		if(enchantments != null){
			for(String enchant : enchantments.keySet()){
				meta.addEnchant(Enchantment.getByName(enchant), enchantments.get(enchant), true);
			}
		}
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack[] convertArray(ItemWrapper[] array){
		if(array == null){
			return null;
		}
		ItemStack[] converted = new ItemStack[array.length];
		for(int i = 0; i < array.length; i++){
			converted[i] = array[i] == null ? null : array[i].toItemStack();
		}
		return converted;
	}

	public static ItemWrapper[] convertArray(ItemStack[] array){
		if(array == null){
			return null;
		}
		ItemWrapper[] converted = new ItemWrapper[array.length];
		for(int i = 0; i < array.length; i++){
			converted[i] = array[i] == null ? null : new ItemWrapper(array[i]);
		}
		return converted;
	}

	public static ArrayList<ItemStack> convertWrapperList(List<ItemWrapper> list){
		ArrayList<ItemStack> converted = new ArrayList<>(list.size());
		for(ItemWrapper wrapper : list){
			converted.add(wrapper == null ? null : wrapper.toItemStack());
		}
		return converted;
	}

	public static ArrayList<ItemWrapper> convertStackList(List<ItemStack> list){
		ArrayList<ItemWrapper> converted = new ArrayList<>(list.size());
		for(ItemStack item : list){
			converted.add(item == null ? null : new ItemWrapper(item));
		}
		return converted;
	}
}
