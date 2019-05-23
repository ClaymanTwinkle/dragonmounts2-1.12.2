package com.TheRPGAdventurer.ROTD.inits;

import java.util.ArrayList;
import java.util.List;

import com.TheRPGAdventurer.ROTD.objects.blocks.BlockDragonNest;
import com.TheRPGAdventurer.ROTD.objects.blocks.BlockDragonShulker;

import net.minecraft.block.Block;

public class ModBlocks
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block NESTBLOCK = new BlockDragonNest("pileofsticks");
	public static final Block dragonshulker = new BlockDragonShulker("block_dragon_shulker");
//	public static final Block pileofsticks = new BlockBase("pileofstickssnow");
	
}