package com.TheRPGAdventurer.ROTD.server.items;

import com.TheRPGAdventurer.ROTD.DragonMounts;
import com.TheRPGAdventurer.ROTD.server.entity.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.server.entity.breeds.EnumDragonBreed;
import com.TheRPGAdventurer.ROTD.server.initialization.EnumItemBreedTypes;
import com.TheRPGAdventurer.ROTD.server.initialization.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemDragonAmulet extends Item {

    EnumItemBreedTypes type;
    EnumDragonBreed breed;
    ItemStack stack;
    public EntityAreaEffectCloud entityareaeffectcloud;

    public ItemDragonAmulet(@Nullable EnumItemBreedTypes type, @Nullable EnumDragonBreed breed) {
        String name = type.toString().toLowerCase() + "_dragon_amulet";
        this.type = type;
        this.breed = breed;
        this.setRegistryName(new ResourceLocation(DragonMounts.MODID, name));
        this.setUnlocalizedName(name);
        this.setMaxStackSize(1);
//        this.setCreativeTab(DragonMounts.TAB);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return !itemstack.isEmpty() && itemstack.getItem() instanceof ItemDragonAmulet ? (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        this.stack = stack;
        stack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entity, int itemSlot, boolean isSelected) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        } else if (stack.getTagCompound().getBoolean("Released")) {
            stack.shrink(1);
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                player.inventory.setInventorySlotContents(itemSlot, new ItemStack(ModItems.AmuletEmpty));
            }
        }
    }

    /**
     * Thanks again Lex!
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        EntityTameableDragon dragon = new EntityTameableDragon(worldIn);

        dragon.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());

        if (stack.getTagCompound() != null) {
            dragon.readEntityFromNBT(stack.getTagCompound());
        }

        if (stack.hasDisplayName()) {
            dragon.setCustomNameTag(stack.getDisplayName());
        }

        dragon.setBreedType(breed);
        stack.getTagCompound().setBoolean("Released", true);
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(dragon);
        }

        stack = new ItemStack(ModItems.AmuletEmpty);
        worldIn.playSound(player, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1, 0.77f);

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);

    }

}