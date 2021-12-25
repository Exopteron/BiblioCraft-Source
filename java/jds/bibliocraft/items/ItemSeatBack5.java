package jds.bibliocraft.items;

import java.util.List;

import jds.bibliocraft.BiblioTab;
import jds.bibliocraft.BlockLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSeatBack5 extends Item
{
	private static final String[] subNames = {"OakSeatBack", "SpruceSeatBack", "BirchSeatBack", "JungleSeatBack", "AcaciaSeatBack", "OldOakSeatBack", "FramedSeatBack"};
	public static final String name = "seatback5";
	public static final ItemSeatBack5 instance = new ItemSeatBack5();
	
	public ItemSeatBack5()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		setHasSubtypes(true);
		maxStackSize = 64;
		setRegistryName(name);
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
    	if (tab instanceof BiblioTab)
    	{
	    	list.add(new ItemStack(this, 1, 0)); //oak
	    	list.add(new ItemStack(this, 1, 1)); //spruce
	    	list.add(new ItemStack(this, 1, 2)); //birch
	    	list.add(new ItemStack(this, 1, 3)); //jungle
	    	list.add(new ItemStack(this, 1, 4)); //acacia
	    	list.add(new ItemStack(this, 1, 5)); //bigoak
	    	list.add(new ItemStack(this, 1, 6));
    	}
     }
    
	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return getUnlocalizedName()+"."+subNames[itemStack.getItemDamage()];
	}

    @SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) 
	{
    	if (stack.getItemDamage() == 6)
    	{
    		NBTTagCompound nbt = stack.getTagCompound();
    		if (nbt != null)
    		{
    			tooltip.add(I18n.translateToLocal("item.paneler.panels")+" \u00a7o"+nbt.getString("renderTexture"));
    		}
    	}
    	super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
