package com.TheRPGAdventurer.ROTD.client.sound;

import com.TheRPGAdventurer.ROTD.DragonMounts;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(DragonMounts.MODID)
public class ModSounds {
	
	@ObjectHolder("mob.dragon.step")
	public static final SoundEvent ENTITY_DRAGON_STEP = createSoundEvent("mob.dragon.step");

	@ObjectHolder("mob.dragon.breathe")
	public static final SoundEvent ENTITY_DRAGON_BREATHE = createSoundEvent("mob.dragon.breathe");

	@ObjectHolder("mob.dragon.death")
	public static final SoundEvent ENTITY_DRAGON_DEATH = createSoundEvent("mob.dragon.death");
	
	@ObjectHolder("mob.dragon.growl")
	public static final SoundEvent ENTITY_DRAGON_GROWL = createSoundEvent("mob.dragon.growl");
	
	@ObjectHolder("mob.dragon.nethergrowl")
	public static final SoundEvent ENTITY_NETHER_DRAGON_GROWL = createSoundEvent("mob.dragon.nethergrowl");
	
	@ObjectHolder("mob.dragon.skeletongrowl")
	public static final SoundEvent ENTITY_SKELETON_DRAGON_GROWL = createSoundEvent("mob.dragon.skeletongrowl");
	
	@ObjectHolder("mob.dragon.zombiedeath")
	public static final SoundEvent ZOMBIE_DRAGON_DEATH = createSoundEvent("mob.dragon.zombiedeath");
	
	@ObjectHolder("mob.dragon.zombiegrowl")
	public static final SoundEvent ZOMBIE_DRAGON_GROWL = createSoundEvent("mob.dragon.zombiegrowl");
	
	@ObjectHolder("mob.dragon.sneeze")
	public static final SoundEvent DRAGON_SNEEZE = createSoundEvent("mob.dragon.sneeze");
	
	@ObjectHolder("mob.dragon.hatched")
	public static final SoundEvent DRAGON_HATCHED = createSoundEvent("mob.dragon.hatched");
	
	@ObjectHolder("mob.dragon.hatching")
	public static final SoundEvent DRAGON_HATCHING = createSoundEvent("mob.dragon.hatching");
	
	@ObjectHolder("mob.dragon.whistle")
	public static final SoundEvent DRAGON_WHISTLE = createSoundEvent("mob.dragon.whistle");
	
	@ObjectHolder("mob.dragon.whistle1")
	public static final SoundEvent DRAGON_WHISTLE1 = createSoundEvent("mob.dragon.whistle1");
	
	private static SoundEvent createSoundEvent(final String soundName) {
		final ResourceLocation soundID = new ResourceLocation(DragonMounts.MODID, soundName);
		return new SoundEvent(soundID).setRegistryName(soundID);
	}

	@Mod.EventBusSubscriber(modid = DragonMounts.MODID)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
			event.getRegistry().registerAll(
					ENTITY_DRAGON_STEP,
					ENTITY_DRAGON_BREATHE,
					ENTITY_DRAGON_DEATH,
					ENTITY_DRAGON_GROWL,
					ENTITY_NETHER_DRAGON_GROWL,
					ENTITY_SKELETON_DRAGON_GROWL,
					ZOMBIE_DRAGON_GROWL,
					ZOMBIE_DRAGON_DEATH,
					DRAGON_SNEEZE,
					DRAGON_HATCHED,
					DRAGON_HATCHING,
					DRAGON_WHISTLE,
					DRAGON_WHISTLE1
			);
		}
	}
	
	  public final String getJsonName() {return DragonMounts.MODID + ":" + jsonName;}

	  private ModSounds(String i_jsonName) {
	    jsonName = i_jsonName;
	  }
	  
	  private final String jsonName;
}
