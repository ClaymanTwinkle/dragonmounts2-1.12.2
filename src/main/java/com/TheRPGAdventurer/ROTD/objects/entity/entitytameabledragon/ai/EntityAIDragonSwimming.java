package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.ai;

import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;

public class EntityAIDragonSwimming extends EntityAIDragonBase {

    public EntityAIDragonSwimming(EntityTameableDragon dragon) {
    	super(dragon);
        this.setMutexBits(4);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return (this.dragon.isInWater() || this.dragon.isInLava()) && !dragon.onGround;
    }

    /**z
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask() {
        if (!dragon.isFlying()) {
            dragon.liftOff();
        }
    }
}