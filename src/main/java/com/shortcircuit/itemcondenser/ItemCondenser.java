package com.shortcircuit.itemcondenser;

import com.shortcircuit.itemcondenser.commands.BrewCommand;
import com.shortcircuit.itemcondenser.commands.ClearCommand;
import com.shortcircuit.itemcondenser.commands.CondenseCommand;
import com.shortcircuit.itemcondenser.commands.CraftCommand;
import com.shortcircuit.itemcondenser.commands.DropCommand;
import com.shortcircuit.itemcondenser.commands.DropallCommand;
import com.shortcircuit.itemcondenser.commands.EnchantCommand;
import com.shortcircuit.itemcondenser.commands.EnderchestCommand;
import com.shortcircuit.itemcondenser.commands.InvcreateCommand;
import com.shortcircuit.itemcondenser.commands.InvlistCommand;
import com.shortcircuit.itemcondenser.commands.InvopenCommand;
import com.shortcircuit.itemcondenser.commands.InvremoveCommand;
import com.shortcircuit.itemcondenser.commands.ItemloreCommand;
import com.shortcircuit.itemcondenser.commands.ItemnameCommand;
import com.shortcircuit.itemcondenser.commands.MoreitemsCommand;
import com.shortcircuit.itemcondenser.commands.RepairCommand;
import com.shortcircuit.itemcondenser.commands.SmeltCommand;
import com.shortcircuit.itemcondenser.commands.SortCommand;
import com.shortcircuit.itemcondenser.commands.StoreCommand;
import com.shortcircuit.itemcondenser.commands.StoreallCommand;
import com.shortcircuit.itemcondenser.commands.TrashCommand;
import com.shortcircuit.itemcondenser.inventories.InventoryManager;
import com.shortcircuit.itemcondenser.listeners.InventoryListener;
import com.shortcircuit.itemcondenser.listeners.UtilityListener;
import com.shortcircuit.itemcondenser.utilities.Utility;
import com.shortcircuit.itemcondenser.utilities.UtilityManager;
import com.shortcircuit.shortcommands.ShortCommands;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.command.ShortCommandHandler;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author ShortCircuit908
 *
 */
@SuppressWarnings("deprecation")
public final class ItemCondenser extends JavaPlugin{
	private Logger logger = getLogger();
	private InventoryManager inventory_manager;
	private UtilityManager utility_manager;
	private ShortCommandHandler<ShortCommand> command_handler;
	public void onEnable(){
		logger.info("ItemCondenser by ShortCircuit908");
		utility_manager = new UtilityManager(this);
		inventory_manager = new InventoryManager(this);
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
		getServer().getPluginManager().registerEvents(new UtilityListener(this), this);
		command_handler = ShortCommands.getInstance().getCommandHandler();
		command_handler.registerCommands(
				new BrewCommand(this),
				new ClearCommand(this),
				new CondenseCommand(this),
				new CraftCommand(this),
				new DropallCommand(this),
				new DropCommand(this),
				new EnchantCommand(this),
				new EnderchestCommand(this),
				new InvcreateCommand(this),
				new InvlistCommand(this),
				new InvopenCommand(this),
				new InvremoveCommand(this),
				new ItemloreCommand(this),
				new ItemnameCommand(this),
				new MoreitemsCommand(this),
				new RepairCommand(this),
				new SmeltCommand(this),
				new SortCommand(this),
				new StoreallCommand(this),
				new StoreCommand(this),
				new TrashCommand(this));
		command_handler.registerHelpTopics(this);
		logger.info("ItemCondenser enabled");
	}
	public void onDisable(){
		logger.info("Disabling...");
		for(Player player : Bukkit.getOnlinePlayers()){
			if(utility_manager.hasUtility(player.getUniqueId())){
				Utility utility = utility_manager.getUtility(player.getUniqueId());
				Block block = utility.getBlock();
				ContainerBlock container = (ContainerBlock)block.getState();
				Inventory inv = container.getInventory();
				HashMap<Integer, ItemStack> unstored = player.getInventory().addItem(inv.getContents());
				for(ItemStack item : unstored.values()){
					player.getWorld().dropItem(player.getLocation(), item);
				}
				utility_manager.removeUtility(player.getUniqueId());
				player.closeInventory();
			}
		}
		logger.info("ItemCondenser disabled");
	}
	public UtilityManager getUtilityManager(){
		return utility_manager;
	}

	public InventoryManager getInventoryManager() {
		return inventory_manager;
	}
}