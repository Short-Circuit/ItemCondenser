package com.shortcircuit.itemcondenser.inventories;

import com.google.gson.reflect.TypeToken;

import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author ShortCircuit908
 */
public class MultiInventory {
	public static final Type TYPE = new TypeToken<MultiInventory>() {
	}.getType();
	private final UUID uuid;
	private final Set<InventoryWrapper> inventories = new HashSet<>();

	public MultiInventory(UUID uuid) {
		this.uuid = uuid;
	}

	public InventoryWrapper createOrUpdateInventory(String name, ItemStack[] contents) {
		return createOrUpdateInventory(name, ItemWrapper.convertArray(contents));
	}

	public InventoryWrapper createOrUpdateInventory(String name, ItemWrapper[] contents) {
		InventoryWrapper inventory = getInventory(name);
		if (inventory == null) {
			inventory = new InventoryWrapper(name, contents);
		}
		else {
			inventory.setContents(contents);
		}
		inventories.add(inventory);
		return inventory;
	}

	public InventoryWrapper getInventory(String name) {
		for (InventoryWrapper inventory : inventories) {
			if (inventory.getName().equals(name)) {
				return inventory;
			}
		}
		return null;
	}

	public InventoryWrapper removeInventory(String name){
		InventoryWrapper inventory = getInventory(name);
		inventories.remove(inventory);
		return inventory;
	}

	public boolean hasInventory(String name){
		return getInventory(name) != null;
	}

	public int getInventoryCount(){
		return inventories.size();
	}

	public ArrayList<String> listInventories(){
		ArrayList<String> inventory_names = new ArrayList<>(inventories.size());
		for(InventoryWrapper inventory : inventories){
			inventory_names.add(inventory.getName());
		}
		return inventory_names;
	}

	public UUID getUuid(){
		return uuid;
	}
}
