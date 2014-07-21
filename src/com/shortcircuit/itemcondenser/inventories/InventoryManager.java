package com.shortcircuit.itemcondenser.inventories;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
/**
 * @author ShortCircuit908
 *
 */
public class InventoryManager{
    private FileConfiguration inventory_file;
    private File file;
    /*
     * TODO: Load the inventory file
     */
    public InventoryManager(String data_folder){
        file = new File(data_folder + "/Inventories.yml");
        inventory_file = YamlConfiguration.loadConfiguration(file);
    }
    /*
     * TODO: Reload the inventory file
     */
    public void reloadInventories(){
        inventory_file = YamlConfiguration.loadConfiguration(file);
    }
    /*
     * TODO: Check if a player has an additional inventory
     */
    public boolean hasInventory(Player player, String inventory_name){
        return inventory_file.contains("Inventories." + player.getName() + "." + inventory_name);
    }
    /*
     * TODO: Check how many additional inventories a player has
     */
    public int getInventoryCount(Player player){
        try{
            return inventory_file.getConfigurationSection("Inventories." + player.getName()).getKeys(false).size();
        }
        catch(NullPointerException e){
            return 0;
        }
    }
    /*
     * TODO: Remove a player's additional inventory
     */
    public void removeInventory(Player player, String inventory_name){
        try{
            inventory_file.set("Inventories." + player.getName() + "." + inventory_name, null);
            inventory_file.save(file);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    /*
     * TODO: Save an additional inventory to the inventory file
     */
    public void saveInventory(Player player, Inventory inventory){
        try{
            // Create an empty list to store serialized items in
            List<Object> contents = new ArrayList<Object>();
            // Serialize each item and add it to the list
            for(ItemStack item : inventory.getContents()){
                if(item != null){
                    Map<String, Object> stack = item.serialize();
                    contents.add(stack);
                }
            }
            // Save the list to the inventory file
            inventory_file.set("Inventories." + player.getName() + "." + inventory.getName(), contents);
            inventory_file.save(file);
        }
        catch(IOException e){
            // Ya dun goofed
            e.printStackTrace();
        }
    }
    /*
     * TODO: Load an additional inventory from the inventory file
     */
    @SuppressWarnings("unchecked")
    public Inventory loadInventory(Player player, String inventory_name){
        // Create an inventory to put the items in
        Inventory inventory = Bukkit.createInventory(player, 36, inventory_name);
        // Load the serialized list of items
        List<Map<String, Object>> contents = (List<Map<String, Object>>)inventory_file.getList("Inventories." + player.getName() + "." + inventory_name);
        // Deserialize each item and add it to the inventory
        for(Map<String, Object> map : contents){
            ItemStack item = ItemStack.deserialize(map);
            inventory.addItem(item);
        }
        return inventory;
    }
    /*
     * TODO: Get a list of a player's additional inventories
     */
    public Set<String> getInventories(Player player){
        return inventory_file.getConfigurationSection("Inventories." + player.getName()).getKeys(false);
    }
}
