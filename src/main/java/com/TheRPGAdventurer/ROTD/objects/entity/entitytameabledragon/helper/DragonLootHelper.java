package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.helper;

import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

/**
 * 与龙的战利品相关
 */
public class DragonLootHelper extends DragonHelper{
    // 剪刀
    private static final DataParameter<Byte> DRAGON_SCALES = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.BYTE);

    public DragonLootHelper(EntityTameableDragon dragon) {
        super(dragon);
        dataWatcher.register(DRAGON_SCALES, (byte) 0);
    }

    public boolean isSheared() {
        return (this.dataWatcher.get(DRAGON_SCALES) & 16) != 0;
    }

    /**
     * make a dragon sheared if set to true
     */
    public void setSheared(boolean sheared) {
        byte b0 = this.dataWatcher.get(DRAGON_SCALES);

        if (sheared) {
            dataWatcher.set(DRAGON_SCALES, (byte) (b0 | 16));
        } else {
            dataWatcher.set(DRAGON_SCALES, (byte) (b0 & -17));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("Sheared", this.isSheared());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.setSheared(nbt.getBoolean("Sheared"));
    }
}
