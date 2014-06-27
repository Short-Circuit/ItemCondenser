package com.shortcircuit.itemcondenser.utilities;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import com.shortcircuit.itemcondenser.ItemCondenser;
/**
 * @author ShortCircuit908
 *
 */
@SuppressWarnings("deprecation")
public class UtilityManager {
    private HashMap<String, UtilityBlock> utility_list = new HashMap<>();
    private ItemCondenser plugin;
    public UtilityManager(ItemCondenser plugin){
        this.plugin = plugin;
    }
    public void addUtility(String player_name, Block block, InventoryType utility_type){
        utility_list.put(player_name, (new UtilityBlock(block, utility_type)));
        if(block.getState() instanceof ContainerBlock){
            ContainerBlock old_inv = (ContainerBlock)block.getState();
            old_inv.getInventory().clear();
            block.getState().update();
        }
    }
    public void removeUtility(Player player){
        UtilityBlock utility = utility_list.get(player.getName());
        Block block = utility.getBlock().getWorld().getBlockAt(utility.getLocation());
        block.setType(utility.getOldBlockType());
        block.setData(utility.getOldBlockData());
        block.getState().setData(utility.getOldBlockState().getData()); 
        if(block.getState() instanceof ContainerBlock){
            ContainerBlock old_inv = (ContainerBlock)block.getState();
            old_inv.getInventory().setContents(utility.getOldInventory());
            block.getState().update();
        }
        block.removeMetadata("isUtility", plugin);
        utility_list.remove(player.getName());
    }
    public UtilityBlock getUtility(String player_name){
        return utility_list.get(player_name);
    }
    public UtilityBlock getUtility(Location block_location){
        for(String key : utility_list.keySet()){
            UtilityBlock utility = utility_list.get(key);
            if(utility.getBlock().getLocation().equals(block_location)){
                return utility;
            }
        }
        return null;
    }
    public boolean hasUtility(String player_name){
        return utility_list.containsKey(player_name);
    }
    public boolean hasUtility(Location block_location){
        for(String key : utility_list.keySet()){
            UtilityBlock utility = utility_list.get(key);
            if(utility.getBlock().getLocation().equals(block_location)){
                return true;
            }
        }
        return false;
    }
}
