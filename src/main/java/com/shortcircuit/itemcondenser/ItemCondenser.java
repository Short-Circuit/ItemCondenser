package com.shortcircuit.itemcondenser;

import com.shortcircuit.itemcondenser.commands.*;
import com.shortcircuit.itemcondenser.configuration.InventoryHandler;
import com.shortcircuit.itemcondenser.listeners.InventoryListener;
import com.shortcircuit.itemcondenser.listeners.UtilityListener;
import com.shortcircuit.itemcondenser.utilities.UtilityBlock;
import com.shortcircuit.itemcondenser.utilities.UtilityManager;
import com.shortcircuit.itemcondenser.worldedit.WorldEditHandler;
import com.shortcircuit.shortcommands.ShortCommands;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.command.ShortCommandHandler;
import com.sk89q.worldedit.WorldEdit;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

/**
 * @author ShortCircuit908
 *
 */
@SuppressWarnings("deprecation")
public final class ItemCondenser extends JavaPlugin{
	private Logger logger = getLogger();
	private InventoryHandler inventory_handler;
	private UtilityManager utility_manager;
	private boolean lockette = false;
	private ShortCommandHandler<ShortCommand> command_handler;
	public void onEnable(){
		logger.info("ItemCondenser by ShortCircuit908");
		utility_manager = new UtilityManager(this);
		try {
			inventory_handler = new InventoryHandler(this);
		}
		catch(NoClassDefFoundError e) {
			logger.severe("Could not load GSON library");
			logger.severe("I dunno how to fix this");
			logger.severe("I mean, this SHOULD be a Maven dependency");
			logger.severe("But Maven has always disliked me");
			logger.severe("Apparently, it's just an Eclipse thing");
			logger.severe("I'll just use IDEA then, I guess");
			setEnabled(false);
			return;
		}
		if(getServer().getPluginManager().getPlugin("ShortCommands") == null){
			logger.severe("This plugin requires the ShortCommands command system to operate");
			logger.severe("Please install the plugin from http://dev.bukkit.org/bukkit-plugins/ShortCommands/");
			setEnabled(false);
			return;
		}
		File configFile = new File(getDataFolder() + "/main/config.yml");
		if(!configFile.exists()){
			logger.info("No configuration file found, creating one");
			saveDefaultConfig();
		}
		getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
		getServer().getPluginManager().registerEvents(new UtilityListener(this), this);
		if(getServer().getPluginManager().isPluginEnabled("Lockette")) {
			String lockVersion = Lockette.getCoreVersion();
			logger.info("Successfully hooked into Lockette v" + lockVersion);
			lockette = true;
		}
		if(Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
			try {
				WorldEdit.getInstance().getEventBus().register(new WorldEditHandler(this));
				logger.info("Successfully hooked into WorldEdit v" + WorldEdit.getVersion());
			}
			catch(NoSuchMethodError e) {
				logger.warning("Minimum WorldEdit version required is 6.0.0");
				logger.warning("Plugin will still function, but will be more error-prone");
			}
		}
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
			if(utility_manager.hasUtility(player.getName())){
				UtilityBlock utility = utility_manager.getUtility(player.getName());
				Block block = utility.getBlock();
				ContainerBlock container = (ContainerBlock)block.getState();
				Inventory inv = container.getInventory();
				for(ItemStack item : inv.getContents()){
					if(item != null){
						inv.remove(item);
						player.getInventory().addItem(item);
					}
				}
				utility_manager.removeUtility(player);
				player.closeInventory();
			}
			else if(player.hasMetadata("invIsOpen")){
				if(player.getMetadata("invIsOpen").get(0).asBoolean()){
					inventory_handler.saveInventory(player, player.getOpenInventory().getTopInventory());
					player.closeInventory();
					player.setMetadata("invIsOpen", new EntityMetadata(this, false));
				}
			}
		}
		logger.info("ItemCondenser disabled");
	}
	public UtilityManager getUtilityManager(){
		return utility_manager;
	}
	public boolean usingLockette() {
		return lockette;
	}
	public InventoryHandler getInventoryHandler() {
		return inventory_handler;
	}
}