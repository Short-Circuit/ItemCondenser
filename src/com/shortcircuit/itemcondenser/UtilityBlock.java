package com.shortcircuit.itemcondenser;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryType;

public class UtilityBlock{
    private Location block_location;
    private Material old_block;
    private Block utility_block;
    private InventoryType utility_type;
    public UtilityBlock(Block old_block, InventoryType utility_type){
        this.old_block = old_block.getType();
        this.utility_type = utility_type;
        this.utility_block = old_block;
        this.block_location = old_block.getLocation();
    }
    public UtilityBlock setOldBlock(Block old_block){
        this.old_block = old_block.getType();
        return this;
    }
    public Material getOldBlock(){
        return old_block;
    }
    public UtilityBlock setUtilityType(InventoryType utility_type){
        this.utility_type = utility_type;
        return this;
    }
    public InventoryType getUtilityType(){
        return utility_type;
    }
    public Block getBlock(){
        return utility_block;
    }
    public Location getLocation(){
        return block_location;
    }
}
