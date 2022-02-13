package com.github.vfyjxf.ae2utilities.client.gui;

import appeng.api.config.Settings;
import appeng.api.config.YesNo;
import appeng.client.gui.implementations.GuiUpgradeable;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.client.gui.widgets.GuiToggleButton;
import appeng.core.localization.GuiText;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketConfigButton;
import appeng.helpers.IInterfaceHost;
import com.github.vfyjxf.ae2utilities.AE2Utilities;
import com.github.vfyjxf.ae2utilities.container.ContainerEnhancedInterface;
import com.github.vfyjxf.ae2utilities.network.Ae2uGuiHandler;
import com.github.vfyjxf.ae2utilities.network.Ae2uNetworkHandler;
import com.github.vfyjxf.ae2utilities.network.packets.PacketSwitchGuis;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiEnhancedInterface extends GuiUpgradeable {

    private GuiTabButton priority;
    private GuiImgButton blockMode;
    private GuiToggleButton interfaceMode;
    private final int tier;

    public GuiEnhancedInterface(final InventoryPlayer inventoryPlayer, final IInterfaceHost te, int tier) {

        super((tier == 3) ? new ContainerEnhancedInterface(inventoryPlayer, te, tier) {
            @Override
            protected int getHeight() {
                return 217;
            }
        } : new ContainerEnhancedInterface(inventoryPlayer, te, tier));
        this.ySize = tier == 3 ? 217 : 211;
        this.tier = tier;
    }

    @Override
    protected void addButtons() {
        if (this.tier == 1) {
            this.priority = new GuiTabButton(this.guiLeft + 154, this.guiTop, 2 + 4 * 16, GuiText.Priority.getLocal(), this.itemRender);
        } else {
            this.priority = new GuiTabButton(this.guiLeft + 154 + 26, this.guiTop + 34, 2 + 4 * 16, GuiText.Priority.getLocal(), this.itemRender);
        }
        this.buttonList.add(this.priority);

        this.blockMode = new GuiImgButton(this.guiLeft - 18, this.guiTop + 8, Settings.BLOCK, YesNo.NO);
        this.buttonList.add(this.blockMode);

        this.interfaceMode = new GuiToggleButton(this.guiLeft - 18, this.guiTop + 26, 84, 85, GuiText.InterfaceTerminal
                .getLocal(), GuiText.InterfaceTerminalHint.getLocal());
        this.buttonList.add(this.interfaceMode);
    }

    @Override
    public void drawFG(final int offsetX, final int offsetY, final int mouseX, final int mouseY) {
        if (this.blockMode != null) {
            this.blockMode.set(((ContainerEnhancedInterface) this.cvb).getBlockingMode());
        }

        if (this.interfaceMode != null) {
            this.interfaceMode.setState(((ContainerEnhancedInterface) this.cvb).getInterfaceTerminalMode() == YesNo.YES);
        }

        this.fontRenderer.drawString(this.getGuiDisplayName(GuiText.Interface.getLocal()), 8, 5, 4210752);

    }

    protected ResourceLocation getBackgroundLocation() {
        return new ResourceLocation(AE2Utilities.MODID, "textures/guis/enhanced_interface_tier_" + this.tier + ".png");
    }

    @Override
    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.handleButtonVisibility();

        this.mc.renderEngine.bindTexture(this.getBackgroundLocation());
        this.drawTexturedModalRect(offsetX, offsetY, 0, 0, 211 - 34, this.ySize);
        if (this.drawUpgrades()) {
            this.drawTexturedModalRect(offsetX + 177, offsetY, 177, 0, 35, 14 + this.cvb.availableUpgrades() * 18);
        }
        if (this.hasToolbox()) {
            this.drawTexturedModalRect(offsetX + 178, offsetY + this.ySize - 90, 178, this.ySize - 90, 68, 68);
        }
    }

    @Override
    protected void actionPerformed(final GuiButton btn) throws IOException {
        super.actionPerformed(btn);

        final boolean backwards = Mouse.isButtonDown(1);

        if (btn == this.priority) {
            Ae2uNetworkHandler.sendToServer(new PacketSwitchGuis(Ae2uGuiHandler.GUI_PRIORITY_ID, true));
        }

        if (btn == this.interfaceMode) {
            NetworkHandler.instance().sendToServer(new PacketConfigButton(Settings.INTERFACE_TERMINAL, backwards));
        }

        if (btn == this.blockMode) {
            NetworkHandler.instance().sendToServer(new PacketConfigButton(this.blockMode.getSetting(), backwards));
        }
    }

}
