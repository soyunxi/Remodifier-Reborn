package org.yunxi.remodifier.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.yunxi.remodifier.common.network.NetworkHandler;

public class ReforgeTableScreen extends AbstractContainerScreen<ReforgeTableContainer> {
    private final ResourceLocation GUI = ResourceLocation.parse("textures/gui/reforger.png");
    private Button button;

    public ReforgeTableScreen(ReforgeTableContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }

    private void onButtonClicked() {
        // 向服务端发送数据包，触发方块实体逻辑
        NetworkHandler.sendToServer(new ProcessorButtonMessage(menu.getPos()));
    }
}
