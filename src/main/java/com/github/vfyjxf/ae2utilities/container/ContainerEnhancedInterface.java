package com.github.vfyjxf.ae2utilities.container;

import appeng.api.config.SecurityPermissions;
import appeng.api.config.Settings;
import appeng.api.config.YesNo;
import appeng.api.util.IConfigManager;
import appeng.container.guisync.GuiSync;
import appeng.container.implementations.ContainerUpgradeable;
import appeng.container.slot.SlotFake;
import appeng.container.slot.SlotNormal;
import appeng.container.slot.SlotRestrictedInput;
import appeng.helpers.DualityInterface;
import appeng.helpers.IInterfaceHost;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerEnhancedInterface extends ContainerUpgradeable {

    private final int tier;
    private final DualityInterface myDuality;

    @GuiSync(3)
    public YesNo bMode = YesNo.NO;

    @GuiSync(4)
    public YesNo iTermMode = YesNo.YES;

    public ContainerEnhancedInterface(InventoryPlayer ip, IInterfaceHost te, int tier) {
        super(ip, te.getInterfaceDuality().getHost());
        this.tier = tier;
        this.myDuality = te.getInterfaceDuality();

        if (this.tier == 1) {

            for (int y = 0; y < tier + 1; y++) {
                for (int x = 0; x < DualityInterface.NUMBER_OF_PATTERN_SLOTS; x++) {
                    this.addSlotToContainer(new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.ENCODED_PATTERN, this.myDuality
                            .getPatterns(), x + y * DualityInterface.NUMBER_OF_PATTERN_SLOTS, 8 + 18 * x, 79 + y * 18, this.getInventoryPlayer()));
                }
            }

            for (int x = 0; x < DualityInterface.NUMBER_OF_CONFIG_SLOTS; x++) {
                this.addSlotToContainer(new SlotFake(this.myDuality.getConfig(), x, 8 + 18 * x, 35));
            }

            for (int x = 0; x < DualityInterface.NUMBER_OF_STORAGE_SLOTS; x++) {
                this.addSlotToContainer(new SlotNormal(this.myDuality.getStorage(), x, 8 + 18 * x, 35 + 18));
            }

        } else if (this.tier == 2) {

            for (int y = 0; y < tier + 1; y++) {
                for (int x = 0; x < DualityInterface.NUMBER_OF_PATTERN_SLOTS; x++) {
                    this.addSlotToContainer(new SlotRestrictedInput(
                            SlotRestrictedInput.PlacableItemType.ENCODED_PATTERN, this.myDuality
                            .getPatterns(), x + y * DualityInterface.NUMBER_OF_PATTERN_SLOTS, 8 + 18 * x, 61 + y * 18, this.getInventoryPlayer()));
                }
            }

            for (int x = 0; x < DualityInterface.NUMBER_OF_CONFIG_SLOTS; x++) {
                this.addSlotToContainer(new SlotFake(this.myDuality.getConfig(), x, 8 + 18 * x, 17));
            }

            for (int x = 0; x < DualityInterface.NUMBER_OF_STORAGE_SLOTS; x++) {
                this.addSlotToContainer(new SlotNormal(this.myDuality.getStorage(), x, 8 + 18 * x, 17 + 18));
            }

        } else if (this.tier == 3) {

            for (int y = 0; y < tier + 1; y++) {
                for (int x = 0; x < DualityInterface.NUMBER_OF_PATTERN_SLOTS; x++) {
                    this.addSlotToContainer(new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.ENCODED_PATTERN, this.myDuality
                            .getPatterns(), x + y * DualityInterface.NUMBER_OF_PATTERN_SLOTS, 8 + 18 * x, 57 + y * 18, this.getInventoryPlayer()));
                }
            }

            for (int x = 0; x < DualityInterface.NUMBER_OF_CONFIG_SLOTS; x++) {
                this.addSlotToContainer(new SlotFake(this.myDuality.getConfig(), x, 8 + 18 * x, 15));
            }

            for (int x = 0; x < DualityInterface.NUMBER_OF_STORAGE_SLOTS; x++) {
                this.addSlotToContainer(new SlotNormal(this.myDuality.getStorage(), x, 8 + 18 * x, 15 + 18));
            }

        }

    }

    @Override
    protected int getHeight() {
        return 211;
    }

    @Override
    protected void setupConfig() {
        this.setupUpgrades();
    }

    @Override
    public int availableUpgrades() {
        return 1;
    }

    @Override
    public void detectAndSendChanges() {
        this.verifyPermissions(SecurityPermissions.BUILD, false);
        super.detectAndSendChanges();
    }

    @Override
    protected void loadSettingsFromHost(final IConfigManager cm) {
        this.setBlockingMode((YesNo) cm.getSetting(Settings.BLOCK));
        this.setInterfaceTerminalMode((YesNo) cm.getSetting(Settings.INTERFACE_TERMINAL));
    }

    public YesNo getBlockingMode() {
        return this.bMode;
    }

    private void setBlockingMode(final YesNo bMode) {
        this.bMode = bMode;
    }

    public YesNo getInterfaceTerminalMode() {
        return this.iTermMode;
    }

    private void setInterfaceTerminalMode(final YesNo iTermMode) {
        this.iTermMode = iTermMode;
    }
}
