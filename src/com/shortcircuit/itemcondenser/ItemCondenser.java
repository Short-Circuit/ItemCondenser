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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.yi.acru.bukkit.Lockette.Lockette;

public final class ItemCondenser extends JavaPlugin{
    public Logger logger = Bukkit.getLogger();
    public InventoryHandler inventory_handler;
    public File file;
    public boolean lockette = false;
    public void onEnable(){
        logger.info("[ItemCondenser] ItemCondenser by ShortCircuit908");
        logger.info("[ItemCondenser] ItemCondenser enabled");
        inventory_handler = new InventoryHandler(getDataFolder().toString());
        File configFile = new File(getDataFolder() + "/config.yml");
        if(!configFile.exists()){
            logger.info("[ItemCondenser] No configuration file found, creating one");
            saveDefaultConfig();
        }
        file = getFile();
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        try{
            String lockVersion = Lockette.getCoreVersion();
            Bukkit.getLogger().info("[ItemCondenser] Successfully hooked into Lockette v" + lockVersion);
            lockette = true;
        }
        catch(NoClassDefFoundError e){
        }
    }
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        // Make sure the inventories are up-to-date
        inventory_handler.reloadInventories();
        // Commands can be disabled in case they conflict with another plugin's
        if(getConfig().getBoolean("DisabledCommands." + commandLabel.toLowerCase())){
            return false;
        }
        // These are player-only commands
        if(sender instanceof Player){
            Player player = (Player) sender;
            /*
             * TODO: Crafting
             */
            if(commandLabel.equalsIgnoreCase("ctable")){
                // Check permissions
                if(player.hasPermission("ItemCondenser.Utility.Craft")){
                    // Open a crafting table
                    player.openWorkbench(player.getLocation(), true);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: Ender chest
             */
            else if(commandLabel.equalsIgnoreCase("echest")){
                // Check permissions
                if(player.hasPermission("ItemCondenser.Utility.EnderChest")){
                    // Get and open the player's ender chest
                    Inventory enderInv = player.getEnderChest();
                    player.openInventory(enderInv);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: Enchanting table
             */
            else if(commandLabel.equalsIgnoreCase("etable")){
                if(player.hasPermission("ItemCondenser.Utility.Enchant")){
                    player.openEnchanting(player.getLocation(), true);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: Repairing (will crash the server)
             */
            else if(commandLabel.equalsIgnoreCase("anvil")){
                // Check permissions
                if(player.hasPermission("ItemCondenser.Utility.Repair")){
                    //Inventory anvilInv = Bukkit.createInventory(player, InventoryType.ANVIL);
                    //player.openInventory(anvilInv);
                    player.sendMessage(ChatColor.RED + "This feature has not been implemented (WIP)");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: Brewing (will crash the server)
             */
            else if(commandLabel.equalsIgnoreCase("brew")){
                // Check permissions
                if(player.hasPermission("ItemCondenser.Utility.Brew")){
                    //Inventory brewInv = Bukkit.createInventory(player, InventoryType.BREWING);
                    //player.openInventory(brewInv);
                    player.sendMessage(ChatColor.RED + "This feature has not been implemented (WIP)");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: Smelting (will crash the server)
             */
            else if(commandLabel.equalsIgnoreCase("smelt")){
                // Check permissions
                if(player.hasPermission("ItemCondenser.Utility.Smelt")){
                    //Inventory smeltInv = Bukkit.createInventory(player, InventoryType.FURNACE);
                    //player.openInventory(smeltInv);
                    player.sendMessage(ChatColor.RED + "This feature has not been implemented (WIP)");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: Trash
             */
            else if(commandLabel.equalsIgnoreCase("trash")){
                // Check permissions
                if(player.hasPermission("ItemCondenser.Utility.Trash")){
                    // Open a temporary inventory
                    Inventory trashInv = Bukkit.createInventory(null, 36, "Trash");
                    player.openInventory(trashInv);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: Clear
             */
            else if(commandLabel.equalsIgnoreCase("clear")){
                // Check permissions
                if(player.hasPermission("ItemCondenser.Clear")){
                    // Use the player's inventory as the default
                    Inventory inv = player.getInventory();
                    // Allow the player to target a chest using /clear chest
                    if(args.length >= 1){
                        if(args[0].equalsIgnoreCase("chest")){
                            // Get the targeted block
                            Block block = player.getTargetBlock(null, 5);
                            boolean isPrivate = false;
                            // If Lockette is installed, check if the block is protected
                            if(lockette){
                                isPrivate = Lockette.isProtected(block);
                            }
                            // Check if the targeted block is a chest
                            if(block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)){
                                // If the chest is private, check if the player is an allowed user
                                if(isPrivate){
                                    if(!Lockette.isOwner(block, player.getName()) && !Lockette.isUser(block, player.getName(), true)
                                            && !Lockette.isEveryone(block)){
                                        // If the player is not an allowed user, tell them to GTFO
                                        player.sendMessage(ChatColor.RED + "That chest is private!");
                                        return true;
                                    }
                                }
                                // Otherwise, get the inventory of the chest
                                Chest chest = (Chest)block.getState();
                                inv = chest.getInventory();
                            }
                            // Check if the targeted block is an ender chest
                            else if(block.getType().equals(Material.ENDER_CHEST)){
                                inv = player.getEnderChest();
                            }
                            else{
                                player.sendMessage(ChatColor.RED + "No chest to clear");
                                return true;
                            }
                        }
                        else{
                            player.sendMessage(ChatColor.RED + "Invalid arguments");
                        }
                    }
                    // Clear the inventory
                    inv.clear();
                    player.sendMessage(ChatColor.GREEN + "Inventory cleared");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: Store items in a chest
             */
            else if(commandLabel.equalsIgnoreCase("store")){
                // Check permissions
                if(player.hasPermission("ItemCondenser.Items.Store")){
                    // Get the player's inventory, and the held item
                    Inventory inv = player.getInventory();
                    ItemStack item = player.getItemInHand();
                    // Get the block the player is targeting
                    Block block = player.getTargetBlock(null, 32);
                    boolean isPrivate = false;
                    // If Lockette is installed, check if the block is protected
                    if(lockette){
                        isPrivate = Lockette.isProtected(block);
                    }
                    // Check if the targeted block is a chest
                    if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST){
                        // If the chest is private, check if the player is an allowed user
                        if(isPrivate){
                            if(!Lockette.isOwner(block, player.getName()) && !Lockette.isUser(block, player.getName(), true)
                                    && !Lockette.isEveryone(block)){
                                // If the player is not an allowed user, tell them to GTFO
                                player.sendMessage(ChatColor.RED + "That chest is private!");
                                return true;
                            }
                        }
                        // Otherwise, get the inventory of the chest
                        Chest chest = (Chest)block.getState();
                        // Make sure the player is holding an item
                        if(item != null && item.getType() != Material.AIR){
                            // Make sure there is room in the chest
                            if(chest.getInventory().firstEmpty() == -1){
                                player.sendMessage(ChatColor.RED + "Chest is full");
                            }
                            else{
                                // Move an item into the chest
                                inv.removeItem(item);
                                chest.getInventory().addItem(item);
                            }
                        }
                    }
                    // Check if the targeted block is an ender chest
                    else if(block.getType() == Material.ENDER_CHEST){
                        // Get the player's ender chest
                        Inventory enderInv = player.getEnderChest();
                        // Make sure the player is holding an item
                        if(item != null && item.getType() != Material.AIR){
                            // Make sure there is room in the ender chest
                            if(enderInv.firstEmpty() == -1){
                                player.sendMessage(ChatColor.RED + "Ender chest is full");
                            }
                            else{
                                // Move an item into the ender chest
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
                return true;
            }
            /*
             * TODO: Store all items in a chest
             */
            else if(commandLabel.equalsIgnoreCase("storeall")){
                if(player.hasPermission("ItemCondenser.Items.Store.All")){
                    Inventory inv = player.getInventory();
                    ItemStack[] items = inv.getContents();
                    Block block = player.getTargetBlock(null, 32);
                    boolean isPrivate = false;
                    if(lockette){
                        isPrivate = Lockette.isProtected(block);
                    }
                    if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST){
                        if(isPrivate){
                            if(!Lockette.isOwner(block, player.getName()) && !Lockette.isUser(block, player.getName(), true)
                                    && !Lockette.isEveryone(block)){
                                player.sendMessage(ChatColor.RED + "That chest is private!");
                                return true;
                            }
                        }
                        Chest chest = (Chest)block.getState();
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
                        if(isPrivate){
                            if(!Lockette.isOwner(block, player.getName()) && !Lockette.isUser(block, player.getName(), true)
                                    && !Lockette.isEveryone(block)){
                                player.sendMessage(ChatColor.RED + "That chest is private!");
                                return true;
                            }
                        }
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
                return true;
            }
            /*
             * TODO: Drop an item
             */
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
                return true;
            }
            /*
             * TODO: Drop all items in a player's inventory
             */
            else if(commandLabel.equalsIgnoreCase("dropall")){
                if(player.hasPermission("ItemCondenser.Items.Drop.All")){
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
                return true;
            }
            /*
             * TODO: Sort the player's inventory
             */
            else if(commandLabel.equalsIgnoreCase("sort")){
                if(player.hasPermission("ItemCondenser.Sort")){
                    Inventory inv = player.getInventory();
                    if(args.length >= 1){
                        if(args[0].equalsIgnoreCase("chest")){
                            Block block = player.getTargetBlock(null, 5);
                            boolean isPrivate = false;
                            if(lockette){
                                isPrivate = Lockette.isProtected(block);
                            }
                            if(block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)){
                                if(isPrivate){
                                    if(!Lockette.isOwner(block, player.getName()) && !Lockette.isUser(block, player.getName(), true)
                                            && !Lockette.isEveryone(block)){
                                        player.sendMessage(ChatColor.RED + "That chest is private!");
                                        return true;
                                    }
                                }
                                Chest chest = (Chest)block.getState();
                                inv = chest.getInventory();
                            }
                            else if(block.getType().equals(Material.ENDER_CHEST)){
                                inv = player.getEnderChest();
                            }
                            else{
                                player.sendMessage(ChatColor.RED + "No chest to sort");
                                return true;
                            }
                        }
                        else{
                            player.sendMessage(ChatColor.RED + "Invalid arguments");
                        }
                    }
                    Inventory newInv = Bukkit.createInventory(player, 54);
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
                return true;
            }
            /*
             * TODO: Condensing
             */
            else if(commandLabel.equalsIgnoreCase("condense")){
                if(player.hasPermission("ItemCondenser.Condense")){
                    Inventory inv = player.getInventory();
                    if(args.length >= 1){
                        if(args[0].equalsIgnoreCase("chest")){
                            Block block = player.getTargetBlock(null, 5);
                            boolean isPrivate = false;
                            if(lockette){
                                isPrivate = Lockette.isProtected(block);
                            }
                            if(block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)){
                                if(isPrivate){
                                    if(!Lockette.isOwner(block, player.getName()) && !Lockette.isUser(block, player.getName(), true)){
                                        player.sendMessage(ChatColor.RED + "That chest is private!");
                                        return true;
                                    }
                                }
                                Chest chest = (Chest)block.getState();
                                inv = chest.getInventory();
                            }
                            else if(block.getType().equals(Material.ENDER_CHEST)){
                                inv = player.getEnderChest();
                            }
                            else{
                                player.sendMessage(ChatColor.RED + "No chest to condense");
                                return true;
                            }
                        }
                        else{
                            player.sendMessage(ChatColor.RED + "Invalid arguments");
                        }
                        return true;
                    }
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
                                                /*
                                            case QUARTZ:
                                                inv.addItem(new ItemStack(Material.QUARTZ_BLOCK, (int)item.getAmount() / 4));
                                                break;
                                                 */
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
                return true;
            }
            /*
             * TODO: Create an additional inventory
             */
            else if(commandLabel.equalsIgnoreCase("invcreate")){
                if(player.hasPermission("ItemCondenser.Inventories.Create")){
                    if(args.length >= 1){
                        if(inventory_handler.getInventoryCount(player) < getConfig().getInt("Inventories.MaximumPerPlayer")
                                || player.hasPermission("ItemCondenser.Inventories.Create.Infinite")){
                            if(!inventory_handler.hasInventory(player, args[0])){
                                player.openInventory(Bukkit.createInventory(player, 36, args[0]));
                                player.setMetadata("invIsOpen", new EntityMetadata(this, true));
                            }
                            else{
                                player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
                                        + " You already have an inventory named " + ChatColor.LIGHT_PURPLE + args[0]);
                            }
                        }
                        else{
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
                                    + " You have reached your maximum inventory count");
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Invalid arguments");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: Open an additional inventory
             */
            else if(commandLabel.equalsIgnoreCase("invopen")){
                if(player.hasPermission("ItemCondenser.Inventories.Open")){
                    if(args.length >= 1){
                        if(inventory_handler.hasInventory(player, args[0])){
                            Inventory inv = inventory_handler.loadInventory(player, args[0]);
                            player.openInventory(inv);
                            player.setMetadata("invIsOpen", new EntityMetadata(this, true));
                        }
                        else{
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
                                    + " You do not have an inventory named " + ChatColor.LIGHT_PURPLE + args[0]);
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Invalid arguments");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: Remove an additional inventory
             */
            else if(commandLabel.equalsIgnoreCase("invremove")){
                if(player.hasPermission("ItemCondenser.Inventories.Remove")){
                    if(args.length >= 1){
                        if(inventory_handler.hasInventory(player, args[0])){
                            inventory_handler.removeInventory(player, args[0]);
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
                                    + " Successfully removed the inventory " + ChatColor.LIGHT_PURPLE + args[0]);
                        }
                        else{
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "[ItemCondenser]" + ChatColor.GREEN
                                    + " You do not have an inventory named " + ChatColor.LIGHT_PURPLE + args[0]);
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "Invalid arguments");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
                return true;
            }
            /*
             * TODO: List a player's additional inventories
             */
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
                return true;
            }
            /*
             * TODO: Rename an item
             */
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
                return true;
            }
            /*
             * TODO: Change an item's lore
             */
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
                return true;
            }
            /*
             * TODO: Give a full stack of items
             */
            else if(commandLabel.equalsIgnoreCase("moreitems")){
                if(player.hasPermission("ItemCondenser.Items.More")){
                    ItemStack item = player.getItemInHand();
                    if(item != null){
                        item.setAmount(64);
                    }
                }
                return true;
            }
        }
        else{
            logger.info("Cannot run this command from the console");
        }
        return false;
    }
}