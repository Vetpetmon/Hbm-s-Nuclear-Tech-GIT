package com.hbm.items.tool;

import java.util.List;

import org.apache.logging.log4j.Level;

import com.hbm.config.GeneralConfig;
import com.hbm.interfaces.IBomb;
import com.hbm.items.ModItems;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.main.MainRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemDetonator extends Item {

	public ItemDetonator(String s) {
		this.setUnlocalizedName(s);
		this.setRegistryName(s);
		this.setCreativeTab(MainRegistry.controlTab);
		
		ModItems.ALL_ITEMS.add(this);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> list, ITooltipFlag flagIn) {
		list.add("Shift right-click to set position,");
		list.add("right-click to detonate!");
		if(stack.getTagCompound() == null)
		{
			list.add("No position set!");
		} else {
			list.add("Set pos to " + stack.getTagCompound().getInteger("x") + ", " + stack.getTagCompound().getInteger("y") + ", " + stack.getTagCompound().getInteger("z"));
		}
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getTagCompound() == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		
		if(player.isSneaking())
		{
			stack.getTagCompound().setInteger("x", pos.getX());
			stack.getTagCompound().setInteger("y", pos.getY());
			stack.getTagCompound().setInteger("z", pos.getZ());
			
			if(world.isRemote)
			{
				player.sendMessage(new TextComponentTranslation("Position set!"));
			}
			
	        world.playSound(null, player.posX, player.posY, player.posZ, HBMSoundHandler.techBoop, SoundCategory.AMBIENT, 1.0F, 1.0F);
        	
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn) {
		ItemStack stack = player.getHeldItem(handIn);
		if(stack.getTagCompound() == null)
		{
			if(world.isRemote)
				player.sendMessage(new TextComponentTranslation("Error: Position not set."));
		} else {
			 int x = stack.getTagCompound().getInteger("x");
			 int y = stack.getTagCompound().getInteger("y");
			 int z = stack.getTagCompound().getInteger("z");
			 BlockPos pos = new BlockPos(x, y, z);
			 if(world.isBlockLoaded(pos) && world.getBlockState(pos).getBlock() instanceof IBomb)
			 {
				world.playSound(null, player.posX, player.posY, player.posZ, HBMSoundHandler.techBleep, SoundCategory.AMBIENT, 1.0F, 1.0F);
				
				
				if(!world.isRemote)
				{
					((IBomb)world.getBlockState(pos).getBlock()).explode(world, pos);
		    		if(GeneralConfig.enableExtendedLogging)
		    			MainRegistry.logger.log(Level.INFO, "[DET] Tried to detonate block at " + x + " / " + y + " / " + z + " by " + player.getDisplayName() + "!");
				}
				if(world.isRemote)
				{
		    		player.sendMessage(new TextComponentTranslation("Detonated!"));
				}
			 } else {
				if(world.isRemote)
				{
					player.sendMessage(new TextComponentTranslation("Error: Target incompatible or too far away."));
				}
			 }
		}
		return super.onItemRightClick(world, player, handIn);
	}
}
