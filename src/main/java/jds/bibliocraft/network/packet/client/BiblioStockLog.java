package jds.bibliocraft.network.packet.client;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.helpers.BiblioSortingHelper;
import jds.bibliocraft.helpers.SortedListItem;
import jds.bibliocraft.network.packet.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioStockLog implements IMessage {
    NBTTagCompound tags;
    // TODO: Throwing `EnumHand`s everywhere might not be the best way to do it
    EnumHand hand;
    public BiblioStockLog() {

    }

    public BiblioStockLog(NBTTagCompound tags, EnumHand hand) {
        this.tags = tags;
        this.hand = hand;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.tags = ByteBufUtils.readTag(buf);
        try {
            this.hand = EnumHand.values()[buf.readByte()];
        } catch (Exception e) {
            this.hand = EnumHand.MAIN_HAND;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.tags);
        buf.writeByte(this.hand.ordinal());
    }

    public static class Handler implements IMessageHandler<BiblioStockLog, IMessage> {

        @Override
        public IMessage onMessage(BiblioStockLog message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                NBTTagCompound tags = message.tags;
                if (tags != null) {
                    NBTTagList comp = tags.getTagList("compasses", Constants.NBT.TAG_COMPOUND);
                    int[] compasses = { -1, -1, -1, -1, -1, -1, -1, -1 };
                    ItemStack[] compassStacks = { ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
                            ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY };

                    for (int i = 0; i < comp.tagCount(); i++) {
                        NBTTagCompound compTag = comp.getCompoundTagAt(i);
                        if (compTag != null) {
                            String invSlotName = "slot" + i;
                            int compSlot = compTag.getInteger(invSlotName);
                            compasses[i] = compSlot;
                            if (compSlot != -1) {
                                compassStacks[i] = new ItemStack(compTag);
                            }
                        }
                    }

                    NBTTagList alphaTagList = tags.getTagList("alphaList", Constants.NBT.TAG_COMPOUND);
                    final ArrayList<SortedListItem> alphaList = BiblioSortingHelper
                            .convertNBTTagListToArrayList(alphaTagList);
                    NBTTagList quanaTagList = tags.getTagList("quantaList", Constants.NBT.TAG_COMPOUND);
                    final ArrayList<SortedListItem> quantaList = BiblioSortingHelper
                            .convertNBTTagListToArrayList(quanaTagList);
                    final int[] finalCompasses = compasses;
                    final ItemStack[] finalCompassStacks = compassStacks;
                    final String title = tags.getString("title");
                    // openCatalogGUI(player, alphaList, quantaList, compassStacks, compasses,
                    // tags.getString("title"));
                    Minecraft.getMinecraft().addScheduledTask(new Runnable() {

                        @Override
                        public void run() {
                            Utils.openCatalogGUI(Minecraft.getMinecraft().player, alphaList, quantaList,
                                    finalCompassStacks,
                                    finalCompasses, title, message.hand);
                        }
                    });
                }
            });
            return null;
        }

    }
}
