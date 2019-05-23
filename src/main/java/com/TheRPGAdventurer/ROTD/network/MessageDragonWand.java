package com.TheRPGAdventurer.ROTD.network;

import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDragonWand extends AbstractMessage<MessageDragonWand> {

	public int dragonId;
	public int slot_index;
	public int armor_type;
	public boolean gender;

	public MessageDragonWand(int dragonId, int slot_index, int armor_type) {
		this.dragonId = dragonId;
		this.slot_index = slot_index;
		this.armor_type = armor_type;
	}

	public MessageDragonWand() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dragonId = buf.readInt();
		slot_index = buf.readInt();
		armor_type = buf.readInt();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dragonId);
		buf.writeInt(slot_index);
		buf.writeInt(armor_type);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientReceived(Minecraft client, MessageDragonWand message, EntityPlayer player, MessageContext messageContext) {
		
	}

	@Override
	public void onServerReceived(MinecraftServer server, MessageDragonWand message, EntityPlayer player, MessageContext messageContext) {
		Entity entity = player.world.getEntityByID(message.dragonId);
		if (entity instanceof EntityTameableDragon) {
			EntityTameableDragon dragon = (EntityTameableDragon) entity;
		}
	}
}