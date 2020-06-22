package com.TheRPGAdventurer.ROTD.network;

import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageDragonBreath implements IMessage {

	public int dragonId;
	public boolean isBreathing;

	public MessageDragonBreath(int dragonId, boolean isBreathing) {
		this.dragonId = dragonId;
		this.isBreathing = isBreathing;
	}

	public MessageDragonBreath() {

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dragonId = buf.readInt();
		isBreathing = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dragonId);
		buf.writeBoolean(isBreathing);
	}

	public static class MessageDragonBreathHandler implements IMessageHandler<MessageDragonBreath, IMessage> {
		@Override
		public IMessage onMessage(MessageDragonBreath message, MessageContext ctx) {
			Entity entity = ctx.getServerHandler().player.world.getEntityByID(message.dragonId);
			if (entity instanceof EntityTameableDragon) {
				EntityTameableDragon dragon = (EntityTameableDragon) entity;
				dragon.setUsingBreathWeapon(message.isBreathing);
			}
			return null;
		}
	}
}

//	@Override
