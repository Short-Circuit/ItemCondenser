package com.shortcircuit.itemcondenser;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class InventoryListener implements Listener{
    public InventoryListener(Plugin plugin){
        
    }
    @SuppressWarnings("unused")
    @EventHandler (priority = EventPriority.MONITOR)
    public void handleInvClose(final InventoryCloseEvent event){
        Player player = (Player)event.getPlayer();
        Inventory inv = event.getInventory();
        /*
        if(isInvOpen.containsKey(player)){
            isInvOpen.remove(player);
            inv.clear();
        }
        if(CoreMain.fileVarExists("Inventories", "Inventories." + player.getName())){
            Set<String> invs = CoreMain.fileGetSection("Inventories", "Inventories." + player.getName()).getKeys(false);
            for(String invName : invs){
                if(invName.equalsIgnoreCase(inv.getName())){
                    ItemStack[] items = inv.getContents();
                    List<ItemStack> listItems = new ArrayList<ItemStack>();
                    for(ItemStack item : items){
                        if(item != null){
                            ItemMeta meta = item.getItemMeta();
                            List<String> lore = meta.getLore();
                            List<String> newLore = new ArrayList<String>();
                            meta.setDisplayName(colorToChar(meta.getDisplayName()));
                            if(lore != null){
                                for(String str : lore){
                                    newLore.add(colorToChar(str));
                                }
                                meta.setLore(newLore);
                            }
                            item.setItemMeta(meta);
                            listItems.add(item);
                        }
                    }
                    CoreMain.fileVarSave("Inventories", "Inventories." + player.getName() + "." + inv.getName().toLowerCase(), listItems);
                }
            }
        }
        */
    }
}
