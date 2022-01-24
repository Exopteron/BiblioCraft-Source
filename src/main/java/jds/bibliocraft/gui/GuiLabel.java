package jds.bibliocraft.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.translation.I18n;
import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.containers.ContainerLabel;
import jds.bibliocraft.tileentities.TileEntityLabel;

public class GuiLabel extends GuiContainer 
{
	public GuiLabel(InventoryPlayer inventoryPlayer, TileEntityLabel woodLabel)
	{
		super(new ContainerLabel(inventoryPlayer, woodLabel));
		//this.woodLabel = woodLabel;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		this.fontRenderer.drawString(I18n.translateToLocal("gui.label"), 8, 6, 4210752);
		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CommonProxy.WOODLABELGUI_PNG);
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
