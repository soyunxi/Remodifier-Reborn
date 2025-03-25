package org.yunxi.remodifier.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public class ReforgeTableButton extends Button {

    protected ReforgeTableButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.isHovered = pMouseX >= this.getX() && pMouseY >= this.getY() && pMouseX < this.getX() + this.width && pMouseY < this.getY() + this.height;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.active && this.isHovered){
            return super.mouseClicked(pMouseX, pMouseY, pButton);
        }
        return false;
    }

    @Override
    public void onPress() {
        if (this.active) {
            super.onPress();
        }
    }

    @Override
    public void playDownSound(SoundManager pHandler) {
        if (this.active){
            pHandler.play(SimpleSoundInstance.forUI(SoundEvents.ANVIL_PLACE, 1.0F));
        }
    }
}
