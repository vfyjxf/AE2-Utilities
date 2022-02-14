package com.github.vfyjxf.ae2utilities.mixins.client;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiInterfaceTerminal;
import appeng.client.gui.widgets.MEGuiTextField;
import appeng.client.me.ClientDCInternalInv;
import appeng.client.me.SlotDisconnected;
import appeng.core.localization.GuiText;
import com.google.common.collect.HashMultimap;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(value = GuiInterfaceTerminal.class, remap = false)
public abstract class MixinGuiInterfaceTerminal extends AEBaseGui {

    @Shadow
    @Final
    private HashMap<Long, ClientDCInternalInv> byId;

    @Shadow
    private boolean refreshList;

    @Shadow
    protected abstract void refreshList();

    @Shadow
    @Final
    private Map<String, Set<Object>> cachedSearches;

    @Shadow
    @Final
    private static int LINES_ON_PAGE;

    @Shadow
    @Final
    private HashMultimap<String, ClientDCInternalInv> byName;

    @Shadow
    private MEGuiTextField searchField;

    @Shadow
    @Final
    private ArrayList<String> names;

    private final Map<Integer, Object> byLine = new HashMap<>();
    private final Map<Integer, Integer> invLines = new HashMap<>();

    private MixinGuiInterfaceTerminal(Container container) {
        super(container);
    }

    /**
     * @author vfyjxf
     * @reason Compatible with Enhanced Interface
     */
    @Override
    @Overwrite
    public void drawFG(final int offsetX, final int offsetY, final int mouseX, final int mouseY) {

        this.fontRenderer.drawString(this.getGuiDisplayName(GuiText.InterfaceTerminal.getLocal()), 8, 6, 4210752);
        this.fontRenderer.drawString(GuiText.inventory.getLocal(), 8, this.ySize - 96 + 3, 4210752);

        final int ex = this.getScrollBar().getCurrentScroll();

        this.inventorySlots.inventorySlots.removeIf(slot -> slot instanceof SlotDisconnected);

        int offset = 17;
        int linesDraw = 0;
        for (int x = 0; x < LINES_ON_PAGE && linesDraw < LINES_ON_PAGE && ex + x < byLine.size(); x++) {
            final Object lineObj = this.byLine.get(ex + x);
            if (lineObj instanceof ClientDCInternalInv) {

                ClientDCInternalInv inv = (ClientDCInternalInv) lineObj;
                int currentInvLine = invLines.get(ex + x);
                for (int i = 0; i < 9; i++) {
                    this.inventorySlots.inventorySlots.add(new SlotDisconnected(inv, i + (currentInvLine * 9), (i * 18 + 8), 1 + offset));
                }
                linesDraw++;
                offset += 18;

            } else if (lineObj instanceof String) {
                String name = (String) lineObj;
                final int rows = this.byName.get(name).size();
                if (rows > 1) {
                    name = name + " (" + rows + ')';
                }

                while (name.length() > 2 && this.fontRenderer.getStringWidth(name) > 155) {
                    name = name.substring(0, name.length() - 1);
                }

                this.fontRenderer.drawString(name, 10, 6 + offset, 4210752);
                linesDraw++;
                offset += 18;
            }
        }

    }

    /**
     * @author vfyjxf
     * @reason Compatible with Enhanced Interface
     */
    @Override
    @Overwrite
    public void drawBG(final int offsetX, final int offsetY, final int mouseX, final int mouseY) {
        this.bindTexture("guis/interfaceterminal.png");
        this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);

        int offset = 17;
        final int ex = this.getScrollBar().getCurrentScroll();
        int linesDraw = 0;
        for (int x = 0; x < LINES_ON_PAGE && linesDraw < LINES_ON_PAGE && ex + x < byLine.size(); x++) {
            final Object lineObj = this.byLine.get(ex + x);
            if (lineObj instanceof ClientDCInternalInv) {
                final int width = 9 * 18;
                this.drawTexturedModalRect(offsetX + 7, offsetY + offset, 7, 139, width, 18);
            }
            offset += 18;
            linesDraw++;
        }

        if (this.searchField != null) {
            this.searchField.drawTextBox();
        }
    }

    /**
     * @author vfyjxf
     * @reason Compatible with Enhanced Interface
     */
    @Overwrite
    public void postUpdate(final NBTTagCompound in) {

        if (in.getBoolean("clear")) {
            this.byId.clear();
            this.refreshList = true;
        }

        for (final String oKey : in.getKeySet()) {
            if (oKey.startsWith("=")) {
                try {
                    final long id = Long.parseLong(oKey.substring(1), Character.MAX_RADIX);
                    final NBTTagCompound invData = in.getCompoundTag(oKey);
                    final ClientDCInternalInv current = this.getById(invData.getInteger("tier"), id, invData.getLong("sortBy"), invData.getString("un"));

                    for (int x = 0; x < current.getInventory().getSlots(); x++) {
                        final String which = Integer.toString(x);
                        if (invData.hasKey(which)) {
                            current.getInventory().setStackInSlot(x, new ItemStack(invData.getCompoundTag(which)));
                        }
                    }
                } catch (final NumberFormatException ignored) {
                }
            }
        }

        if (this.refreshList) {
            this.refreshList = false;
            // invalid caches on refresh
            this.cachedSearches.clear();
            this.refreshList();
        }
    }

    @Inject(method = "refreshList", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;iterator()Ljava/util/Iterator;", ordinal = 0), remap = false, cancellable = true)
    private void injectRefreshList(CallbackInfo ci) {
        byLine.clear();
        invLines.clear();
        int line = 0;
        for (String n : this.names) {
            byLine.put(line++, n);
            final ArrayList<ClientDCInternalInv> clientInventories = new ArrayList<>(this.byName.get(n));
            Collections.sort(clientInventories);
            for (ClientDCInternalInv inv : clientInventories) {
                for (int i = 0; i < (inv.getInventory().getSlots() / 9); i++) {
                    byLine.put(line, inv);
                    invLines.put(line, i);
                    line++;
                }
            }
        }
        this.getScrollBar().setRange(0, byLine.size() - LINES_ON_PAGE, 2);
        ci.cancel();
    }

    private ClientDCInternalInv getById(final int tier, final long id, final long sortBy, final String string) {

        ClientDCInternalInv o = this.byId.get(id);

        if (o == null) {
            this.byId.put(id, o = new ClientDCInternalInv((tier + 1) * 9, id, sortBy, string));
            this.refreshList = true;
        }

        return o;
    }


}
