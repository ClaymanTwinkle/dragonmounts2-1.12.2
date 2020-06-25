package com.TheRPGAdventurer.ROTD.objects.entity;

import com.TheRPGAdventurer.ROTD.DragonMounts;
import com.TheRPGAdventurer.ROTD.client.gui.GuiHandler;
import com.TheRPGAdventurer.ROTD.inits.ModItems;
import com.TheRPGAdventurer.ROTD.inits.ModTools;
import com.TheRPGAdventurer.ROTD.objects.blocks.BlockDragonBreedEgg;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.util.DMUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

public class DragonProcessInteractUtils {
    public static boolean processInteract(EntityTameableDragon dragon, EntityPlayer player, @Nullable EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        /*
         * Turning it to block
         */
        if (dragon.isEgg() && player.isSneaking()) {
            dragon.world.playSound(player, dragon.getPosition(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 0.5F, 1);
            dragon.world.setBlockState(dragon.getPosition(), BlockDragonBreedEgg.DRAGON_BREED_EGG.getStateFromMeta(dragon.getBreedType().getMeta()));
            dragon.setDead();
            return true;
        }

        /*
         * Dragon Riding The Player
         */
        if (dragon.isAllowed(player) && !dragon.isSitting() && dragon.isBaby() && !hasInteractItemsEquipped(player) && !player.isSneaking() && player.getPassengers().size() < 3) {
            dragon.setAttackTarget(null);
            dragon.getNavigator().clearPath();
            dragon.getAISit().setSitting(false);
            dragon.startRiding(player, true);
            return true;
        }

        // prevent doing any interactions when a hatchling rides you, the hitbox could block the player's raytraceresult for rightclick
        if (player.isPassenger(dragon)) return false;

        if (dragon.isServer() && !dragon.isEgg()) {
            if (dragon.isAllowed(player)) {
                /*
                 * Player Riding the Dragon
                 */
                if (dragon.isTamed() && dragon.isSaddled() && (dragon.isAdult() || dragon.isJuvenile()) && !player.isSneaking() && !hasInteractItemsEquipped(player) && dragon.getPassengers().size() < 3) {
                    dragon.setRidingPlayer(player);
                }

                /*
                 * GUI
                 */
                if (player.isSneaking() && !hasInteractItemsEquipped(player)) {
                    // Dragon Inventory
                    if (!dragon.isClient() && (!player.isPassenger(player))) {
                        player.openGui(DragonMounts.instance, GuiHandler.GUI_DRAGON, dragon.world, dragon.getEntityId(), 0, 0);
                    }
                }
            }

            /*
             * Sit
             */
            if (dragon.isTamed() && (DMUtils.hasEquipped(player, Items.STICK) || DMUtils.hasEquipped(player, Items.BONE)) && dragon.onGround) {
                dragon.getAISit().setSitting(!dragon.isSitting());
                dragon.getNavigator().clearPath();
            }
        }

        /*
         * Consume
         */
        if (!stack.isEmpty() && stack.getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood) stack.getItem();
            if (food.isWolfsFavoriteMeat() ||
                    DMUtils.hasEquipped(player, Items.PORKCHOP) ||
                    DMUtils.hasEquipped(player, Items.COOKED_PORKCHOP) ||
                    DMUtils.hasEquipped(player, Items.BEEF) ||
                    DMUtils.hasEquipped(player, Items.CHICKEN) ||
                    DMUtils.hasEquipped(player, Items.COOKED_CHICKEN) ||
                    DMUtils.hasEquipped(player, Items.COOKED_BEEF) ||
                    DMUtils.hasEquipped(player, Items.RABBIT) ||
                    DMUtils.hasEquipped(player, Items.COOKED_RABBIT) ||
                    DMUtils.hasEquipped(player, Items.MUTTON) ||
                    DMUtils.hasEquipped(player, Items.COOKED_MUTTON) ||
                    DMUtils.hasEquipped(player, Items.FISH) ||
                    DMUtils.hasEquipped(player, Items.COOKED_FISH) ||
                    DMUtils.hasEquipped(player, Items.ROTTEN_FLESH) ||
                    DMUtils.hasEquippedOreDicFish(player)) {

                // Taming
                if (!dragon.isTamed()) {
                    consumeItemFromStack(player, stack);
                    dragon.eatEvent(stack.getItem());
                    dragon.tamedFor(player, dragon.getRNG().nextInt(4) == 0);
                    return true;
                }

                //  hunger
                if (dragon.getHunger() < 100) {
                    consumeItemFromStack(player, stack);
                    dragon.eatEvent(stack.getItem());
                    dragon.setHunger(dragon.getHunger() + (DMUtils.getFoodPoints(player)));
                    return true;
                }
            }

            // Mate
            if ((DMUtils.hasEquipped(player, dragon.getBreed().getBreedingItem()) || DMUtils.hasEquippedOreDicFish(player)) && dragon.isAdult() && !dragon.isInLove()) {
                dragon.setInLove(player);
                dragon.eatEvent(stack.getItem());
                consumeItemFromStack(player, stack);
                return true;
            }

            // Stop Growth
            if (DMUtils.hasEquipped(player, dragon.getBreed().getShrinkingFood()) && !dragon.isGrowthPaused() && dragon.isAllowed(player)) {
                dragon.setGrowthPaused(true);
                dragon.eatEvent(stack.getItem());
                dragon.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1f, 0.8F);
                player.sendStatusMessage(new TextComponentTranslation("dragon.growth.paused"), true);
                consumeItemFromStack(player, stack);
                return true;
            }

            // Continue growth
            if (DMUtils.hasEquipped(player, dragon.getBreed().getGrowingFood()) && dragon.isGrowthPaused() && dragon.isAllowed(player)) {
                dragon.setGrowthPaused(false);
                dragon.eatEvent(stack.getItem());
                dragon.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1, 0.8F);
                consumeItemFromStack(player, stack);
                return true;
            }
        }
        return false;
    }

    private static void consumeItemFromStack(EntityPlayer player, ItemStack stack) {
        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }
    }


    private static boolean hasInteractItemsEquipped(EntityPlayer player) {
        return DMUtils.hasEquippedUsable(player)
                || DMUtils.hasEquipped(player, ModTools.diamond_shears)
                || DMUtils.hasEquipped(player, ModItems.dragon_wand)
                || DMUtils.hasEquipped(player, ModItems.dragon_whistle)
                || DMUtils.hasEquipped(player, ModItems.Amulet)
                || DMUtils.hasEquipped(player, Items.BONE)
                || DMUtils.hasEquipped(player, Items.STICK)
                || DMUtils.hasEquippedFood(player);
    }
}
