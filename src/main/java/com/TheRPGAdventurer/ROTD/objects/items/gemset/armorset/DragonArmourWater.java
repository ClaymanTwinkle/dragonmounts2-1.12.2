package com.TheRPGAdventurer.ROTD.objects.items.gemset.armorset;

import com.TheRPGAdventurer.ROTD.inits.ModArmour;
import com.TheRPGAdventurer.ROTD.objects.items.EnumItemBreedTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class DragonArmourWater extends DragonArmourBase {
	
	public DragonArmourWater(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String unlocalizedName) {
		super(materialIn, renderIndexIn, equipmentSlotIn, unlocalizedName, EnumItemBreedTypes.WATER);
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (player.getCooldownTracker().getCooldown(this, 0) > 0) return;
		super.onArmorTick(world, player, itemStack);
		if (!(head == ModArmour.waterDragonScaleCap && chest == ModArmour.waterDragonscaleChesplate && legs == ModArmour.waterDragonScaleLeggings && feet == ModArmour.waterDragonScaleBoots))
			return;
		if (!player.isInWater()) return;
		
		player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 300));
		player.getCooldownTracker().setCooldown(this, 70);
	}
	
}
