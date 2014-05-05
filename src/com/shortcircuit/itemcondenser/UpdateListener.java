package com.shortcircuit.itemcondenser;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class UpdateListener implements Listener{
    Plugin plugin;
    public UpdateListener(Plugin plugin){
        this.plugin = plugin;
    }
    @EventHandler (priority = EventPriority.MONITOR)
    public void doLogin(final PlayerJoinEvent event){
        try{
            Main.currentVersion = Update.getCurrentVersion();
        }
        catch(Exception e){

        }
        Player player = event.getPlayer();
        if(player.isOp() && Main.version < Main.currentVersion){
            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser] " + ChatColor.GREEN
                    + "A new version is available (" + ChatColor.LIGHT_PURPLE + "v"
                    + Main.currentVersion + ChatColor.GREEN + ")");
            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser] " + ChatColor.GREEN
                    + plugin.getDescription().getWebsite());
        }
    }
}
