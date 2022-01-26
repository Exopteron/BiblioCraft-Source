package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.items.ItemStockroomCatalog;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioStockTitle implements IMessage {
    String title;
    EnumHand hand;
    public BiblioStockTitle() {

    }

    public BiblioStockTitle(String title, EnumHand hand) {
        this.title = title;
        this.hand = hand;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.title = ByteBufUtils.readUTF8String(buf);
        try {
            this.hand = EnumHand.values()[buf.readByte()];
        } catch (Exception e) {
            this.hand = EnumHand.MAIN_HAND;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.title);
        buf.writeByte(this.hand.ordinal());
    }

    public static class Handler implements IMessageHandler<BiblioStockTitle, IMessage> {

        @Override
        public IMessage onMessage(BiblioStockTitle message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                ItemStack stockroomcatalog = player.getHeldItem(message.hand);
                if (stockroomcatalog != ItemStack.EMPTY && stockroomcatalog.getItem() instanceof ItemStockroomCatalog) {
                    NBTTagCompound tags = stockroomcatalog.getTagCompound();
                    if (tags == null) {
                        tags = new NBTTagCompound();
                    }
                    NBTTagCompound display = new NBTTagCompound();
                    message.title = message.title.substring(0, Math.min(message.title.length(), 24)); // make sure string is in length
                    display.setString("Name", TextFormatting.WHITE + message.title);
                    tags.setTag("display", display);
                    stockroomcatalog.setTagCompound(tags);
                    player.setHeldItem(message.hand, stockroomcatalog);
                    //player.inventory.setInventorySlotContents(player.inventory.currentItem, stockroomcatalog);
                }
            });
            return null;
        }

    }
}
