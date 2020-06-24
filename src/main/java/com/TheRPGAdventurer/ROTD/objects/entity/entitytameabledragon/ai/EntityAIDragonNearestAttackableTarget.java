package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.ai;

import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.IMob;

public class EntityAIDragonNearestAttackableTarget extends EntityAINearestAttackableTarget<EntityLiving> {

    private final EntityTameableDragon dragon;

    public EntityAIDragonNearestAttackableTarget(EntityTameableDragon creature) {
        super(creature, EntityLiving.class, 10, false, true, input -> input != null && IMob.VISIBLE_MOB_SELECTOR.apply(input));
        this.dragon = creature;
    }

    @Override
    public boolean shouldExecute() {
        return dragon.isAdult() && super.shouldExecute();
    }
}
