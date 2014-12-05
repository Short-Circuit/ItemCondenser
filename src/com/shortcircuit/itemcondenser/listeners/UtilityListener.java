package com.shortcircuit.itemcondenser.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.itemcondenser.utilities.UtilityBlock;
@SuppressWarnings("deprecation")
/**
 * @author ShortCircuit908
 *
 */
public class UtilityListener implements Listener{
    private ItemCondenser plugin;
    public UtilityListener(ItemCondenser plugin){
        this.plugin = plugin;
    }
    /*
     * Remove the utility
     */
    @EventHandler
    public void closeUtility(final InventoryCloseEvent event){
        Player player = (Player)event.getPlayer();
        if(plugin.getUtilityManager().hasUtility(player.getName())){
            // Make sure that the utility inventory is empty
            boolean empty = true;
            for(ItemStack item : event.getInventory().getContents()){
                if(item != null){
                    empty = false;
                    break;
                }
            }
            // Remove the utility
            if(empty){
                plugin.getUtilityManager().removeUtility(player);
            }
        }
    }
    @EventHandler
    public void forceCloseUtility(final PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(plugin.getUtilityManager().hasUtility(player.getName())){
            UtilityBlock utility = plugin.getUtilityManager().getUtility(player.getName());
            Block block = utility.getBlock();
            ContainerBlock container = (ContainerBlock)block.getState();
            Inventory inv = container.getInventory();
            for(ItemStack item : inv.getContents()){
                if(item != null){
                    inv.remove(item);
                    player.getInventory().addItem(item);
                }
            }
            plugin.getUtilityManager().removeUtility(player);
        }
    }
    @EventHandler
    public void cancelUtilityBreak(final BlockBreakEvent event){
        if(event.getBlock().hasMetadata("isUtility")){
            event.getPlayer().sendMessage(ChatColor.RED + "[ItemCondenser] You cannot break a utility block!");
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void cancelUtilityUse(final PlayerInteractEvent event){
        if(event.getClickedBlock() != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(event.getClickedBlock().hasMetadata("isUtility")){
                event.getPlayer().sendMessage(ChatColor.RED + "[ItemCondenser] You cannot use a utility block!");
                event.setCancelled(true);
            }
        }
    }
}