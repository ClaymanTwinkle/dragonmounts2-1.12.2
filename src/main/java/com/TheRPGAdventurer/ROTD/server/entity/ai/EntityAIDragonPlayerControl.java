/*
 ** 2012 March 18
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package com.TheRPGAdventurer.ROTD.server.entity.ai;

import java.util.BitSet;

import com.TheRPGAdventurer.ROTD.server.entity.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.server.entity.breeds.EnumDragonBreed;
import com.TheRPGAdventurer.ROTD.util.math.MathX;
import com.TheRPGAdventurer.ROTD.util.reflection.PrivateAccessor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;

/**
 * Abstract "AI" for player-controlled movements.
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class EntityAIDragonPlayerControl extends EntityAIDragonBase implements PrivateAccessor {

    protected EntityPlayer rider;
    private Vec3d inates;

    public EntityAIDragonPlayerControl(EntityTameableDragon dragon) {
        super(dragon);
        setMutexBits(0xffffffff);
    }
    
    @Override
    public boolean shouldExecute() {   
        rider = dragon.getControllingPlayer();
        return rider != null;
    }

    @Override
    public void startExecuting() {
        dragon.getNavigator().clearPathEntity();
    }
    
    protected boolean isFlyUp() {
        return getControlFlag(0);
    }
    
    protected boolean isFlyDown() {
        return getControlFlag(1);
    }
    
    private boolean getControlFlag(int index) {
        BitSet controlFlags = dragon.getControlFlags();
        return controlFlags == null ? false : controlFlags.get(index);
    }
    
    @Override
    public void updateTask() {
        Vec3d wp = rider.getLookVec();
        double x = dragon.posX;
        double y = dragon.posY;
        double z = dragon.posZ;
        
        double verticalSpeed = 0;    
        
        if(dragon.getBreedType() == EnumDragonBreed.SYLPHID) {
        	PotionEffect watereffect = new PotionEffect(MobEffects.WATER_BREATHING, 20*10);
        	if (!rider.isPotionActive(watereffect.getPotion()) && rider.isInWater()) { // If the Potion isn't currently active,
        		rider.addPotionEffect(watereffect); // Apply a copy of the PotionEffect to the player
    		}
        } 

        // if we're breathing at a target, look at it
        if (dragon.isUsingBreathWeapon() && dragon.getBreed().canUseBreathWeapon()) {
        	Vec3d dragonEyePos  = dragon.getPositionVector().addVector(0, dragon.getEyeHeight(), 0);
            Vec3d lookDirection = rider.getLook(1.0F);
            Vec3d endOfLook = dragonEyePos.addVector(lookDirection.x, MathX.clamp(lookDirection.y, -90, 90), lookDirection.z);
            dragon.getLookHelper().setLookPosition(endOfLook.x, endOfLook.y, endOfLook.z, 
            		dragon.getHeadYawSpeed(), dragon.getHeadPitchSpeed());
            updateIntendedRideRotation(rider);
        }
                
        // control direction with movement keys
        if (rider.moveStrafing != 0 || rider.moveForward != 0) {
            if (rider.moveForward < 0) {
                wp = wp.rotateYaw(MathX.PI_F);
            } else if (rider.moveStrafing > 0) {
                wp = wp.rotateYaw(MathX.PI_F * 0.5f);
            } else if (rider.moveStrafing < 0) {
                wp = wp.rotateYaw(MathX.PI_F * -0.5f);
            } 
            
            x += wp.x * 10;
            y += wp.y * 10;         
            z += wp.z * 10;         
     }
      
     // lift off from a jump
     if (!dragon.isFlying()) {
        if (entityIsJumping(rider)) {
             dragon.liftOff();
        }
     } 
        
        dragon.getMoveHelper().setMoveTo(x,y,z,1.2);
    }
    
	private void updateIntendedRideRotation(EntityPlayer rider) {
		boolean hasRider = dragon.hasControllingPlayer(rider);
		if(dragon.isUsingBreathWeapon() && hasRider && rider.moveStrafing == 0) {
			dragon.rotationYaw = rider.rotationYaw;
			dragon.rotationPitch = rider.rotationPitch;
		}
	}
    
}