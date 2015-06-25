package com.shortcircuit.itemcondenser.worldedit;

import com.sk89q.worldedit.blocks.BaseBlock;

import org.bukkit.block.Block;

/**
 * @author ShortCircuit908
 */
public class DelayedBlockChanger implements Runnable {
	private Block block;
	private BaseBlock old_block;

	public DelayedBlockChanger(Block block, BaseBlock old_block) {
		this.block = block;
		this.old_block = old_block;
	}

	@SuppressWarnings("deprecation")
	public void run() {
		block.setTypeId(old_block.getType());
		block.setData((byte) old_block.getData());
	}
}
