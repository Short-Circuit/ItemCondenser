package com.shortcircuit.itemcondenser;
import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.yi.acru.bukkit.Lockette.Lockette;

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
		if(!getServer().getPluginManager().isPluginEnabled("ShortCommands")){
			logger.severe("This plugin requires the ShortCommands command system to operate");
			logger.severe("Please install the plugin from http://dev.bukkit.org/bukkit-plugins/ShortCommands/");
			setEnabled(false);
			return;
		}
		inventory_handler = new InventoryHandler(this);
		File configFile = new File(getDataFolder() + "/config.yml");
		if(!configFile.exists()){
			logger.info("No configuration file found, creating one");
			saveDefaultConfig();
		}
		getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
		getServer().getPluginManager().registerEvents(new UtilityListener(this), this);
		try{
			String lockVersion = Lockette.getCoreVersion();
			logger.info("Successfully hooked into Lockette v" + lockVersion);
			lockette = true;
		}
		catch(NoClassDefFoundError e){
		}
		utility_manager = new UtilityManager(this);
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