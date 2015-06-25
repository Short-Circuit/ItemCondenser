package com.shortcircuit.itemcondenser.listeners;

import com.shortcircuit.itemcondenser.ItemCondenser;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;

/**
 * @author ShortCircuit908
 */
public class InventoryListener implements Listener {
	private final ItemCondenser plugin;

	public InventoryListener(ItemCondenser plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onLogin(final PlayerLoginEvent event) {

	}

	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Inventory inventory = event.getInventory();
	}
}