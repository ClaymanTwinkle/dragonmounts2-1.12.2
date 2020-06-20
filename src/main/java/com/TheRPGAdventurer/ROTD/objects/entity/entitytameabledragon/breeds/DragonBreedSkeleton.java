package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.breeds;

import com.TheRPGAdventurer.ROTD.DragonMountsLootTables;
import com.TheRPGAdventurer.ROTD.inits.ModItems;
import com.TheRPGAdventurer.ROTD.inits.ModSounds;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.breath.BreathNode;
import com.TheRPGAdventurer.ROTD.objects.items.ItemDragonAmulet;
import com.TheRPGAdventurer.ROTD.objects.items.ItemDragonEssence;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class DragonBreedSkeleton extends DragonBreed {

    DragonBreedSkeleton() {
        super("skeleton", 0xffffff);

        setHabitatBlock(Blocks.BONE_BLOCK);
        setImmunity(DamageSource.LIGHTNING_BOLT);
        setImmunity(DamageSource.WITHER);
    }

    @Override
    public boolean isHabitatEnvironment(EntityTameableDragon dragon) {
        if (dragon.posY > dragon.world.getHeight() * 0.25) {
            // woah dude, too high!
            return false;
        }

        int bx = MathHelper.floor(dragon.posX);
        int by = MathHelper.floor(dragon.posY);
        int bz = MathHelper.floor(dragon.posZ);
        BlockPos blockPos = new BlockPos(bx, by, bz);

        // too bright!
        return dragon.world.getLightBrightness(blockPos) < 4;
    }

    @Override
    public void onEnable(EntityTameableDragon dragon) {
    }

    @Override
    public void onDisable(EntityTameableDragon dragon) {
    }

    @Override
    public void onDeath(EntityTameableDragon dragon) {
    }

    public SoundEvent getLivingSound(EntityTameableDragon dragon) {
        if (dragon.isBaby()) {
            return ModSounds.ENTITY_DRAGON_HATCHLING_GROWL;
        } else {
            return ModSounds.ENTITY_SKELETON_DRAGON_GROWL;
        }
    }

//	@Override
//	public boolean canChangeBreed() {return false;}

    @Override
    public boolean canUseBreathWeapon() {
        return false;
    }

    @Override
    public void continueAndUpdateBreathing(World world, Vec3d origin, Vec3d endOfLook, BreathNode.Power power, EntityTameableDragon dragon) {
        return;
    }

//	@Override
//	public boolean isInfertile() {
//		return true;
//	}

    @Override
    public EnumParticleTypes getSneezeParticle() {
        return null;
    }

    @Override
    public double getBreedHealth() {
        double base = super.getBreedHealth();
        return base - (base < 16d ? 0d : 15d);
    }

    @Override
    public ItemDragonEssence getDragonEssence(EntityTameableDragon dragon) {
        return ModItems.EssenceSkeleton;
    }

    @Override
    public ItemDragonAmulet getDragonAmulet(EntityTameableDragon dragon) {
        return ModItems.AmuletSkeleton;
    }

    @Override
    public Item getShearItem(EntityTameableDragon dragon) {
        return null;
    }

    @Override
    public ResourceLocation getLootTable(EntityTameableDragon dragon) {
        return DragonMountsLootTables.ENTITIES_DRAGON_SKELETON;
    }

}
	
