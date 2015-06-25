package com.shortcircuit.itemcondenser.commands;

import com.shortcircuit.itemcondenser.ItemCondenser;
import com.shortcircuit.shortcommands.command.CommandType;
import com.shortcircuit.shortcommands.command.CommandWrapper;
import com.shortcircuit.shortcommands.command.ShortCommand;
import com.shortcircuit.shortcommands.exceptions.BlockOnlyException;
import com.shortcircuit.shortcommands.exceptions.ConsoleOnlyException;
import com.shortcircuit.shortcommands.exceptions.InvalidArgumentException;
import com.shortcircuit.shortcommands.exceptions.NoPermissionException;
import com.shortcircuit.shortcommands.exceptions.PlayerOnlyException;
import com.shortcircuit.shortcommands.exceptions.TooFewArgumentsException;
import com.shortcircuit.shortcommands.exceptions.TooManyArgumentsException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShortCircuit908
 *
 */
public class ItemloreCommand extends ShortCommand{
	public ItemloreCommand(ItemCondenser owning_plugin) {
		super(owning_plugin);
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.PLAYER;
	}

	@Override
	public String[] getCommandNames() {
		return new String[] {"itemlore"};
	}

	@Override
	public String getPermissions() {
		return "itemcondenser.items.lore";
	}

	@Override
	public String[] getHelp() {
		return new String[] {
				ChatColor.GREEN + "Changes the lore an item",
				ChatColor.GREEN + "/${command}",
				ChatColor.GREEN + "Use \"/${command} remove\" to reset the item's lore"};
	}

	@Override
	public String[] exec(CommandWrapper command)
			throws TooFewArgumentsException, TooManyArgumentsException,
			InvalidArgumentException, NoPermissionException,
			PlayerOnlyException, ConsoleOnlyException, BlockOnlyException {
		if(command.getArgs().length < 1) {
			throw new TooFewArgumentsException(command.getCommandLabel());
		}
		Player player = (Player)command.getSender();
		ItemStack item = player.getItemInHand();
		if(item.getType() != Material.AIR){
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			if(command.getArg(0).equalsIgnoreCase("remove")){
				lore = null;
			}
			else{
				if(lore == null){
					lore = new ArrayList<>();
				}
				String message = "";
				for(String arg : command.getArgs()){
					message += " " + arg;
				}
				message = message.trim();
				lore.add(ChatColor.translateAlternateColorCodes('&', message));
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		return new String[] {};
	}

	@Override
	public boolean canBeDisabled() {
		return true;
	}
}
