package com.shortcircuit.itemcondenser.inventories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
}
