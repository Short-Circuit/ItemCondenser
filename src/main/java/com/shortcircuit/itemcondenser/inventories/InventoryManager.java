package com.shortcircuit.itemcondenser.inventories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shortcircuit.itemcondenser.ItemCondenser;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * @author ShortCircuit908
 */
public class InventoryManager {
	private final ItemCondenser plugin;
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
	private final File data_dir;

	public InventoryManager(ItemCondenser plugin) {
		this.plugin = plugin;
		data_dir = new File(plugin.getDataFolder() + "/Inventories/");
		data_dir.mkdir();
	}

	public MultiInventory getInventories(UUID uuid) {
		File file = new File(data_dir + "/" + uuid.toString() + ".json");
		if (file.exists()) {
			try {
				FileReader reader = new FileReader(file);
				MultiInventory inventory = gson.fromJson(reader, MultiInventory.TYPE);
				reader.close();
				return inventory;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			return createInventories(uuid);
		}
		return null;
	}

	public MultiInventory createInventories(UUID uuid) {
		File file = new File(data_dir + "/" + uuid.toString() + ".json");
		try {
			file.createNewFile();
			MultiInventory inventory = new MultiInventory(uuid);
			FileWriter writer = new FileWriter(file);
			gson.toJson(inventory, MultiInventory.TYPE, writer);
			writer.flush();
			writer.close();
			return inventory;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasInventory(UUID uuid, String inventory_name) {
		MultiInventory inventories = getInventories(uuid);
		return inventories.hasInventory(inventory_name);
	}

	public Inventory loadInventory(Player player, String inventory_name) {
		MultiInventory inventories = getInventories(player.getUniqueId());
		if (inventories.hasInventory(inventory_name)) {
			Inventory inventory = plugin.getServer().createInventory(player, 36, inventory_name);
			inventory.setContents(inventories.getInventory(inventory_name).toBukkitInventory());
			return inventory;
		}
		return null;
	}

	public void saveInventories(MultiInventory inventories){
		File file = new File(data_dir + "/" + inventories.getUuid().toString() + ".json");
		try{
			FileWriter writer = new FileWriter(file);
			gson.toJson(inventories, MultiInventory.TYPE, writer);
			writer.flush();
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
