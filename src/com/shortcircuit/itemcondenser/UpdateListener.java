package com.shortcircuit.itemcondenser;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class UpdateListener implements Listener{
    Plugin plugin;
    public UpdateListener(Plugin plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(player.hasPermission("itemcondenser.updates") && Main.update){
            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser] "
                    + ChatColor.GREEN + "An update is available: " + Main.name + ", a "
                    + Main.type + " for " + Main.version + " available at " + Main.link);
        }
    }
}
