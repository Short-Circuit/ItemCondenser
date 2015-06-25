package com.shortcircuit.itemcondenser.utilities;

import com.shortcircuit.itemcondenser.utils.ImmutableBlockState;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author ShortCircuit908
 */
public class Utility {
	private final UUID uuid;
	private final UtilityType type;
	private final ImmutableBlockState old_state;
	private final Block block;
	public Utility(Player player, Block block, UtilityType type){
		uuid = player.getUniqueId();
		this.type = type;
		this.old_state = new ImmutableBlockState(block.getState());
		this.block = block;
	}
	public Block getBlock(){
		return block;
	}
	public ImmutableBlockState getOldState(){
		return old_state;
	}
	public UUID getUuid(){
		return uuid;
	}
	public UtilityType getType(){
		return type;
	}
}
