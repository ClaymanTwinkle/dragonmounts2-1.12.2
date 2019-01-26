package com.TheRPGAdventurer.ROTD.client.initialization;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeys {
	
    public static final String KEY_CATEGORY = "key.categories.gameplay";
    public static KeyBinding KEY_FLY_UP;
    public static KeyBinding KEY_FLY_DOWN;
    public static  KeyBinding KEY_BREATH;
    public static KeyBinding KEY_HOVERCANCEL;
    
    public static void init() {
    	KEY_BREATH = new KeyBinding("key.dragon.breath", Keyboard.KEY_R, KEY_CATEGORY);
    	KEY_HOVERCANCEL = new KeyBinding("key.dragon.cancelhover", Keyboard.KEY_LCONTROL, KEY_CATEGORY);
    	KEY_FLY_UP = new KeyBinding("key.dragon.flyUp", Keyboard.KEY_SPACE, KEY_CATEGORY);
    	KEY_FLY_DOWN = new KeyBinding("key.dragon.flyDown", Keyboard.KEY_LCONTROL, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(KEY_BREATH);
        ClientRegistry.registerKeyBinding(KEY_FLY_UP);
        ClientRegistry.registerKeyBinding(KEY_FLY_DOWN);
        ClientRegistry.registerKeyBinding(KEY_HOVERCANCEL);
        
    	  
    }
    
/*    @SubscribeEvent
    public void onTick(ClientTickEvent evt) {
        BitSet flags = dcm.getFlags();
        flags.set(0, KEY_FLY_UP.isKeyDown());
        flags.set(1, KEY_FLY_DOWN.isKeyDown());
        flags.set(2, KEY_HOVERCANCEL.isPressed());
        
        // send message to server if it has changed
        if (dcm.hasChanged()) {
            DragonMounts.NETWORK_WRAPPER.sendToServer(dcm);
        }
    }
    */
}
