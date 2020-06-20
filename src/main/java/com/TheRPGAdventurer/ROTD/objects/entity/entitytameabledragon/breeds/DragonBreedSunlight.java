package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.breeds;

import com.TheRPGAdventurer.ROTD.DragonMountsLootTables;
import com.TheRPGAdventurer.ROTD.inits.ModItems;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;

import com.TheRPGAdventurer.ROTD.objects.items.ItemDragonAmulet;
import com.TheRPGAdventurer.ROTD.objects.items.ItemDragonEssence;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

public class DragonBreedSunlight extends DragonBreed {

	DragonBreedSunlight() {
		super("sunlight", 0Xffde00);
		
		setHabitatBlock(Blocks.DAYLIGHT_DETECTOR);
		setHabitatBlock(Blocks.GLOWSTONE);
		setHabitatBlock(Blocks.YELLOW_GLAZED_TERRACOTTA);
	}

	@Override
	public void onEnable(EntityTameableDragon dragon) {}

	@Override
	public void onDisable(EntityTameableDragon dragon) {}

	@Override
	public void onDeath(EntityTameableDragon dragon) {}
	
	@Override
	public void onLivingUpdate(EntityTameableDragon dragon) {
		if(dragon.posY > dragon.world.getHeight() + 8 && dragon.world.isDaytime()) doParticles(dragon);
	}

	@Override
	public ItemDragonEssence getDragonEssence(EntityTameableDragon dragon) {
		return ModItems.EssenceSunlight;
	}

	@Override
	public ItemDragonAmulet getDragonAmulet(EntityTameableDragon dragon) {
		return ModItems.AmuletSunlight;
	}

	@Override
	public Item getShearItem(EntityTameableDragon dragon) {
		return dragon.isMale() ? ModItems.SunlightDragonScales : ModItems.SunlightDragonScales2;
	}

	@Override
	public ResourceLocation getLootTable(EntityTameableDragon dragon) {
		return dragon.isMale() ? DragonMountsLootTables.ENTITIES_DRAGON_SUNLIGHT : DragonMountsLootTables.ENTITIES_DRAGON_SUNLIGHT2;
	}

	private void doParticles(EntityTameableDragon dragon) {
        if (!dragon.isEgg() && !dragon.isBaby()) {
	        float s = dragon.getScale() * 1.2f;
	        for (double x1 = 0; x1 < s + 2; ++x1) {
		        double x = dragon.posX + (rand.nextDouble() - 0.5) * (dragon.width - 0.65) * s;
		        double y = dragon.posY + (rand.nextDouble() - 0.5) * dragon.height * s;
		        double z = dragon.posZ + (rand.nextDouble() - 0.5) * (dragon.width - 0.65) * s;
		        
		        dragon.world.spawnParticle(EnumParticleTypes.CRIT, x, y, z, 0, 0, 0);
	        }
        }
    }
}
