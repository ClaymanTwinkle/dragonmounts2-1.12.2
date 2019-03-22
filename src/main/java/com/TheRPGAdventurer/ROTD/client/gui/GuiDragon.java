package com.TheRPGAdventurer.ROTD.client.gui;

import com.TheRPGAdventurer.ROTD.DragonMounts;
import com.TheRPGAdventurer.ROTD.client.inventory.ContainerDragon;
import com.TheRPGAdventurer.ROTD.server.entity.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.server.network.MessageDragonGui;
import com.TheRPGAdventurer.ROTD.server.network.MessageDragonLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiDragon extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(DragonMounts.MODID, "textures/gui/dragon.png");
    private IInventory playerInventory;
    private IInventory dragonInv;
    private EntityTameableDragon dragon;
    private float mousePosX;
    private float mousePosY;
    private LockButton lock;
    private GuiButton sit;
    public static ResourceLocation lockOpen = new ResourceLocation(DragonMounts.MODID, "textures/gui/lock_2.png");
    public static ResourceLocation lockLocked = new ResourceLocation(DragonMounts.MODID, "textures/gui/lock_1.png");
    public static ResourceLocation lockDisabled = new ResourceLocation(DragonMounts.MODID, "textures/gui/lock_3.png");
    private EntityPlayer player;

    public GuiDragon(IInventory playerInv, EntityTameableDragon dragon) {
        super(new ContainerDragon(dragon, Minecraft.getMinecraft().player));
        this.playerInventory = playerInv;
        this.dragonInv = dragon.dragonInv;
        this.player = Minecraft.getMinecraft().player;
        this.dragon = dragon;
        this.allowUserInput = false;
        this.ySize = 214;
        this.xSize = 176;

    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the
     * items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        float scale = dragon.getScale();
        this.fontRenderer.drawString(dragon.hasCustomName() ? dragon.getCustomNameTag() : "Dragon Inventory", 8, 6, dragon.getBreed().getColor());
        this.fontRenderer.drawString(dragon.isMale() ? "M" : "FM", 160, 6, dragon.isMale() ? 0x0079be : 0Xff8b8b);


    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        if (dragon.isChested()) {
            this.drawTexturedModalRect(x + 0, y + 73, 0, 130, 170, 55);
        }
        GuiInventory.drawEntityOnScreen(x + 80, y + 65, (int) (13 / dragon.getScale()), x + 51 - this.mousePosX, y + 75 - 50 - this.mousePosY,
                this.dragon);

    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        lock = new LockButton(0, width / 2 + 69, height / 2 - 54, 16, 16, I18n.format("gui.allowothers", new Object[0]), dragon);
        sit = new GuiButton(1, width / 2 + 47, height / 2 - 53, 18, 14, "SIT");

        buttonList.add(lock);
        buttonList.add(sit);
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        boolean sit = (button == this.sit);
        boolean lock = (button == this.lock);

        if (sit) {
            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonGui(dragon.getEntityId()));
        } else if (lock) {
            DragonMounts.NETWORK_WRAPPER.sendToServer(new MessageDragonLock(dragon.getEntityId()));
        }

    }

    public void updateScreen() {
        lock.enable = (player == dragon.getOwner());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mousePosX = mouseX;
        this.mousePosY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    static class LockButton extends GuiButton {

        private boolean enable;
        private EntityTameableDragon dragon;

        public LockButton(int buttonId, int x, int y, int i, int j, String buttonText, EntityTameableDragon dragon) {
            super(buttonId, x, y, i, j, buttonText);
            this.dragon = dragon;
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            return enable && super.mousePressed(mc, mouseX, mouseY);
        }

        /**
         * Draws this button to the screen.
         */
        @Override
        public void drawButton(Minecraft mc, int parX, int parY, float partialTicks) {
            if (visible) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 16, 16, 16, 16);

                if (dragon.allowedOtherPlayers()) {
                    mc.getTextureManager().bindTexture(lockOpen);
                } else if (!dragon.allowedOtherPlayers()) {
                    mc.getTextureManager().bindTexture(lockLocked);
                }
            }
        }
    }
}