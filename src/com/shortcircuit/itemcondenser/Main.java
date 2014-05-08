package com.shortcircuit.itemcondenser;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    static double currentVersion;
    Logger logger = Bukkit.getLogger();
    static HashMap<Player, String> isInvOpen = new HashMap<Player, String>();
    InventoryHandler inventory_handler;
    Updater updater;
    static boolean update = false;
    static String name;
    static String version;
    static String link;
    static ReleaseType type;
    static File file;
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
        updater = new Updater(this, 71867, file, UpdateType.NO_DOWNLOAD, true);
        update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
        name = updater.getLatestName();
        version = updater.getLatestGameVersion();
        link = updater.getLatestFileLink();
        type = updater.getLatestType();
    }
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(commandLabel.equalsIgnoreCase("craft") || commandLabel.equalsIgnoreCase("invcraft")){
                if(player.hasPermission("itemcondenser.craft")){
                    player.openWorkbench(player.getLocation(), true);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("enderchest") || commandLabel.equalsIgnoreCase("invenderchest")
                    || commandLabel.equalsIgnoreCase("ec") || commandLabel.equalsIgnoreCase("invec")){
                if(player.hasPermission("itemcondenser.enderchest")){
                    Inventory enderInv = player.getEnderChest();
                    player.openInventory(enderInv);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("repair") || commandLabel.equalsIgnoreCase("invrepair")){
                if(player.hasPermission("itemcondenser.repair")){
                    //Inventory anvilInv = Bukkit.createInventory(player, InventoryType.ANVIL);
                    //player.openInventory(anvilInv);
                    player.sendMessage(ChatColor.RED + "This feature has not been implemented (waiting for CraftBukkit)");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            /* TODO: Brewing
             * 
             */
            else if(commandLabel.equalsIgnoreCase("brew") || commandLabel.equalsIgnoreCase("invbrew")){
                if(player.hasPermission("itemcondenser.brew")){
                    //Inventory brewInv = Bukkit.createInventory(player, InventoryType.BREWING);
                    //player.openInventory(brewInv);
                    InventoryView view = player.openWorkbench(player.getLocation(), true);
                    int[] spaces = {1, 3, 4, 5, 6};
                    for(int space : spaces){
                        view.setItem(space, new ItemStack(Material.THIN_GLASS, 1));
                    }
                    isInvOpen.put(player, "brew");
                    player.sendMessage(ChatColor.RED + "This feature has not been implemented (waiting for CraftBukkit)");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("smelt") || commandLabel.equalsIgnoreCase("invsmelt")){
                if(player.hasPermission("itemcondenser.smelt")){
                    //Inventory smeltInv = Bukkit.createInventory(player, InventoryType.FURNACE);
                    //player.openInventory(smeltInv);
                    player.sendMessage(ChatColor.RED + "This feature has not been implemented (waiting for CraftBukkit)");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("trash") || commandLabel.equalsIgnoreCase("invtrash")){
                if(player.hasPermission("itemcondenser.trash")){
                    Inventory trashInv = Bukkit.createInventory(null, 36, "Trash");
                    player.openInventory(trashInv);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("clear") || commandLabel.equalsIgnoreCase("invclear")){
                Inventory inv = player.getInventory();
                if(player.hasPermission("itemcondenser.clear")){
                    inv.clear();
                    player.sendMessage(ChatColor.GREEN + "Inventory cleared");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("store") || commandLabel.equalsIgnoreCase("invstore")){
                if(player.hasPermission("itemcondenser.store")){
                    if(player.hasPermission("itemcondenser.store")){
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
                                    short data = item.getDurability();
                                    if(data != 0) {
                                        player.sendMessage(ChatColor.GREEN + "Stored " + ChatColor.LIGHT_PURPLE + item.getType() + ":"
                                                + item.getDurability() + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                                    }
                                    else {
                                        player.sendMessage(ChatColor.GREEN + "Stored " + ChatColor.LIGHT_PURPLE + item.getType()
                                                + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                                    }
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
                                    short data = item.getDurability();
                                    if(data != 0) {
                                        player.sendMessage(ChatColor.GREEN + "Stored " + ChatColor.LIGHT_PURPLE + item.getType() + ":"
                                                + item.getDurability() + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                                    }
                                    else {
                                        player.sendMessage(ChatColor.GREEN + "Stored " + ChatColor.LIGHT_PURPLE + item.getType()
                                                + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
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
            }
            else if(commandLabel.equalsIgnoreCase("storeall") || commandLabel.equalsIgnoreCase("invstoreall")){
                if(player.hasPermission("itemcondenser.storeall")){
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
                                    short data = item.getDurability();
                                    if(data != 0) {
                                        player.sendMessage(ChatColor.GREEN + "Stored " + ChatColor.LIGHT_PURPLE + item.getType() + ":"
                                                + item.getDurability() + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                                    }
                                    else {
                                        player.sendMessage(ChatColor.GREEN + "Stored " + ChatColor.LIGHT_PURPLE + item.getType()
                                                + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                                    }
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
                                    short data = item.getDurability();
                                    if(data != 0) {
                                        player.sendMessage(ChatColor.GREEN + "Stored " + ChatColor.LIGHT_PURPLE + item.getType() + ":"
                                                + item.getDurability() + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                                    }
                                    else {
                                        player.sendMessage(ChatColor.GREEN + "Stored " + ChatColor.LIGHT_PURPLE + item.getType()
                                                + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                                    }
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
            else if(commandLabel.equalsIgnoreCase("drop") || commandLabel.equalsIgnoreCase("invdrop")){
                if(player.hasPermission("itemcondenser.drop")){
                    Inventory inv = player.getInventory();
                    ItemStack item = player.getItemInHand();
                    if(item.getType() != Material.AIR){
                        inv.removeItem(item);
                        Location lookAt = player.getTargetBlock(null, 32).getLocation().add(0.5, 1.5, 0.5);
                        player.getWorld().dropItem(lookAt, item);
                        short data = item.getDurability();
                        if(data != 0) {
                            player.sendMessage(ChatColor.GREEN + "Dropped " + ChatColor.LIGHT_PURPLE + item.getType() + ":"
                                    + item.getDurability() + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                        }
                        else {
                            player.sendMessage(ChatColor.GREEN + "Dropped " + ChatColor.LIGHT_PURPLE + item.getType()
                                    + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                        }
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("dropall") || commandLabel.equalsIgnoreCase("invdropall")){
                if(player.hasPermission("itemcondenser.dropall")){
                    Inventory inv = player.getInventory();
                    ItemStack[] items = inv.getContents();
                    for(ItemStack item : items){
                        if(item != null){
                            inv.removeItem(item);
                            Location lookAt = player.getTargetBlock(null, 32).getLocation();
                            Location dropAt = player.getWorld().getHighestBlockAt(lookAt).getLocation().add(0.5, 1.5, 0.5);
                            player.getWorld().dropItem(dropAt, item);
                            short data = item.getDurability();
                            if(data != 0) {
                                player.sendMessage(ChatColor.GREEN + "Dropped " + ChatColor.LIGHT_PURPLE + item.getType() + ":"
                                        + item.getDurability() + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                            }
                            else {
                                player.sendMessage(ChatColor.GREEN + "Dropped " + ChatColor.LIGHT_PURPLE + item.getType()
                                        + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + item.getAmount());
                            }
                        }
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("sort") || commandLabel.equalsIgnoreCase("invsort")){
                if(player.hasPermission("itemcondenser.sort")){
                    Inventory inv = player.getInventory();
                    Inventory newInv = Bukkit.createInventory(player, 36);
                    Inventory enderInv = player.getEnderChest();
                    ItemStack[] items = inv.getContents();
                    ItemStack[] enderItems = enderInv.getContents();
                    for(ItemStack item : items){
                        if(item != null){
                            inv.removeItem(item);
                            newInv.addItem(item);
                        }
                    }
                    ItemStack[] newItems = newInv.getContents();
                    for(ItemStack item : newItems){
                        if(item != null){
                            newInv.removeItem(item);
                            inv.addItem(item);
                        }
                    }
                    for(ItemStack item : enderItems){
                        if(item != null){
                            enderInv.removeItem(item);
                            newInv.addItem(item);
                        }
                    }
                    ItemStack[] newEnderItems = newInv.getContents();
                    for(ItemStack item : newEnderItems){
                        if(item != null){
                            newInv.removeItem(item);
                            enderInv.addItem(item);
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "Inventory sorted");
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("enchant") || commandLabel.equalsIgnoreCase("invenchant")){
                if(player.hasPermission("itemcondenser.enchant")){
                    player.openEnchanting(player.getLocation(), true);
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("condense") || commandLabel.equalsIgnoreCase("invcondense")){
                if(player.hasPermission("itemcondenser.sort")){
                    player.performCommand("sort");
                }
                Inventory inv = player.getInventory();
                ItemStack[] items = inv.getContents();
                if(player.hasPermission("itemcondenser.condense")){
                    ItemStack[] itemReference = {new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.IRON_INGOT, 9),
                            new ItemStack(Material.DIAMOND), new ItemStack(Material.COAL), new ItemStack(Material.WHEAT),
                            new ItemStack(Material.REDSTONE), new ItemStack(Material.EMERALD), new ItemStack(Material.GOLD_INGOT),
                            new ItemStack(Material.MELON), new ItemStack(Material.INK_SACK, 1, (short) 4)};
                    ItemStack[] fourItemReference = {new ItemStack(Material.GLOWSTONE_DUST), new ItemStack(Material.CLAY_BALL),
                            new ItemStack(Material.SNOW_BALL), new ItemStack(Material.QUARTZ)};
                    for(ItemStack item : items){
                        if(item != null){
                            int amount = item.getAmount();
                            for(ItemStack referenceItem : itemReference){
                                if(item.isSimilar(referenceItem)){
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
                                        if(item.getAmount() == 9){
                                            inv.removeItem(item);
                                        }
                                        else{
                                            item.setAmount(item.getAmount() % 9);
                                        }
                                        short data = item.getDurability();
                                        if(data != 0) {
                                            player.sendMessage(ChatColor.GREEN + "Condensed " + ChatColor.LIGHT_PURPLE + item.getType() + ":"
                                                    + item.getDurability() + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + amount);
                                        }
                                        else {
                                            player.sendMessage(ChatColor.GREEN + "Condensed " + ChatColor.LIGHT_PURPLE + item.getType()
                                                    + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + amount);
                                        }
                                    }
                                }
                            }
                            for(ItemStack referenceItem : fourItemReference){
                                if(item.isSimilar(referenceItem)){
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
                                        if(item.getAmount() == 4){
                                            inv.removeItem(item);
                                        }
                                        else{
                                            item.setAmount(item.getAmount() % 4);
                                        }
                                        short data = item.getDurability();
                                        if(data != 0) {
                                            player.sendMessage(ChatColor.GREEN + "Condensed " + ChatColor.LIGHT_PURPLE + item.getType() + ":"
                                                    + item.getDurability() + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + amount);
                                        }
                                        else {
                                            player.sendMessage(ChatColor.GREEN + "Condensed " + ChatColor.LIGHT_PURPLE + item.getType()
                                                    + ChatColor.GREEN + " x " + ChatColor.LIGHT_PURPLE + amount);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("invcreate")){
                if(player.hasPermission("itemcondenser.invcreate")){
                    inventory_handler.saveInventory(player, player.getInventory());
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("invopen")){
                if(player.hasPermission("itemcondenser.invopen")){
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("invremove")){
                if(player.hasPermission("itemcondenser.invremove")){
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("invlist")){
                if(player.hasPermission("itemcondenser.invlist")){
                }
                else{
                    player.sendMessage(ChatColor.RED + "Insufficient permissions");
                }
            }
            else if(commandLabel.equalsIgnoreCase("itemname")){
                if(player.hasPermission("itemcondenser.itemname")){
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
                if(player.hasPermission("itemcondenser.itemname")){
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
                if(player.hasPermission("itemcondenser.moreitems")){
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
    public static String colorToChar(String original){
        String colChar = "";
        for(ChatColor color : ChatColor.values()){
            switch(color){
            case YELLOW:
                colChar = "e";
                break;
            case WHITE:
                colChar = "f";
                break;
            case UNDERLINE:
                colChar = "n";
                break;
            case STRIKETHROUGH:
                colChar = "m";
                break;
            case RESET:
                colChar = "r";
                break;
            case RED:
                colChar = "c";
                break;
            case MAGIC:
                colChar = "k";
                break;
            case LIGHT_PURPLE:
                colChar = "d";
                break;
            case ITALIC: 
                colChar = "o";
                break;
            case GREEN:
                colChar = "a";
                break;
            case GRAY:
                colChar = "7";
                break;
            case GOLD:
                colChar = "6";
                break;
            case DARK_RED:
                colChar = "4";
                break;
            case DARK_PURPLE:
                colChar = "5";
                break;
            case DARK_GREEN:
                colChar = "2";
                break;
            case DARK_GRAY:
                colChar = "8";
                break;
            case DARK_BLUE:
                colChar = "1";
                break;
            case DARK_AQUA:
                colChar = "3";
                break;
            case BOLD:
                colChar = "l";
                break;
            case BLUE:
                colChar = "9";
                break;
            case BLACK:
                colChar = "0";
                break;
            case AQUA:
                colChar = "b";
                break;
            default: colChar = "";
            }
            original = original.replace(color + "", "&" + colChar);
        }
        return original;
    }
}