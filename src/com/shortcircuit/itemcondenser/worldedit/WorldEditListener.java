package com.shortcircuit.itemcondenser.worldedit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.shortcircuit.itemcondenser.ItemCondenser;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.logging.AbstractLoggingExtent;

/* 
 * @author ShortCircuit908
 */
public class WorldEditListener extends AbstractLoggingExtent {
    private Actor actor;
    private ItemCondenser plugin;
    public WorldEditListener(Actor actor, Extent extent, ItemCondenser plugin) {
        super(extent);
        this.actor = actor;
        this.plugin = plugin;
    }

    @Override
    protected void onBlockChange(Vector position, BaseBlock newBlock) {
        if(actor.isPlayer()) {
            Player player = Bukkit.getPlayer(actor.getUniqueId());
            Location loc = new Location(player.getWorld(), position.getBlockX(), position.getBlockY(), position.getBlockZ());
            Block block = player.getWorld().getBlockAt(loc);
            BaseBlock old_block = getBlock(position);
            if(block.hasMetadata("isUtility")) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DelayedBlockChanger(block, old_block), 10);
                player.sendMessage(ChatColor.RED + "[ItemCondenser] You cannot edit a utility block!");
            }
        }
    }
}