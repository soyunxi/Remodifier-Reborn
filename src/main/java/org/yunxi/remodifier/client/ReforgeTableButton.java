package org.yunxi.remodifier.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ReforgeTableButton extends Button {
    private final boolean canProcess;

    protected ReforgeTableButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration, boolean canProcess) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
        this.canProcess = canProcess;
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
        if (canProcess) {
            super.onPress();
        }
    }
}
