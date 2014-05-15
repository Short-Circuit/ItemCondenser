package com.shortcircuit.itemcondenser;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateListener implements Listener{
    Main main;
    public UpdateListener(Main main){
        this.main = main;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(main.getConfig().getBoolean("CheckUpdates")){
            Player player = event.getPlayer();
            if(player.hasPermission("itemcondenser.updates") && main.update){
                player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser] "
                        + ChatColor.GREEN + "An update is available: " + main.name + ", a "
                        + main.type + " for " + main.version + " available at " + main.link);
            }
        }
    }
}
