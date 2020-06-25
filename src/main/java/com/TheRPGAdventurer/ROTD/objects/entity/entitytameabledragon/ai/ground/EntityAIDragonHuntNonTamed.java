/*
 ** 2013 July 20
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.ai.ground;

import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.util.EntityClassPredicate;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.passive.*;

/**
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class EntityAIDragonHuntNonTamed extends EntityAITargetNonTamed {

    private final EntityTameableDragon dragon;

    public EntityAIDragonHuntNonTamed(EntityTameableDragon dragon) {
        super(dragon, EntityAnimal.class, false, new EntityClassPredicate(EntitySheep.class, EntityPig.class, EntityChicken.class, EntityRabbit.class, EntityLlama.class));
        this.dragon = dragon;
    }

    @Override
    public boolean shouldExecute() {
        return dragon.getControllingPlayer() == null && dragon.isAdult() && dragon.getHunger() < 50 && super.shouldExecute();
    }

}
