package com.shortcircuit.itemcondenser.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.itemcondenser.inventories.ItemWrapper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ShortCircuit908
 *
 */
public class InventoryHandler {
	private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	private File player_dir;

	public InventoryHandler(ItemCondenser plugin) {
		player_dir = new File(plugin.getDataFolder() + "/Inventories/");
		player_dir.mkdirs();
	}

	public Set<String> getInventories(Player player){
		return getJsonInventories(player).keySet();
	}

	public Inventory loadInventory(Player player, String inventory_name) {
		Map<String, ItemWrapper[]> inventories = getJsonInventories(player);
		if(inventories.containsKey(inventory_name)) {
			Inventory inventory = Bukkit.createInventory(player, 36, inventory_name);
			for(ItemWrapper serialized : inventories.get(inventory_name)) {
				ItemStack item = serialized.toItemStack();
				inventory.addItem(item);
			}
			return inventory;
		}
		return null;
	}

	public void saveInventory(Player player, Inventory inventory) {
		Map<String, ItemWrapper[]> inventories = getJsonInventories(player);
		inventories.remove(inventory.getName());
		inventories.put(inventory.getName(), toStructuredInventory(inventory));
		saveElement(player, getJsonElement(inventories));
	}

	public void removeInventory(Player player, String inventory_name) {
		Map<String, ItemWrapper[]> inventories = getJsonInventories(player);
		inventories.remove(inventory_name);
		saveElement(player, getJsonElement(inventories));
	}

	public boolean hasInventory(Player player, String inventory_name) {
		return getInventories(player).contains(inventory_name);
	}

	private ItemWrapper[] toStructuredInventory(Inventory inventory){
		Set<ItemWrapper> contents = new HashSet<ItemWrapper>();
		for(ItemStack item : inventory.getContents()) {
			if(item != null){
				contents.add(new ItemWrapper(item));
			}
		}
		return (ItemWrapper[])contents.toArray(new ItemWrapper[0]);
	}

	private JsonElement loadElement(Player player) {
		File file = new File(player_dir + "/" + player.getUniqueId());
		JsonParser parser = new JsonParser();
		try {
			JsonElement element = parser.parse(new FileReader(file));
			return element;
		}
		catch(JsonParseException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ItemCondenser] Unable to parse file "
					+ file.getName() + ": " + e.getMessage());
		}
		catch(FileNotFoundException e){
			return null;
		}
		return null;
	}

	private Map<String, ItemWrapper[]> getJsonInventories(Player player){
		return getJsonInventories(loadElement(player));
	}

	private void saveElement(Player player, JsonElement element) {
		File file = new File(player_dir + "/" + player.getUniqueId());
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.append(element.toString());
			writer.flush();
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public JsonElement getJsonElement(Map<String, ItemWrapper[]> inventories) {
		return gson.toJsonTree(inventories);
	}

	private Map<String, ItemWrapper[]> getJsonInventories(JsonElement element){
		try {
			Type type = new TypeToken<Map<String, ItemWrapper[]>>(){}.getType();
			Map<String, ItemWrapper[]> inventories = gson.fromJson(element, type);
			if(inventories == null){
				inventories = new HashMap<>();
			}
			return inventories;
		}
		catch(JsonParseException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ItemCondenser] Unable to parse element: "
					+ e.getMessage());
		}
		return new HashMap<>();
	}
}
