package com.shortcircuit.itemcondenser;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener{
    Main main;
    public InventoryListener(Main main){
        this.main = main;
    }
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
}
