package org.yunxi.remodifier.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.yunxi.remodifier.Remodifier;
import org.yunxi.remodifier.common.block.ReforgedTableBlockEntity;
import org.yunxi.remodifier.common.network.NetworkHandler;
import org.yunxi.remodifier.common.network.ReforgeTableButtonPacket;

import java.util.function.Supplier;

public class ReforgeTableScreen extends AbstractContainerScreen<ReforgeTableContainer> {
    private final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(Remodifier.MODID, "textures/gui/reforger.png");
    private Button button;

    private static final int GUI_X_TEXTURE = 0;
    private static final int GUI_Y_TEXTURE = 0;
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 166;

    // 修改按钮纹理坐标和尺寸
    private static final int BUTTON_X_TEXTURE = 0;
    private static final int BUTTON_Y_NORMAL = 166;
    private static final int BUTTON_Y_HOVER = 186;
    private static final int BUTTON_Y_DISABLED = 206;
    private static final int BUTTON_Y_DISABLED_HOVER = 226;
    private static final int BUTTON_WIDTH = 20;
    private static final int BUTTON_HEIGHT = 20;

    private static final int ICON_X_TEXTURE = 176;
    private static final int ICON_Y_TEXTURE = 0;
    private static final int ICON_WIDTH = 28;
    private static final int ICON_HEIGHT = 21;

    private static final int BUTTON_X_POS = 122;
    private static final int BUTTON_Y_POS = 45;
    private static final int ICON_X_POS = 46;
    private static final int ICON_Y_POS = 45;

    public ReforgeTableScreen(ReforgeTableContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.button = new ReforgeTableButton(
                relX + BUTTON_X_POS,
                relY + BUTTON_Y_POS,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                Component.literal(""),
                this::onPress,
                this::createNarrationMessage,
                menu.getReforgedTableBlockEntity().canProcess());
        this.addRenderableWidget(button);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
        renderButton(guiGraphics, i, i1);
        if (!menu.getReforgedTableBlockEntity().canProcess()) {
            renderIcon(guiGraphics);
        }
    }

    private void renderButton(GuiGraphics graphics, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;

        boolean canProcess = menu.getReforgedTableBlockEntity().canProcess();
        boolean isHovering = mouseX >= relX + BUTTON_X_POS &&
                mouseX < relX + BUTTON_X_POS + BUTTON_WIDTH &&
                mouseY >= relY + BUTTON_Y_POS &&
                mouseY < relY + BUTTON_Y_POS + BUTTON_HEIGHT;
        int yTexture = BUTTON_Y_DISABLED;
        if (canProcess) {
            yTexture = isHovering ? BUTTON_Y_HOVER : BUTTON_Y_NORMAL;
        } else {
            yTexture = isHovering ? BUTTON_Y_DISABLED_HOVER : BUTTON_Y_DISABLED;
        }

        graphics.blit(GUI,
                relX + BUTTON_X_POS,
                relY + BUTTON_Y_POS,
                BUTTON_X_TEXTURE,
                yTexture,
                BUTTON_WIDTH,
                BUTTON_HEIGHT);
    }

    private void renderIcon(GuiGraphics graphics) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX + ICON_X_POS, relY + ICON_Y_POS, ICON_X_TEXTURE, ICON_Y_TEXTURE, ICON_WIDTH, ICON_HEIGHT);
    }

    public @NotNull MutableComponent createNarrationMessage(Supplier<MutableComponent> supplier) {
        ReforgedTableBlockEntity blockEntity = menu.getReforgedTableBlockEntity();
        if (blockEntity.canProcess()) {
            return Component.translatable("gui.remodifier.processor_button");
        } else {
            return Component.translatable("gui.remodifier.missing_item");
        }
    }


    public void onPress(Button button) {
        NetworkHandler.sendToServer(new ReforgeTableButtonPacket(menu.getPos()));
    }
}
