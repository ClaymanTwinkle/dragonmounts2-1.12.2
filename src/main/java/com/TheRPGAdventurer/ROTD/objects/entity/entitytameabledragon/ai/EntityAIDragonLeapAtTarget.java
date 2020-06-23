package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.ai;

import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import net.minecraft.entity.ai.EntityAILeapAtTarget;

public class EntityAIDragonLeapAtTarget extends EntityAILeapAtTarget {
    private EntityTameableDragon dragon;

    public EntityAIDragonLeapAtTarget(EntityTameableDragon leapingEntity, float leapMotionYIn) {
        super(leapingEntity, leapMotionYIn);
        dragon = leapingEntity;
    }

    @Override
    public boolean shouldExecute() {
        return dragon.isBaby() && dragon.onGround && super.shouldExecute();
    }
}
