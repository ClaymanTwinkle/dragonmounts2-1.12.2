package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.breeds;

import com.TheRPGAdventurer.ROTD.DragonMountsLootTables;
import com.TheRPGAdventurer.ROTD.inits.ModItems;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;

import com.TheRPGAdventurer.ROTD.objects.items.ItemDragonAmulet;
import com.TheRPGAdventurer.ROTD.objects.items.ItemDragonEssence;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

public class DragonBreedEnchant extends DragonBreed {

	DragonBreedEnchant() {
		super("enchant", 0x8359ae);
		
        setImmunity(DamageSource.MAGIC);
        setImmunity(DamageSource.HOT_FLOOR);
        setImmunity(DamageSource.LIGHTNING_BOLT);
        setImmunity(DamageSource.WITHER);
        
        setHabitatBlock(Blocks.BOOKSHELF);
        setHabitatBlock(Blocks.ENCHANTING_TABLE);
	}

	@Override
	public void onEnable(EntityTameableDragon dragon) {}

	@Override
	public void onDisable(EntityTameableDragon dragon) {}

	@Override
	public void onDeath(EntityTameableDragon dragon) {}
	
	@Override
	public void onLivingUpdate(EntityTameableDragon dragon) {
		doParticles(dragon);
	}

	@Override
	public ItemDragonEssence getDragonEssence(EntityTameableDragon dragon) {
		return ModItems.EssenceEnchant;
	}

	@Override
	public ItemDragonAmulet getDragonAmulet(EntityTameableDragon dragon) {
		return ModItems.AmuletEnchant;
	}

	@Override
	public Item getShearItem(EntityTameableDragon dragon) {
		return ModItems.EnchantDragonScales;
	}

	@Override
	public ResourceLocation getLootTable(EntityTameableDragon dragon) {
		return DragonMountsLootTables.ENTITIES_DRAGON_ENCHANT;
	}

	private void doParticles(EntityTameableDragon dragon) {
        if (!dragon.isEgg() && !dragon.isBaby()) {
	        float s = dragon.getScale() * 1.2f;
	        for (double x1 = 0; x1 < s + 25; ++x1) {
		        double x = dragon.posX + (rand.nextDouble() - 0.5) * (dragon.width - 1.2) * s;
		        double y = dragon.posY + (rand.nextDouble() - 0.5) * dragon.height * s;
		        double z = dragon.posZ + (rand.nextDouble() - 0.5) * (dragon.width - 1.2) * s;
		        
		        dragon.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, x, y, z, 0, 0, 0);
	        }
        }
    }
}
