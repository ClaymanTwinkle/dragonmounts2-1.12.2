package com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.helper;

import com.TheRPGAdventurer.ROTD.DragonMounts;
import com.TheRPGAdventurer.ROTD.inits.ModArmour;
import com.TheRPGAdventurer.ROTD.inventory.ContainerDragon;
import com.TheRPGAdventurer.ROTD.network.MessageDragonInventory;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

/**
 * 龙的设置面板
 */
public class DragonInventoryHelper extends DragonHelper {
    private static final Logger L = LogManager.getLogger();

    // 是否备鞍
    private static final DataParameter<Boolean> DATA_SADDLED = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.BOOLEAN);
    // 是否备箱子
    private static final DataParameter<Boolean> CHESTED = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.BOOLEAN);

    // 盔甲
    private static final DataParameter<Integer> ARMOR = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.VARINT);
    // 4横幅
    private static final DataParameter<ItemStack> BANNER1 = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<ItemStack> BANNER2 = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<ItemStack> BANNER3 = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<ItemStack> BANNER4 = EntityDataManager.createKey(EntityTameableDragon.class, DataSerializers.ITEM_STACK);

    private DragonInventory dragonInv;
    private boolean hasChestVarChanged = false;

    public DragonInventoryHelper(EntityTameableDragon dragon) {
        super(dragon);
        dataWatcher.register(DATA_SADDLED, false);
        dataWatcher.register(CHESTED, false);
        dataWatcher.register(ARMOR, 0);

        dataWatcher.register(BANNER1, ItemStack.EMPTY);
        dataWatcher.register(BANNER2, ItemStack.EMPTY);
        dataWatcher.register(BANNER3, ItemStack.EMPTY);
        dataWatcher.register(BANNER4, ItemStack.EMPTY);

        InitializeDragonInventory();
    }


    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("Saddle", isSaddled());
        nbt.setBoolean("Chested", this.isChested());
        nbt.setInteger("Armor", this.getArmor());

        writeDragonInventory(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.setSaddled(nbt.getBoolean("Saddle"));
        this.setChested(nbt.getBoolean("Chested"));
        this.setArmor(nbt.getInteger("Armor"));

        readDragonInventory(nbt);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (hasChestVarChanged && dragonInv != null && !this.isChested()) {
            for (int i = ContainerDragon.chestStartIndex; i < 30; i++) {
                if (!dragonInv.getStackInSlot(i).isEmpty()) {
                    if (!dragon.world.isRemote) {
                        dragon.entityDropItem(dragonInv.getStackInSlot(i), 1);
                    }
                    dragonInv.removeStackFromSlot(i);
                }
            }
            hasChestVarChanged = false;
        }
    }

    @Override
    public void onDeath() {
        super.onDeath();
        if (dragonInv != null && !dragon.world.isRemote && !dragon.isEgg() && !dragon.isTamed()) {
            for (int i = 0; i < dragonInv.getSizeInventory(); ++i) {
                ItemStack itemstack = dragonInv.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    dragon.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    /**
     * Credits: AlexThe 666 Ice and Fire
     */
    private void InitializeDragonInventory() {
        int numberOfInventoryforChest = 27;
        int numberOfPlayerArmor = 5;
        DragonInventory dragonInv = this.dragonInv;
        this.dragonInv = new DragonInventory("dragonInv", 6 + numberOfInventoryforChest + 6 + numberOfPlayerArmor, this);
        this.dragonInv.setCustomName(dragon.getName());
        if (dragonInv != null) {
            int i = Math.min(dragonInv.getSizeInventory(), this.dragonInv.getSizeInventory());
            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = dragonInv.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    this.dragonInv.setInventorySlotContents(j, itemstack.copy());
                }
            }

            if (dragon.world.isRemote) {
                ItemStack saddle = dragonInv.getStackInSlot(0);
                ItemStack chest_left = dragonInv.getStackInSlot(1);
                ItemStack banner1 = this.dragonInv.getStackInSlot(31);
                ItemStack banner2 = this.dragonInv.getStackInSlot(32);
                ItemStack banner3 = this.dragonInv.getStackInSlot(33);
                ItemStack banner4 = this.dragonInv.getStackInSlot(34);

                DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));

                DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 1, chest_left != null && chest_left.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !chest_left.isEmpty() ? 1 : 0));

                DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 2, dragon.getIntFromArmor(dragonInv.getStackInSlot(2))));

                DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 31, banner1 != null && banner1.getItem() == Items.BANNER && !banner1.isEmpty() ? 1 : 0));

                DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 32, banner2 != null && banner2.getItem() == Items.BANNER && !banner2.isEmpty() ? 1 : 0));

                DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 33, banner3 != null && banner3.getItem() == Items.BANNER && !banner3.isEmpty() ? 1 : 0));

                DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 34, banner4 != null && banner4.getItem() == Items.BANNER && !banner4.isEmpty() ? 1 : 0));

            }
        }
    }

    /**
     * Credits: AlexThe 666 Ice and Fire
     */
    private void readDragonInventory(NBTTagCompound nbt) {
        if (dragonInv != null) {
            NBTTagList nbttaglist = nbt.getTagList("Items", 10);
            InitializeDragonInventory();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                this.dragonInv.setInventorySlotContents(j, new ItemStack(nbttagcompound));
            }
        } else {
            NBTTagList nbttaglist = nbt.getTagList("Items", 10);
            InitializeDragonInventory();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                this.InitializeDragonInventory();
                this.dragonInv.setInventorySlotContents(j, new ItemStack(nbttagcompound));

                ItemStack saddle = dragonInv.getStackInSlot(0);
                ItemStack chest = dragonInv.getStackInSlot(1);
                ItemStack banner1 = dragonInv.getStackInSlot(31);
                ItemStack banner2 = dragonInv.getStackInSlot(32);
                ItemStack banner3 = dragonInv.getStackInSlot(33);
                ItemStack banner4 = dragonInv.getStackInSlot(34);

                if (dragon.world.isRemote) {
                    DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));

                    DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 1, chest != null && chest.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !chest.isEmpty() ? 1 : 0));

                    DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 2, dragon.getIntFromArmor(dragonInv.getStackInSlot(2))));

                    DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 31, banner1 != null && banner1.getItem() == Items.BANNER && !banner1.isEmpty() ? 1 : 0));

                    DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 32, banner2 != null && banner2.getItem() == Items.BANNER && !banner2.isEmpty() ? 1 : 0));

                    DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 33, banner3 != null && banner3.getItem() == Items.BANNER && !banner3.isEmpty() ? 1 : 0));

                    DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 34, banner4 != null && banner4.getItem() == Items.BANNER && !banner4.isEmpty() ? 1 : 0));
                }
            }
        }
    }

    /**
     * Credits: AlexThe 666 Ice and Fire
     */
    private void writeDragonInventory(NBTTagCompound nbt) {
        if (dragonInv != null) {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 0; i < this.dragonInv.getSizeInventory(); ++i) {
                ItemStack itemstack = this.dragonInv.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("Slot", (byte) i);
                    itemstack.writeToNBT(nbttagcompound);
                    nbttaglist.appendTag(nbttagcompound);
                }
            }
            nbt.setTag("Items", nbttaglist);
        }
        if (dragon.getCustomNameTag() != null && !dragon.getCustomNameTag().isEmpty()) {
            nbt.setString("CustomName", dragon.getCustomNameTag());
        }
    }

    public void refreshInventory() {
        ItemStack saddle = dragonInv.getStackInSlot(0);
        ItemStack leftChestforInv = dragonInv.getStackInSlot(1);
        ItemStack banner1 = dragonInv.getStackInSlot(31);
        ItemStack banner2 = dragonInv.getStackInSlot(32);
        ItemStack banner3 = dragonInv.getStackInSlot(33);
        ItemStack banner4 = dragonInv.getStackInSlot(34);
        ItemStack armorStack = dragonInv.getStackInSlot(2);
        int armor = dragon.getIntFromArmor(dragonInv.getStackInSlot(2));
        int armor1 = dragon.getIntFromArmor(dragonInv.getStackInSlot(2));

        if (saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() && !isSaddled() && dragon.isOldEnoughToBreathe()) {
            this.setSaddled(true);
            dragon.world.playSound(dragon.posX, dragon.posY, dragon.posZ, SoundEvents.ENTITY_HORSE_SADDLE, SoundCategory.PLAYERS, 1F, 1.0F, false);
        }
        if (leftChestforInv != null && leftChestforInv.getItem() == Item.getItemFromBlock(Blocks.CHEST) &&
                !leftChestforInv.isEmpty() && !isChested() && dragon.isOldEnoughToBreathe()) {
            this.setChested(true);
            dragon.world.playSound(dragon.posX, dragon.posY, dragon.posZ, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.PLAYERS, 1F, 1F, false);
        }

        // figure out to break the boolean loop and make it play armor equip sounds
        if (dragon.ticksExisted > 20 && armor != armor1 && armor1 != 0 && armorStack.getItem() == ModArmour.dragonarmor_diamond && dragon.isOldEnoughToBreathe()) {
            this.setArmor(armor);
        }

        this.setBanner1(banner1);
        this.setBanner2(banner2);
        this.setBanner3(banner3);
        this.setBanner4(banner4);

        if (dragon.isServer()) {
            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 1, leftChestforInv != null && leftChestforInv.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !leftChestforInv.isEmpty() ? 1 : 0));
            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 2, dragon.getIntFromArmor(this.dragonInv.getStackInSlot(2))));
            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 31, banner1 != null && banner1.getItem() == Items.BANNER && !banner1.isEmpty() ? 1 : 0));
            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 32, banner2 != null && banner2.getItem() == Items.BANNER && !banner2.isEmpty() ? 1 : 0));
            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 31, banner3 != null && banner3.getItem() == Items.BANNER && !banner3.isEmpty() ? 1 : 0));
            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonInventory(dragon.getEntityId(), 32, banner4 != null && banner4.getItem() == Items.BANNER && !banner4.isEmpty() ? 1 : 0));
        }
    }

    public DragonInventory getDragonInv() {
        return dragonInv;
    }

    /**
     * Returns true if the dragon is saddled.
     */
    public boolean isSaddled() {
        return dataWatcher.get(DATA_SADDLED);
    }

    /**
     * Set or remove the saddle of the
     */
    public void setSaddled(boolean saddled) {
        L.trace("setSaddled({})", saddled);
        dataWatcher.set(DATA_SADDLED, saddled);
    }

    // used to be called isChestedLeft
    public boolean isChested() {
        return dataWatcher.get(CHESTED);
    }

    public void setChested(boolean chested) {
        dataWatcher.set(CHESTED, chested);
        hasChestVarChanged = true;
    }

    /**
     * 1 equals iron 2 equals gold 3 equals diamond 4 equals emerald
     *
     * @return 0 no armor
     */
    public int getArmor() {
        return this.dataWatcher.get(ARMOR);
    }

    public void setArmor(int armorType) {
        this.dataWatcher.set(ARMOR, armorType);
    }

    public ItemStack getBanner1() {
        return dataWatcher.get(BANNER1);
    }

    public void setBanner1(ItemStack bannered) {
        dataWatcher.set(BANNER1, bannered);
    }

    public ItemStack getBanner2() {
        return dataWatcher.get(BANNER2);
    }

    public void setBanner2(ItemStack male) {
        dataWatcher.set(BANNER2, male);
    }

    public ItemStack getBanner3() {
        return dataWatcher.get(BANNER3);
    }

    public void setBanner3(ItemStack male) {
        dataWatcher.set(BANNER3, male);
    }

    public ItemStack getBanner4() {
        return dataWatcher.get(BANNER4);
    }

    public void setBanner4(ItemStack male) {
        dataWatcher.set(BANNER4, male);
    }


    /**
     * Credits: AlexThe 666 Ice and Fire
     */
    public static class DragonInventory extends ContainerHorseChest {

        public DragonInventory(String inventoryTitle, int slotCount, DragonInventoryHelper helper) {
            super(inventoryTitle, slotCount);
            this.addInventoryChangeListener(new DragonInventoryListener(helper));
        }
    }

    /**
     * Credits: AlexThe 666 Ice and Fire
     */
    public static class DragonInventoryListener implements IInventoryChangedListener {
        private final DragonInventoryHelper helper;

        DragonInventoryListener(DragonInventoryHelper helper) {
            this.helper = helper;
        }

        @Override
        public void onInventoryChanged(@Nonnull IInventory invBasic) {
            helper.refreshInventory();
        }
    }
}
