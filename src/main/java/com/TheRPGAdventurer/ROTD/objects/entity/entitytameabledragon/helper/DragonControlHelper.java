package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.helper;

import com.TheRPGAdventurer.ROTD.DragonMounts;
import com.TheRPGAdventurer.ROTD.inits.ModKeys;
import com.TheRPGAdventurer.ROTD.network.MessageDragonBreath;
import com.TheRPGAdventurer.ROTD.network.MessageDragonExtras;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 控制
 */
public class DragonControlHelper extends DragonHelper {
    private static final Logger L = LogManager.getLogger();

    private static final DataParameter<Boolean> FOLLOW_YAW = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HOVER_CANCELLED = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> Y_LOCKED = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BOOSTING = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GOING_DOWN = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_FLYING = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.BOOLEAN);

    private boolean followYaw;
    private boolean isUnhovered;
    private boolean yLocked;
    private boolean isGoingDown;

    public DragonControlHelper(EntityTameableDragon dragon) {
        super(dragon);
        dataWatcher.register(FOLLOW_YAW, true);
        dataWatcher.register(HOVER_CANCELLED, false);
        dataWatcher.register(Y_LOCKED, false);
        dataWatcher.register(BOOSTING, false);
        dataWatcher.register(GOING_DOWN, false);
        dataWatcher.register(DATA_FLYING, false);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("followyaw", this.followYaw());
        nbt.setBoolean("unhovered", this.isUnHovered());
        nbt.setBoolean("ylocked", this.isYLocked());
        nbt.setBoolean("boosting", this.boosting());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.setFollowYaw(nbt.getBoolean("followyaw"));
        this.setUnHovered(nbt.getBoolean("unhovered"));
        this.setYLocked(nbt.getBoolean("ylocked"));
        this.setBoosting(nbt.getBoolean("boosting"));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (dragon.isClient()) {
            this.updateKeys();
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (dragon.getRidingEntity() instanceof EntityLivingBase) {
            EntityLivingBase ridingEntity = (EntityLivingBase) dragon.getRidingEntity();
            if (ridingEntity.isElytraFlying()) {
                this.setUnHovered(true);
            }
        }

        if (dragon.getControllingPlayer() == null && !dragon.isFlying() && dragon.isSitting()) {
            dragon.removePassengers();
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateKeys() {
        Minecraft mc = Minecraft.getMinecraft();

        if ((dragon.hasControllingPlayer(mc.player)) ||
                (dragon.getRidingEntity() instanceof EntityPlayer && dragon.getRidingEntity() != null && dragon.getRidingEntity().getUniqueID().equals(mc.player.getUniqueID()))) {
            boolean isBreathing = ModKeys.KEY_BREATH.isKeyDown();
            boolean isBoosting = ModKeys.BOOST.isKeyDown();
            boolean isDown = ModKeys.DOWN.isKeyDown();
            boolean unhover = ModKeys.KEY_HOVERCANCEL.isPressed();
            boolean followyaw = ModKeys.FOLLOW_YAW.isPressed();
            boolean locky = ModKeys.KEY_LOCKEDY.isPressed();

            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonBreath(dragon.getEntityId(), isBreathing));
            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonExtras(dragon.getEntityId(), unhover, followyaw, locky, isBoosting, isDown));
        }
    }

    public boolean followYaw() {
        if (dragon.isClient()) {
            boolean folowYaw = dataWatcher.get(FOLLOW_YAW);
            this.followYaw = folowYaw;
            return folowYaw;
        }
        return followYaw;
    }

    public void setFollowYaw(boolean folowYaw) {
        dataWatcher.set(FOLLOW_YAW, folowYaw);
        if (dragon.isServer()) {
            this.followYaw = folowYaw;
        }
    }

    public boolean isUnHovered() {
        if (dragon.isClient()) {
            boolean isUnhovered = dataWatcher.get(HOVER_CANCELLED);
            this.isUnhovered = isUnhovered;
            return isUnhovered;
        }
        return isUnhovered;
    }

    public void setUnHovered(boolean isUnhovered) {
        dataWatcher.set(HOVER_CANCELLED, isUnhovered);
        if (dragon.isServer()) {
            this.isUnhovered = isUnhovered;
        }
    }

    public boolean isYLocked() {
        if (dragon.isClient()) {
            boolean yLocked = dataWatcher.get(Y_LOCKED);
            this.yLocked = yLocked;
            return yLocked;
        }
        return yLocked;
    }

    public void setYLocked(boolean yLocked) {
        dataWatcher.set(Y_LOCKED, yLocked);
        if (dragon.isServer()) {
            this.yLocked = yLocked;
        }
    }

    public boolean boosting() {
        return dataWatcher.get(BOOSTING);
    }

    public void setBoosting(boolean allow) {
        dataWatcher.set(BOOSTING, allow);
    }

    /**
     * Returns true if the entity is breathing.
     */
    public boolean isGoingDown() {
        if (dragon.isClient()) {
            this.isGoingDown = this.dataWatcher.get(GOING_DOWN);
            return isGoingDown;
        }
        return isGoingDown;
    }

    /**
     * Set the breathing flag of the entity.
     */
    public void setGoingDown(boolean goingdown) {
        this.dataWatcher.set(GOING_DOWN, goingdown);
        if (dragon.isServer()) {
            this.isGoingDown = goingdown;
        }
    }

    /**
     * Returns true if the entity is flying.
     */
    public boolean isFlying() {
        return dataWatcher.get(DATA_FLYING);
    }

    /**
     * f Set the flying flag of the entity.
     */
    public void setFlying(boolean flying) {
        L.trace("setFlying({})", flying);
        dataWatcher.set(DATA_FLYING, flying);
    }
}
