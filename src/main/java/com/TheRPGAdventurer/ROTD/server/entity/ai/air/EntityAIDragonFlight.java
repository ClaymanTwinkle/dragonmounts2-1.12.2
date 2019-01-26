/*
 ** 2013 July 28
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package com.TheRPGAdventurer.ROTD.server.entity.ai.air;

import com.TheRPGAdventurer.ROTD.DragonMountsConfig;
import com.TheRPGAdventurer.ROTD.server.entity.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.server.entity.ai.EntityAIDragonBase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;

/**
 * Dragon AI for instant landing, if left unmounted in air.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class EntityAIDragonFlight extends EntityAIDragonBase {

	private final double speed;
	private BlockPos landingPos;

	public EntityAIDragonFlight(EntityTameableDragon dragon, double speed) {
		super(dragon);
		this.speed = speed;
		setMutexBits(1);
	}

	public BlockPos findLandingArea(BlockPos pos) {
		for (int Y = 1; Y <= 2; Y++) {
			for (int Z = 1; Z <= 2; Z++) {
				for (int X = 1; X <= 2; X++) {
					    world.getBlockState(new BlockPos(X, Y, Z)).getMaterial().isSolid(); {
						pos = pos.down();
					}				    
				}
			}
		}
		return pos;
	}

	private boolean findLandingBlock() {
		// get current entity position
		landingPos = dragon.getPosition();

		// add some variance
		int followRange = MathHelper.floor(getFollowRange());
		int ox = followRange - random.nextInt(followRange) * 2;
		int oz = followRange - random.nextInt(followRange) * 2;
		landingPos = landingPos.add(ox, 0, oz);

		// get ground block
		landingPos = dragon.world.provider.getDimensionType() == DimensionType.NETHER ? findLandingArea(landingPos) : dragon.world.getHeight(landingPos);


		// make sure the block below is solid
		return world.getBlockState(landingPos.down()).getMaterial().isSolid() ;
	}

	@Override
	public boolean shouldExecute() {
		return !dragon.isInWater() && !dragon.isInLava() && dragon.isFlying() && dragon.getControllingPlayer() == null
				&& findLandingBlock() && dragon.getRevengeTarget() == null && dragon.nothing() && !dragon.isFlyingAround();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return dragon.isFlying() && dragon.getControllingPlayer() == null && !dragon.getNavigator().noPath() && dragon.nothing() && !dragon.isFlyingAround();
	}

	@Override
	public void startExecuting() {
		// try to fly to ground block position
		if (!tryMoveToBlockPos(landingPos, speed) && !dragon.isFlyingAround()) {
			// probably too high, so simply descend vertically
			tryMoveToBlockPos(dragon.getPosition().down(4), speed);
		}
	}
	
	private BlockPos getMidPoint() {
		if(dragon.isTamed() && dragon.hasHomePosition) {
			return dragon.homePos;
		} else {
			return landingPos;
		}
	}
}
