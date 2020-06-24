/*
 ** 2016 March 12
 **
 ** The author disclaims copyright to this source code. In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.helper;

import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.ai.*;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.ai.ground.EntityAIDragonFollowOwner;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.ai.ground.EntityAIDragonHuntNonTamed;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.ai.ground.EntityAIDragonWatchIdle;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.ai.ground.EntityAIDragonWatchLiving;
import net.minecraft.entity.ai.*;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraftforge.oredict.OreDictionary;

import java.util.stream.Collectors;

/**
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class DragonBrain extends DragonHelper {
    // mutex 1: movement
    // mutex 2: looking
    // mutex 4: special state
    private final EntityAITasks tasks;

    // mutex 1: waypointing
    // mutex 2: continuous waypointing

    // mutex 1: generic targeting
    private final EntityAITasks targetTasks;

    public DragonBrain(EntityTameableDragon dragon) {
        super(dragon);
        tasks = dragon.tasks;
        targetTasks = dragon.targetTasks;
    }

    public void setAvoidsWater(boolean avoidWater) {
        PathNavigate pathNavigate = dragon.getNavigator();
        if (pathNavigate instanceof PathNavigateGround) {
            PathNavigateGround pathNavigateGround = (PathNavigateGround) pathNavigate;
            pathNavigateGround.setCanSwim(!avoidWater); // originally setAvoidsWater()
        }
    }

    public void updateAITasks() {
        // only hatchlings are small enough for doors
        // (eggs don't move on their own anyway and are ignored)
        // guessed, based on EntityAIRestrictOpenDoor - break the door down, don't open it
        if (dragon.getNavigator() instanceof PathNavigateGround) {
            PathNavigateGround pathNavigateGround = (PathNavigateGround) dragon.getNavigator();
            pathNavigateGround.setEnterDoors(dragon.isBaby());
        }

        // clear current navigation target
        dragon.getNavigator().clearPath();

        // clear existing task
        tasks.taskEntries.clear();
        targetTasks.taskEntries.clear();

        // eggs don't have any tasks
        if (dragon.isEgg()) {
            return;
        }

        tasks.addTask(0, new EntityAIDragonCatchOwner(dragon)); // mutex all
        tasks.addTask(1, new EntityAIDragonPlayerControl(dragon)); // mutex all
        tasks.addTask(2, dragon.getAISit()); // mutex 4+1
        tasks.addTask(2, new EntityAIDragonSwimming(dragon)); // mutex 4
        tasks.addTask(3, new EntityAIDragonMate(dragon, 0.6)); // mutex 2+1
        tasks.addTask(4, new EntityAIDragonLeapAtTarget(dragon, 0.7F)); // mutex 1
        tasks.addTask(4, new EntityAIDragonAttackMelee(dragon, 1, true)); // mutex 2+1
        //tasks.addTask(6, new EntityAIDragonFlight(dragon, 1)); // mutex 1
        tasks.addTask(5, new EntityAIDragonTempt(dragon, 0.75, false, OreDictionary.getOres("listAllfishraw").stream().map(ItemStack::getItem).collect(Collectors.toSet()))); // mutex 2+1
        tasks.addTask(6, new EntityAIDragonFollowOwner(dragon, 1.5)); // mutex 2+1
        tasks.addTask(7, new EntityAIDragonFollowParent(dragon, 1.4f)); // mutex 1
        tasks.addTask(8, new EntityAIMoveTowardsRestriction(dragon, 1)); // mutex 1
        tasks.addTask(9, new EntityAIWander(dragon, 1)); // mutex 1
        tasks.addTask(10, new EntityAIDragonWatchLiving(dragon, 16, 0.05f)); // mutex 2
        tasks.addTask(11, new EntityAIDragonWatchIdle(dragon)); // mutex 2

        targetTasks.addTask(0, new EntityAIOwnerHurtByTarget(dragon)); // mutex 1
        targetTasks.addTask(1, new EntityAIOwnerHurtTarget(dragon)); // mutex 1
        targetTasks.addTask(2, new EntityAIDragonHurtByTarget(dragon, false)); // mutex 1
        targetTasks.addTask(3, new EntityAIDragonHuntNonTamed(dragon)); // mutex 1
        targetTasks.addTask(4, new EntityAIDragonNearestAttackableTarget(dragon));
    }
}

