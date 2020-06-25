package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.breath.weapons;

import com.TheRPGAdventurer.ROTD.DragonMountsConfig;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.breath.BreathAffectedBlock;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.breath.BreathAffectedEntity;
import com.TheRPGAdventurer.ROTD.util.reflection.PrivateAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by TGG on 5/08/2015.
 *
 * Models the effects of a breathweapon on blocks and entities
 * Usage:
 * 1) Construct with a parent dragon
 * 2) affectBlock() to apply an area of effect to the given block (eg set fire to it)
 * 3) affectEntity() to apply an area of effect to the given entity (eg damage it)
 *
 * Currently does fire only.  Intended to be subclassed later on for different weapon types.
 */
public class BreathWeaponNether extends BreathWeapon implements PrivateAccessor {
	
  public BreathWeaponNether(EntityTameableDragon dragon) {
    super(dragon);
  }
  
  /**
   * Used this to be compatible for Biomes O Plenty, BOP Author made a switch statement on his/her blocks
   * Instead of programming the blocks one by one. I dunno if that was allowed
   * 
   */
  public int processFlammability(Block block, World world, BlockPos sideToIgnite, EnumFacing facing) {
    try {
	      return block.getFlammability(world, sideToIgnite, facing);
	  } catch (IllegalArgumentException e) {
		  return 3;
	  }	 	
  }

  /** if the hitDensity is high enough, manipulate the block (eg set fire to it)
   * @param world
   * @param blockPosition  the world [x,y,z] of the block
   * @param currentHitDensity
   * @return the updated block hit density
   */
  public BreathAffectedBlock affectBlock(World world, Vec3i blockPosition, BreathAffectedBlock currentHitDensity) {
    checkNotNull(world);
    checkNotNull(blockPosition);
    checkNotNull(currentHitDensity);

    BlockPos pos = new BlockPos(blockPosition);
    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();

    Random rand = new Random();

    // Flammable blocks: set fire to them once they have been exposed enough.  After sufficient exposure, destroy the
    //   block (otherwise -if it's raining, the burning block will keep going out)
    // Non-flammable blocks:
    // 1) liquids (except lava) evaporate
    // 2) If the block can be smelted (eg sand), then convert the block to the smelted version
    // 3) If the block can't be smelted then convert to lava

    for (EnumFacing facing : EnumFacing.values()) {
      BlockPos sideToIgnite = pos.offset(facing);
      if (processFlammability(block, world, sideToIgnite, facing) > 0) {
        int flammability = processFlammability(block, world, sideToIgnite, facing);     	
        float thresholdForIgnition = convertFlammabilityToHitDensityThreshold(flammability);
        float densityOfThisFace = currentHitDensity.getHitDensity(facing);
        if (densityOfThisFace >= thresholdForIgnition && world.isAirBlock(sideToIgnite) && DragonMountsConfig.canFireBreathAffectBlocks) {
          burnBlocks(sideToIgnite, rand, 77, world);
        }
      }
    }
    return new BreathAffectedBlock();  // reset to zero
  }

  /** if the hitDensity is high enough, manipulate the entity (eg set fire to it, damage it)
   * A dragon can't be damaged by its own breathweapon;
   * @param world
   * @param entityID  the ID of the affected entity
   * @param currentHitDensity the hit density
   * @return the updated hit density; null if entity dead, doesn't exist, or otherwise not affected
   */
  @Override
  public BreathAffectedEntity affectEntity(World world, Integer entityID, BreathAffectedEntity currentHitDensity) {
    checkNotNull(world);
    checkNotNull(entityID);
    checkNotNull(currentHitDensity);

    Entity entity = world.getEntityByID(entityID);
    if (entity == null || !(entity instanceof EntityLivingBase) || entity.isDead) {
      return null;
    }
    
    if (entityID == dragon.getEntityId()) return null;

    float hitDensity = currentHitDensity.getHitDensity();

    final float DAMAGE_PER_HIT_DENSITY = NETHER_DAMAGE * hitDensity;
    
    triggerDamageExceptionsForFire(entity, entityID, DAMAGE_PER_HIT_DENSITY, currentHitDensity);
    entity.attackEntityFrom(DamageSource.causeMobDamage(dragon), DAMAGE_PER_HIT_DENSITY);

    this.xp(entity);

    return currentHitDensity;
  }

}
