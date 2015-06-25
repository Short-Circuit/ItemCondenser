package com.shortcircuit.itemcondenser.inventories;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * @author ShortCircuit908
 */
public class InventoryWrapper {
	private final String name;
	private final ArrayList<ItemWrapper> contents;

	public InventoryWrapper(String name, ItemStack[] inventory) {
		this(name, ItemWrapper.convertArray(inventory));
	}

	public InventoryWrapper(String name, ItemWrapper[] inventory){
		this.name = name;
		if(inventory != null) {
			contents = new ArrayList<>(inventory.length);
			for (ItemWrapper item : inventory) {
				contents.add(item);
			}
		}
		else{
			contents = new ArrayList<>(36);
		}
	}

	public ItemStack[] toBukkitInventory() {
		ItemStack[] bukkit_contents = new ItemStack[contents.size()];
		for (int i = 0; i < contents.size(); i++) {
			ItemWrapper wrapped = contents.get(i);
			bukkit_contents[i] = wrapped == null ? null : wrapped.toItemStack();
		}
		return bukkit_contents;
	}

	public ArrayList<ItemWrapper> getContents(){
		return contents;
	}

	public void setContents(ItemStack[] contents){
		setContents(ItemWrapper.convertArray(contents));
	}

	public void setContents(ItemWrapper[] contents){
		this.contents.clear();
		if(contents != null) {
			assert contents.length <= this.contents.size();
			for (ItemWrapper item : contents) {
				this.contents.add(item);
			}
		}
	}

	public String getName(){
		return name;
	}
}
