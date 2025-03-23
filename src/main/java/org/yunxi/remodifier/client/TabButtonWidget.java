package org.yunxi.remodifier.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TabButtonWidget extends Button {
    public boolean toggled;

    protected ResourceLocation texture;
    protected int u;
    protected int v;
    protected int pressedUOffset;
    protected int hoverVOffset;

    public TabButtonWidget(int x, int y, int width, int height, Component text, OnPress onPress) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION);
    }

    public TabButtonWidget(int x, int y, int width, int height, Component text, OnPress onPress, Component tooltip) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION);
        this.setTooltip(Tooltip.create(tooltip));
    }

    public void setTextureUV(int u, int v, int pressedUOffset, int hoverVOffset, ResourceLocation texture) {
        this.u = u;
        this.v = v;
        this.pressedUOffset = pressedUOffset;
        this.hoverVOffset = hoverVOffset;
        this.texture = texture;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();

        // 设置纹理
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int currentU = this.u;
        int currentV = this.v;

        if (this.toggled) {
            currentU += this.pressedUOffset;
        }

        if (this.isHovered()) {
            currentV += this.hoverVOffset;
        }

        // 绘制按钮背景
        guiGraphics.blit(this.texture,
                getX(), getY(),
                currentU, currentV,
                this.width, this.height);

        // 绘制按钮文字
        int color = this.active ? 0xFFFFFF : 0xA0A0A0;
        guiGraphics.drawCenteredString(
                minecraft.font,
                this.getMessage(),
                this.getX() + this.width / 2,
                this.getY() + (this.height - 8) / 2,
                color | (Math.round(this.alpha * 255.0F) << 24)
        );

        // 绘制工具提示
        if (this.isHovered() && this.getTooltip() != null) {
            guiGraphics.renderTooltip(minecraft.font, (Component) this.getTooltip(), mouseX, mouseY);
        }
    }
}
