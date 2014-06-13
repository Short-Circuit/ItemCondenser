package com.shortcircuit.itemcondenser;

import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class UtilityManager {
    private HashMap<String, UtilityBlock> utility_list = new HashMap<>();
    public UtilityManager(){
    }
    public void addUtility(String player_name, Block block, InventoryType utility_type){
        utility_list.put(player_name, (new UtilityBlock(block, utility_type)));
    }
    public void removeUtility(Player player){
        UtilityBlock utility = utility_list.get(player.getName());
        Block block = utility.getBlock().getWorld().getBlockAt(utility.getLocation());
        block.setType(utility.getOldBlock());
        utility_list.remove(player.getName());
    }
    public UtilityBlock getUtility(String player_name){
        return utility_list.get(player_name);
    }
    public boolean hasUtility(String player_name){
        return utility_list.containsKey(player_name);
    }
}
