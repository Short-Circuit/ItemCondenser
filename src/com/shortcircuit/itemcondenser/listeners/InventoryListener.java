package com.shortcircuit.itemcondenser.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;

import com.shortcircuit.itemcondenser.EntityMetadata;
import com.shortcircuit.itemcondenser.ItemCondenser;
/**
 * @author ShortCircuit908
 *
 */
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
                main.getInventoryManager().saveInventory(player, inventory);
            }
        }
        player.setMetadata("invIsOpen", new EntityMetadata(main, false));
    }
}
