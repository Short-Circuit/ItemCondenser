package com.shortcircuit.itemcondenser.listeners;

import com.shortcircuit.itemcondenser.ItemCondenser;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("deprecation")
/**
 * @author ShortCircuit908
 *
 */
public class UtilityListener implements Listener {
	private ItemCondenser plugin;

	public UtilityListener(ItemCondenser plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void closeUtility(final InventoryCloseEvent event) {

	}

	@EventHandler
	public void forceCloseUtility(final PlayerQuitEvent event) {

	}

	@EventHandler
	public void cancelUtilityBreak(final BlockBreakEvent event) {

	}

	@EventHandler
	public void cancelUtilityUse(final PlayerInteractEvent event) {

	}
}