package com.shortcircuit.itemcondenser;

import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
@SuppressWarnings("deprecation")
public class InventoryListener implements Listener{
    private ItemCondenser main;
    public InventoryListener(ItemCondenser main){
        this.main = main;
    }
    /*
     * TODO: Set default metadata
     */
    @EventHandler
    public void onLogin(final PlayerLoginEvent event){
        event.getPlayer().setMetadata("invIsOpen", new EntityMetadata(main, false));
    }
    /*
     * TODO: Save the player's additional inventory when it is closed
     */
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event){
        Player player = (Player)event.getPlayer();
        Inventory inventory = event.getInventory();
        if(player.hasMetadata("invIsOpen")){
            if(player.getMetadata("invIsOpen").get(0).asBoolean()){
                main.inventory_handler.saveInventory(player, inventory);
            }
        }
        player.setMetadata("invIsOpen", new EntityMetadata(main, false));
    }
    @EventHandler
    public void closeUtility(final InventoryCloseEvent event){
        Player player = (Player)event.getPlayer();
        if(main.getUtilityManager().hasUtility(player.getName())){
            boolean empty = true;
            for(ItemStack item : event.getInventory().getContents()){
                if(item != null){
                    empty = false;
                    break;
                }
            }
            if(empty){
                main.getUtilityManager().removeUtility(player);
            }
        }
    }
    @EventHandler
    public void forceCloseUtility(final PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(main.getUtilityManager().hasUtility(player.getName())){
            UtilityBlock utility = main.getUtilityManager().getUtility(player.getName());
            Block block = utility.getBlock();
            ContainerBlock container = (ContainerBlock)block.getState();
            Inventory inv = container.getInventory();
            for(ItemStack item : inv.getContents()){
                if(item != null){
                    inv.remove(item);
                    player.getInventory().addItem(item);
                }
            }
            main.getUtilityManager().removeUtility(player);
        }
    }
}
