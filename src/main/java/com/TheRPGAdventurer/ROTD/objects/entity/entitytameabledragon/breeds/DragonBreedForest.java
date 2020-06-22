package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.breeds;

import com.TheRPGAdventurer.ROTD.DragonMountsLootTables;
import com.TheRPGAdventurer.ROTD.inits.ModItems;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.objects.items.ItemDragonAmulet;
import com.TheRPGAdventurer.ROTD.objects.items.ItemDragonEssence;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

;


public class DragonBreedForest extends DragonBreed {

    DragonBreedForest() {
        super("forest", 0x298317);

        setImmunity(DamageSource.MAGIC);
        setImmunity(DamageSource.HOT_FLOOR);
        setImmunity(DamageSource.LIGHTNING_BOLT);
        setImmunity(DamageSource.WITHER);

        setHabitatBlock(Blocks.YELLOW_FLOWER);
        setHabitatBlock(Blocks.RED_FLOWER);
        setHabitatBlock(Blocks.MOSSY_COBBLESTONE);
        setHabitatBlock(Blocks.VINE);
        setHabitatBlock(Blocks.SAPLING);
        setHabitatBlock(Blocks.LEAVES);
        setHabitatBlock(Blocks.LEAVES2);

        setHabitatBiome(Biomes.JUNGLE);
        setHabitatBiome(Biomes.JUNGLE_HILLS);

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

    @Override
    public void onLivingUpdate(EntityTameableDragon dragon) {
        Biome biome = dragon.world.getBiome(dragon.getPosition());

        boolean isSavanna = BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)
                || BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY)
                || BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA)
                || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY);

        boolean isTaiga = BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD)
                || BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN);


        if (isSavanna) {
            dragon.setForestType(EnumForestType.DRY.getName());
        } else if (isTaiga) {
            dragon.setForestType(EnumForestType.TAIGA.getName());
        } else {
            dragon.setForestType(EnumForestType.FOREST.getName());
        }
    }

    @Override
    public ItemDragonEssence getDragonEssence(EntityTameableDragon dragon) {
        return ModItems.EssenceForest;
    }

    @Override
    public ItemDragonAmulet getDragonAmulet(EntityTameableDragon dragon) {
        return ModItems.AmuletForest;
    }

    @Override
    public Item getShearItem(EntityTameableDragon dragon) {
        return ModItems.ForestDragonScales;
    }

    @Override
    public ResourceLocation getLootTable(EntityTameableDragon dragon) {
        return DragonMountsLootTables.ENTITIES_DRAGON_FOREST;
    }

    public enum EnumForestType implements IStringSerializable {
        FOREST, TAIGA, DRY;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }
}
	
