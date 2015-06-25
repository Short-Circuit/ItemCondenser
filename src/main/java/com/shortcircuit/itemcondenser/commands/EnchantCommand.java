package com.shortcircuit.itemcondenser.commands;

import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.shortcommands.command.CommandType;
import com.shortcircuit.shortcommands.command.CommandWrapper;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.exceptions.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;

/**
 * @author ShortCircuit908
 * 
 */
public class EnchantCommand extends ShortCommand{
	public EnchantCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[] {"enchant", "etable"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.utility.enchant";
	}

	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Opens a portable enchanting table",
				ChatColor.GREEN + "/${command}"};
	}

	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		if (!(command.getSender() instanceof Player)) {
			throw new PlayerOnlyException();
		}
		if (command.getArgs().length < 1) {
			throw new TooFewArgumentsException(command.getCommandLabel());
		}
		Player player = (Player)command.getSender();
		if (command.getArg(0).equalsIgnoreCase("list")) {
			boolean color = false;
			for (Enchantment enchantment : Enchantment.values()) {
				player.sendMessage((color ? ChatColor.YELLOW : ChatColor.GOLD) + getEnchantmentInfo(enchantment));
				color ^= true;
			}
			return new String[]{};
		}
		ItemStack item = player.getItemInHand();
		if (item == null || item.getType().equals(Material.AIR)) {
			return new String[]{"You must be holding an item"};
		}
		ItemMeta meta = item.getItemMeta();
		if (command.getArg(0).equalsIgnoreCase("clear")) {
			Iterator<Enchantment> iterator;
			if (command.getCommandLabel().equalsIgnoreCase("enchantbook") && meta instanceof EnchantmentStorageMeta) {
				iterator = ((EnchantmentStorageMeta) meta).getStoredEnchants().keySet().iterator();
				while (iterator.hasNext()) {
					((EnchantmentStorageMeta) meta).removeStoredEnchant(iterator.next());
				}
			}
			else {
				iterator = meta.getEnchants().keySet().iterator();
				while (iterator.hasNext()) {
					meta.removeEnchant(iterator.next());
				}
			}

			player.getItemInHand().setItemMeta(meta);
			player.getPlayer().updateInventory();
			return new String[]{"Enchantments cleared"};
		}
		Enchantment enchantment = getEnchantment(command.getArg(0));
		if (enchantment == null) {
			return new String[]{command.getArg(0) + " is not a valid enchantment", "Use \"/enchant list\" to view enchantments"};
		}
		int level = enchantment.getStartLevel();
		if (command.getArgs().length > 1) {
			try {
				level = Short.parseShort(command.getArg(1));
			}
			catch (NumberFormatException e) {
				return new String[]{"Level must be a valid integer between -32768 and 32767"};
			}
		}
		if (command.getCommandLabel().equalsIgnoreCase("enchantbook") && meta instanceof EnchantmentStorageMeta) {
			((EnchantmentStorageMeta) meta).addStoredEnchant(enchantment, level, true);
		}
		else {
			meta.addEnchant(enchantment, level, true);
		}
		player.getItemInHand().setItemMeta(meta);
		player.getPlayer().updateInventory();
		return new String[]{"*Poof*"};
	}

	private Enchantment getEnchantment(String name) {
		switch (name.toLowerCase().replace("_", "")) {
			case "0":
			case "protect":
			case "protection":
			case "protectionenvironmental":
				return Enchantment.PROTECTION_ENVIRONMENTAL;
			case "1":
			case "fireprotect":
			case "fireprotection":
			case "protectionfire":
				return Enchantment.PROTECTION_FIRE;
			case "2":
			case "featherfall":
			case "featherfalling":
			case "fallprotect":
			case "fallprotection":
			case "protectionfall":
				return Enchantment.PROTECTION_FALL;
			case "3":
			case "blastprotect":
			case "blastprotection":
			case "explosionprotect":
			case "explosionprotection":
			case "protectionexplosions":
				return Enchantment.PROTECTION_EXPLOSIONS;
			case "4":
			case "projectileprotect":
			case "projectileprotection":
			case "arrowprotect":
			case "arrowprotection":
			case "protectionprojectile":
				return Enchantment.PROTECTION_PROJECTILE;
			case "5":
			case "breathing":
			case "respiration":
			case "oxygen":
				return Enchantment.OXYGEN;
			case "6":
			case "aquaaffinity":
			case "wateraffinity":
			case "waterworker":
				return Enchantment.WATER_WORKER;
			case "7":
			case "retaliate":
			case "thorns":
				return Enchantment.THORNS;
			case "8":
			case "depthstrider":
				return Enchantment.DEPTH_STRIDER;
			case "16":
			case "sharpness":
			case "damageall":
				return Enchantment.DAMAGE_ALL;
			case "17":
			case "smite":
			case "damageundead":
				return Enchantment.DAMAGE_UNDEAD;
			case "18":
			case "damagespiders":
			case "baneofarthropods":
			case "damagearthropods":
				return Enchantment.DAMAGE_ARTHROPODS;
			case "19":
			case "knockback":
				return Enchantment.KNOCKBACK;
			case "20":
			case "fire":
			case "fireaspect":
				return Enchantment.FIRE_ASPECT;
			case "21":
			case "looting":
			case "lootbonusmobs":
				return Enchantment.LOOT_BONUS_MOBS;
			case "32":
			case "efficiency":
			case "digspeed":
				return Enchantment.DIG_SPEED;
			case "33":
			case "silktouch":
				return Enchantment.SILK_TOUCH;
			case "34":
			case "unbreaking":
			case "durability":
				return Enchantment.DURABILITY;
			case "35":
			case "fortune":
			case "lootbonusblocks":
				return Enchantment.LOOT_BONUS_BLOCKS;
			case "48":
			case "power":
			case "arrowdamage":
				return Enchantment.ARROW_DAMAGE;
			case "49":
			case "punch":
			case "arrowknockback":
				return Enchantment.ARROW_KNOCKBACK;
			case "50":
			case "flame":
			case "arrowfire":
				return Enchantment.ARROW_FIRE;
			case "51":
			case "infinity":
			case "arrowinfinite":
				return Enchantment.ARROW_INFINITE;
			case "61":
			case "luckofthesea":
			case "luck":
				return Enchantment.LUCK;
			case "62":
			case "lure":
				return Enchantment.LURE;
			default:
				Enchantment enchantment = Enchantment.getByName(name.toUpperCase());
				if (enchantment == null) {
					try {
						enchantment = Enchantment.getById(Integer.parseInt(name));
					}
					catch (NumberFormatException e) {
						// Do nothing
					}
				}
				return enchantment;
		}
	}

	private String getEnchantmentInfo(Enchantment enchantment) {
		StringBuilder s_builder = new StringBuilder(enchantment.getName())
				.append("(")
				.append(enchantment.getId())
				.append(") Min=")
				.append(enchantment.getStartLevel())
				.append(" Max=")
				.append(enchantment.getMaxLevel());
		if (enchantment.getItemTarget() != null) {
			s_builder.append(" Tool=")
					.append(enchantment.getItemTarget());
		}
		return s_builder.toString();
	}
	
	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
