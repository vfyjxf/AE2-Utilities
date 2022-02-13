package com.github.vfyjxf.ae2utilities.client.gui;

import appeng.client.gui.implementations.GuiPriority;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.helpers.IPriorityHost;
import com.github.vfyjxf.ae2utilities.network.Ae2uGuiHandler;
import com.github.vfyjxf.ae2utilities.network.Ae2uNetworkHandler;
import com.github.vfyjxf.ae2utilities.network.packets.PacketSwitchGuis;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.io.IOException;

public class GuiAe2uPriority extends GuiPriority {

    private final int originalGui;

    private GuiTabButton originalGuiBtn;

    public GuiAe2uPriority(InventoryPlayer inventoryPlayer, IPriorityHost te) {
        super(inventoryPlayer, te);
        if (te instanceof TileEnhancedInterfaceBase) {
            originalGui = ((TileEnhancedInterfaceBase) te).getGuiId();
        } else {
            originalGui = Ae2uGuiHandler.GUI_ENHANCED_INTERFACE_TIER_1_ID;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        originalGuiBtn = ObfuscationReflectionHelper.getPrivateValue(GuiPriority.class, this, "originalGuiBtn");
    }

    @Override
    protected void actionPerformed(GuiButton btn) throws IOException {
        if (btn == this.originalGuiBtn) {
            Ae2uNetworkHandler.sendToServer(new PacketSwitchGuis(this.originalGui, true));
        } else {
            super.actionPerformed(btn);
        }
    }
}
