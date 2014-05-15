package com.shortcircuit.itemcondenser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.shortcircuit.itemcondenser.Updater.ReleaseType;
import com.shortcircuit.itemcondenser.Updater.UpdateType;

public final class Main extends JavaPlugin{
    public Logger logger = Bukkit.getLogger();
    public InventoryHandler inventory_handler;
    private Updater updater;
    public boolean update = false;
    public String name;
    public String version;
    public String link;
    public ReleaseType type;
    public File file;
    public void onEnable(){
        logger.info("[ItemCondenser] ItemCondenser by ShortCircuit908");
        logger.info("[ItemCondenser] ItemCondenser enabled");
        inventory_handler = new InventoryHandler(this);
        File configFile = new File(this.getDataFolder() + "/config.yml");
        if(!configFile.exists()){
            logger.info("[ItemCondenser] No configuration file found, creating one");
            saveDefaultConfig();
        }
        file = this.getFile();
        if(this.getConfig().getBoolean("CheckUpdates")){
            updater = new Updater(this, 71867, file, UpdateType.NO_DOWNLOAD, true);
            update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
            name = updater.getLatestName();
            version = updater.getLatestGameVersion();
            link = updater.getLatestFileLink();
            type = updater.getLatestType();
        }
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
    }
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        inventory_handler.reloadInventories();
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(commandLabel.equalsIgnoreCase("ctable")){
                if(player.hasPermission("ItemCondenser.Utility.Craft")){
                    player.openWorkbench(player.getLocation(), true);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("echest")){
                if(player.hasPermission("ItemCondenser.Utility.EnderChest")){
                    Inventory enderInv = player.getEnderChest();
                    player.openInventory(enderInv);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("anvil")){
                if(player.hasPermission("ItemCondenser.Utility.Repair")){
                    //Inventory anvilInv = Bukkit.createInventory(player, InventoryType.ANVIL);
                    //player.openInventory(anvilInv);
                    player.sendMessage(ChatColor.RED + "This feature has not been implemented (waiting for CraftBukkit)");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("brew")){
                if(player.hasPermission("ItemCondenser.Utility.Brew")){
                    //Inventory brewInv = Bukkit.createInventory(player, InventoryType.BREWING);
                    //player.openInventory(brewInv);
                    InventoryView view = player.openWorkbench(player.getLocation(), true);
                    int[] spaces = {1, 3, 4, 5, 6};
                    for(int space : spaces){
                        view.setItem(space, new ItemStack(Material.THIN_GLASS, 1));
                    }
                    player.sendMessage(ChatColor.RED + "This feature has not been implemented (waiting for CraftBukkit)");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("smelt")){
                if(player.hasPermission("ItemCondenser.Utility.Smelt")){
                    //Inventory smeltInv = Bukkit.createInventory(player, InventoryType.FURNACE);
                    //player.openInventory(smeltInv);
                    player.sendMessage(ChatColor.RED + "This feature has not been implemented (waiting for CraftBukkit)");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("trash")){
                if(player.hasPermission("ItemCondenser.Utility.Trash")){
                    Inventory trashInv = Bukkit.createInventory(null, 36, "Trash");
                    player.openInventory(trashInv);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("clear")){
                Inventory inv = player.getInventory();
                if(player.hasPermission("ItemCondenser.Clear")){
                    inv.clear();
                    player.sendMessage(ChatColor.GREEN + "Inventory cleared");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("store")){
                if(player.hasPermission("ItemCondenser.Items.Store")){
                    Inventory inv = player.getInventory();
                    ItemStack item = player.getItemInHand();
                    Location lookAt = player.getTargetBlock(null, 32).getLocation();
                    Block block = player.getWorld().getBlockAt(lookAt);
                    if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST){
                        Chest chest = (Chest) player.getWorld().getBlockAt(lookAt).getState();
                        if(item != null && item.getType() != Material.AIR){
                            if(chest.getInventory().firstEmpty() == -1){
                                player.sendMessage(ChatColor.RED + "Chest is full");
                            }
                            else{
                                inv.removeItem(item);
                                chest.getInventory().addItem(item);
                            }
                        }
                    }
                    else if(block.getType() == Material.ENDER_CHEST){
                        Inventory enderInv = player.getEnderChest();
                        if(item != null && item.getType() != Material.AIR){
                            if(enderInv.firstEmpty() == -1){
                                player.sendMessage(ChatColor.RED + "Ender chest is full");
                            }
                            else{
                                inv.removeItem(item);
                                enderInv.addItem(item);
                            }
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "No chest to store items in");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("storeall")){
                if(player.hasPermission("ItemCondenser.Items.StoreAll")){
                    Inventory inv = player.getInventory();
                    ItemStack[] items = inv.getContents();
                    Location lookAt = player.getTargetBlock(null, 32).getLocation();
                    Block block = player.getWorld().getBlockAt(lookAt);
                    if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST){
                        Chest chest = (Chest) player.getWorld().getBlockAt(lookAt).getState();
                        for(ItemStack item : items){
                            if(item != null && item.getType() != Material.AIR){
                                if(chest.getInventory().firstEmpty() == -1){
                                    player.sendMessage(ChatColor.RED + "Chest is full");
                                    break;
                                }
                                else{
                                    inv.removeItem(item);
                                    chest.getInventory().addItem(item);
                                }
                            }
                        }
                    }
                    else if(block.getType() == Material.ENDER_CHEST){
                        Inventory enderInv = player.getEnderChest();
                        for(ItemStack item : items){
                            if(item != null && item.getType() != Material.AIR){
                                if(enderInv.firstEmpty() == -1){
                                    player.sendMessage(ChatColor.RED + "Ender chest is full");
                                    break;
                                }
                                else{
                                    inv.removeItem(item);
                                    enderInv.addItem(item);
                                }
                            }
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "No chest to store items in");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("drop")){
                if(player.hasPermission("ItemCondenser.Items.Drop")){
                    Inventory inv = player.getInventory();
                    ItemStack item = player.getItemInHand();
                    if(item.getType() != Material.AIR){
                        inv.removeItem(item);
                        Location lookAt = player.getTargetBlock(null, 32).getLocation().add(0.5, 1.5, 0.5);
                        player.getWorld().dropItem(lookAt, item);
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("dropall")){
                if(player.hasPermission("ItemCondenser.Items.DropAll")){
                    Inventory inv = player.getInventory();
                    ItemStack[] items = inv.getContents();
                    for(ItemStack item : items){
                        if(item != null){
                            inv.removeItem(item);
                            Location lookAt = player.getTargetBlock(null, 32).getLocation();
                            Location dropAt = player.getWorld().getHighestBlockAt(lookAt).getLocation().add(0.5, 1.5, 0.5);
                            player.getWorld().dropItem(dropAt, item);
                        }
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("sort")){
                if(player.hasPermission("ItemCondenser.Sort")){
                    Inventory inv = player.getInventory();
                    Inventory newInv = Bukkit.createInventory(player, 36);
                    for(Material material : Material.values()){
                        for(ItemStack item : inv.getContents()){
                            if(item != null){
                                if(item.getType().equals(material)){
                                    inv.removeItem(item);
                                    newInv.addItem(item);
                                }
                            }
                        }
                    }
                    for(ItemStack item : newInv.getContents()){
                        if(item != null){
                            newInv.removeItem(item);
                            inv.addItem(item);
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "Inventory sorted");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("etable")){
                if(player.hasPermission("ItemCondenser.Utility.Enchant")){
                    player.openEnchanting(player.getLocation(), true);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("condense")){
                Inventory inv = player.getInventory();
                if(player.hasPermission("ItemCondenser.Condense")){
                    for(ItemStack item : inv.getContents()){
                        if(item != null){
                            inv.removeItem(item);
                            inv.addItem(item);
                        }
                    }
                    ItemStack[] items = inv.getContents();
                    Material[] itemReference = {Material.GOLD_NUGGET, Material.IRON_INGOT,
                            Material.DIAMOND, Material.COAL, Material.WHEAT, Material.REDSTONE,
                            Material.EMERALD, Material.GOLD_INGOT, Material.MELON,
                            Material.INK_SACK};
                    Material[] fourItemReference = {Material.GLOWSTONE_DUST, Material.CLAY_BALL,
                            Material.SNOW_BALL, Material.QUARTZ};
                    for(ItemStack item : items){
                        if(inv.firstEmpty() != -1){
                            if(item != null){
                                int amount = item.getAmount();
                                for(Material reference : itemReference){
                                    if(item.getType().equals(reference)){
                                        if(amount >= 9){
                                            switch(item.getType()) {
                                            case GOLD_NUGGET:
                                                inv.addItem(new ItemStack(Material.GOLD_INGOT, (int)item.getAmount() / 9));
                                                break;
                                            case EMERALD:
                                                inv.addItem(new ItemStack(Material.EMERALD_BLOCK, (int)item.getAmount() / 9));
                                                break;
                                            case IRON_INGOT:
                                                inv.addItem(new ItemStack(Material.IRON_BLOCK, (int)item.getAmount() / 9));
                                                break;
                                            case COAL:
                                                inv.addItem(new ItemStack(Material.COAL_BLOCK, (int)item.getAmount() / 9));
                                                break;
                                            case DIAMOND:
                                                inv.addItem(new ItemStack(Material.DIAMOND_BLOCK, (int)item.getAmount() / 9));
                                                break;
                                            case WHEAT:
                                                inv.addItem(new ItemStack(Material.HAY_BLOCK, (int)item.getAmount() / 9));
                                                break;
                                            case REDSTONE:
                                                inv.addItem(new ItemStack(Material.REDSTONE_BLOCK, (int)item.getAmount() / 9));
                                                break;
                                            case GOLD_INGOT:
                                                inv.addItem(new ItemStack(Material.GOLD_BLOCK, (int)item.getAmount() / 9));
                                                break;
                                            case MELON:
                                                inv.addItem(new ItemStack(Material.MELON_BLOCK, (int)item.getAmount() / 9));
                                                break;
                                            case INK_SACK:
                                                if(item.getDurability() == (short) 4) {
                                                    inv.addItem(new ItemStack(Material.LAPIS_BLOCK, (int)item.getAmount() / 9));
                                                }
                                                break;
                                            default:
                                                break;
                                            }
                                            if(amount == 9){
                                                inv.removeItem(item);
                                            }
                                            else{
                                                item.setAmount(amount % 9);
                                            }
                                            
                                        }
                                    }
                                }
                                for(Material reference : fourItemReference){
                                    if(item.getType().equals(reference)){
                                        if(amount >= 4){
                                            switch(item.getType()){
                                            case CLAY_BALL:
                                                inv.addItem(new ItemStack(Material.CLAY, (int)item.getAmount() / 4));
                                                break;
                                            case SNOW_BALL:
                                                inv.addItem(new ItemStack(Material.SNOW_BLOCK, (int)item.getAmount() / 4));
                                                break;
                                            case GLOWSTONE_DUST:
                                                inv.addItem(new ItemStack(Material.GLOWSTONE, (int)item.getAmount() / 4));
                                                break;
                                            case QUARTZ:
                                                inv.addItem(new ItemStack(Material.QUARTZ_BLOCK, (int)item.getAmount() / 4));
                                                break;
                                            default:
                                                break;
                                            }
                                            if(amount == 4){
                                                inv.removeItem(item);
                                            }
                                            else{
                                                item.setAmount(amount % 4);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN + " Inventory is full!");
                            break;
                        }
                    }
                    for(ItemStack item : inv.getContents()){
                        if(item != null){
                            inv.removeItem(item);
                            inv.addItem(item);
                        }
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("invcreate")){
                if(player.hasPermission("ItemCondenser.Inventories.Create")){
                    if(args.length >= 1){
                        if(inventory_handler.getInventoryCount(player) < this.getConfig().getInt("Inventories.MaximumPerPlayer") || player.hasPermission("ItemCondenser.Inventories.Create.Infinite")){
                            if(!inventory_handler.hasInventory(player, args[0])){
                                player.openInventory(Bukkit.createInventory(player, 36, args[0]));
                                player.setMetadata("invIsOpen", new EntityMetadata(this, true));
                            }
                            else{
                                player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN + " You already have an inventory named " + ChatColor.LIGHT_PURPLE + args[0]);
                            }
                        }
                        else{
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN + " You have reached your maximum inventory count");
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Invalid arguments");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("invopen")){
                if(player.hasPermission("ItemCondenser.Inventories.Open")){
                    if(args.length >= 1){
                        if(inventory_handler.hasInventory(player, args[0])){
                            Inventory inv = inventory_handler.loadInventory(player, args[0]);
                            player.openInventory(inv);
                            player.setMetadata("invIsOpen", new EntityMetadata(this, true));
                        }
                        else{
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN + " You do not have an inventory named " + ChatColor.LIGHT_PURPLE + args[0]);
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Invalid arguments");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("invremove")){
                if(player.hasPermission("ItemCondenser.Inventories.Remove")){
                    if(args.length >= 1){
                        if(inventory_handler.hasInventory(player, args[0])){
                            inventory_handler.removeInventory(player, args[0]);
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN + " Successfully removed the inventory " + ChatColor.LIGHT_PURPLE + args[0]);
                        }
                        else{
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN + " You do not have an inventory named " + ChatColor.LIGHT_PURPLE + args[0]);
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Invalid arguments");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("invlist")){
                if(player.hasPermission("ItemCondenser.Inventories.List")){
                    Set<String> inventories = inventory_handler.getInventories(player);
                    if(inventories.size() > 0){
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN + " Your inventories:");
                        for(int i = 0; i < inventories.size(); i++){
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "[" + (i + 1) + "] " + ChatColor.GREEN + inventories.toArray()[i]);
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN + " You do not have anu inventories");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("itemname")){
                if(player.hasPermission("ItemCondenser.Items.Name")){
                    if(args.length >= 1){
                        for(int i = 1; i < args.length; i++){
                            args[0] = args[0] + " " + args[i];
                        }
                        ItemStack item = player.getItemInHand();
                        if(item != null){
                            ItemMeta meta = item.getItemMeta();
                            if(args[0].equalsIgnoreCase("remove")){
                                ItemStack refItem = new ItemStack(item.getType());
                                meta.setDisplayName(refItem.getItemMeta().getDisplayName());
                            }
                            else{
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[0]));
                            }
                            item.setItemMeta(meta);
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Invalid arguments");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient arguments");
                }
            }
            else if(commandLabel.equalsIgnoreCase("itemlore")){
                inventory_handler.saveInventory(player, player.getInventory());
                if(player.hasPermission("ItemCondenser.Items.Lore")){
                    if(args.length >= 1){
                        ItemStack item = player.getItemInHand();
                        if(item != null){
                            ItemMeta meta = item.getItemMeta();
                            List<String> lore = meta.getLore();
                            if(lore == null){
                                lore = new ArrayList<String>();
                            }
                            if(args[0].equalsIgnoreCase("remove")){
                                lore.clear();
                                ItemStack refItem = new ItemStack(item.getType());
                                lore = refItem.getItemMeta().getLore();
                            }
                            else{
                                for(int i = 1; i < args.length; i++){
                                    args[0] = args[0] + " " + args[i];
                                }
                                lore.add(ChatColor.translateAlternateColorCodes('&', args[0]));
                            }
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Invalid arguments");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient arguments");
                }
            }
            else if(commandLabel.equalsIgnoreCase("moreitems")){
                if(player.hasPermission("ItemCondenser.Items.More")){
                    ItemStack item = player.getItemInHand();
                    if(item != null){
                        item.setAmount(64);
                    }
                }
            }
        }
        else{
            logger.info("Cannot run this command from the console");
        }
        return true;
    }
}