package com.shortcircuit.itemcondenser;

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

public class InventoryHandler {
    Main main;
    FileConfiguration inventory_file;
    File file;
    public InventoryHandler(Main main){
        this.main = main;
        file = new File(this.main.getDataFolder() + "/Inventories.yml");
        inventory_file = YamlConfiguration.loadConfiguration(file);
    }
    public boolean hasInventory(Player player, String inventory_name){
        return inventory_file.contains("Inventories." + player.getName() + "." + inventory_name);
    }
    public int getInventoryCount(Player player){
        return inventory_file.getConfigurationSection("Inventories." + player.getName()).getKeys(false).size();
    }
    public void removeInventory(Player player, String inventory_name){
        try{
            inventory_file.set("Inventories." + player.getName() + "." + inventory_name, null);
            inventory_file.save(file);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void saveInventory(Player player, Inventory inventory){
        try{
            List<Object> contents = new ArrayList<Object>();
            for(ItemStack item : inventory.getContents()){
                if(item != null){
                    Map<String, Object> stack = item.serialize();
                    //Map<String, Object> meta = item.getItemMeta().serialize();
                    //stack.put("meta", meta);
                    contents.add(stack);
                }
            }
            inventory_file.set("Inventories." + player.getName() + "." + inventory.getName(), contents);
            inventory_file.save(file);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public Inventory loadInventory(Player player, String inventory_name){
        Inventory inventory = Bukkit.createInventory(player, 36, inventory_name);
        List<Map<String, Object>> contents = (List<Map<String, Object>>)inventory_file.getList("Inventories." + player.getName() + "." + inventory_name);
        for(Map<String, Object> map : contents){
            ItemStack item = ItemStack.deserialize(map);
            inventory.addItem(item);
        }
        return inventory;
    }
    public Set<String> getInventories(Player player){
        return inventory_file.getConfigurationSection("Inventories." + player.getName()).getKeys(false);
    }
}
