/*
 ** 2013 November 03
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.ai;

import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

/**
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class EntityAIDragonCatchOwner extends EntityAIDragonBase {
    public EntityAIDragonCatchOwner(EntityTameableDragon dragon) {
        super(dragon);
    }

    @Override
    public boolean shouldExecute() {
        EntityPlayer owner = (EntityPlayer) dragon.getOwner();
        if (owner == null) {
            return false;
        }

        // don't catch if leashed
        if (dragon.getLeashed()) {
            return false;
        }
//         no point in catching players in creative mode
        if (owner.capabilities.isCreativeMode) {
            return false;
        }

        // don't catch if already being ridden
        if (dragon.getControllingPlayer() != null) {
            return false;
        }

        // don't follow if sitting
        if (dragon.isSitting()) {
            return false;
        }


        if (!dragon.isSaddled()) {
            return false;
        }

        if(owner.isRiding()) {
           return false;
        }

        // don't catch if owner has a working Elytra equipped
        // note: isBroken() is misleading, it actually checks if the items is usable
        ItemStack itemStack = owner.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (itemStack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemStack)) {
            return false;
        }

        return owner.fallDistance > 4;
    }

    @Override
    public void updateTask() {
        EntityPlayer owner = (EntityPlayer) dragon.getOwner();

        if(owner == null) return;

        // catch owner in flight if possible
        if (!dragon.isFlying()) {
            dragon.liftOff();
        }

        // don't catch if owner is too far away
        double followRange = getFollowRange();
        dragon.setBoosting(dragon.getDistance(owner) > dragon.width);
        if (dragon.getDistance(owner) < followRange) {
            // mount owner if close enough, otherwise move to owner
            if ((dragon.getDistance(owner) <= dragon.width || dragon.getDistance(owner) <= dragon.height) && !owner.isSneaking() && !owner.isRiding() && dragon.isFlying()) {
                owner.startRiding(dragon);
            } else {
                dragon.getNavigator().tryMoveToEntityLiving(owner, 10);
            }
        }
    }
}
