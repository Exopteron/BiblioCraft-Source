package jds.bibliocraft.gui;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.tileentities.TileEntityShelf;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.translation.I18n;

public class GuiGenericShelf extends GuiContainer
{
	
	
	public GuiGenericShelf(InventoryPlayer inventoryPlayer, TileEntityShelf genericShelf)
	{
		super(new jds.bibliocraft.containers.ContainerGenericShelf(inventoryPlayer, genericShelf));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		this.fontRenderer.drawString(I18n.translateToLocal("gui.shelf"), 8, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CommonProxy.GENERICSHELFGUI_PNG);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
