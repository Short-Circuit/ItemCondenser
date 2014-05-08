package com.shortcircuit.itemcondenser;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

public class InventoryHandler {
    Plugin plugin;
    public InventoryHandler(Plugin plugin){
        this.plugin = plugin;
    }
    public void saveInventory(Player player, Inventory inventory){
        try{
            StringWriter str = new StringWriter();
            JSONObject object = new JSONObject();
            Map<String, List<Map<String, Object>>> inventories = new HashMap<String, List<Map<String, Object>>>();
            List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
            for(ItemStack item : inventory.getContents()){
                if(item != null){
                    contents.add(item.serialize());
                }
            }
            inventories.put(inventory.getName(), contents);
            object = object.put(player.getName(), inventories);
            object.write(str);
            Bukkit.getLogger().info(str.toString());
        }
        catch(Exception e){

        }
    }
    public Inventory loadInventory(Player player, String inventory_name){

        return null;
    }
}
