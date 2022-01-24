package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.network.packet.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioUpdateInv implements IMessage {
    ItemStack stackostuff;
    boolean isSWP;
    public BiblioUpdateInv() {
        
    }
    public BiblioUpdateInv(ItemStack stackostuff, boolean isSWP) {
        this.stackostuff = stackostuff;
        this.isSWP = isSWP;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.stackostuff = ByteBufUtils.readItemStack(buf);
        this.isSWP = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.stackostuff);
        buf.writeBoolean(this.isSWP);
    }
    public static class Handler implements IMessageHandler<BiblioUpdateInv, IMessage> {

        @Override
        public IMessage onMessage(BiblioUpdateInv message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            ItemStack stackostuff = message.stackostuff;
            if (stackostuff != ItemStack.EMPTY) {
                boolean safe = true;
                // Attempted fix for exploit based on what the GT:NH developers did
                try {
                    NBTTagList list = stackostuff.getTagCompound().getTagList("Inventory", 10);
                    for (int i = 0; i <= list.tagCount(); i++) {
                        if (!(new ItemStack(list.getCompoundTagAt(i)).getItem() instanceof ItemMap)) {
                            safe = false;
                            break;
                        }
                    }
                } catch (NullPointerException e) {
                    // lazy :D
                }
                if (safe) {
                    ItemStack currentPlayerSlot = player.getHeldItem(EnumHand.MAIN_HAND);
                    if (currentPlayerSlot != ItemStack.EMPTY) {
                        if (currentPlayerSlot.getUnlocalizedName().equals(stackostuff.getUnlocalizedName())
                                && Utils.checkIfValidPacketItem(currentPlayerSlot.getUnlocalizedName())) {
                            NBTTagCompound currentTags = currentPlayerSlot.getTagCompound();
                            NBTTagCompound newTags = stackostuff.getTagCompound();
                            if (!currentPlayerSlot.getUnlocalizedName().contains("item.AtlasBook")) {
                                if (currentTags != null && currentTags.hasKey("Inventory") && newTags != null) {
                                    NBTTagList tagList = currentTags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
                                    newTags.setTag("Inventory", tagList);
                                    stackostuff.setTagCompound(newTags);
                                }
                            } else if (currentTags.hasKey("atlasID") && newTags.hasKey("atlasID")
                                    && currentTags.getInteger("atlasID") != newTags.getInteger("atlasID")) {
                                return null;
                            }
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, stackostuff);
                        }
                    }
                }
            }
            if (message.isSWP) {
                player.closeScreen();
                player.openGui(BiblioCraft.instance, 100, player.world, (int) player.posX, (int) player.posY,
                        (int) player.posZ);
            }
            return null;
        }
        
    }
}
