package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.helper;

import com.TheRPGAdventurer.ROTD.DragonMountsConfig;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

public class DragonHungerHelper extends DragonHelper {

    private static final DataParameter<Integer> HUNGER = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.VARINT);

    public DragonHungerHelper(EntityTameableDragon dragon) {
        super(dragon);

        this.dataWatcher.register(HUNGER, 100);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("hunger", this.getHunger());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.setHunger(nbt.getInteger("hunger"));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.dragon.ticksExisted % (DragonMountsConfig.hungerDecrement) == 1) {
            if (this.getHunger() > 0) {
                this.setHunger(this.getHunger() - 1);
            }
        }
    }

    public int getHunger() {
        return dataWatcher.get(HUNGER);
    }

    public void setHunger(int hunger) {
        this.dataWatcher.set(HUNGER, Math.min(100, hunger));
    }
}
