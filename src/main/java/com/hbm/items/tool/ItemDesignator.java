package com.hbm.items.tool;

import java.util.List;

import com.hbm.blocks.bomb.LaunchPad;
import com.hbm.items.ModItems;
import com.hbm.lib.HBMSoundHandler;
import com.hbm.main.MainRegistry;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemDesignator extends Item {

	public ItemDesignator(String s) {
		this.setUnlocalizedName(s);
		this.setRegistryName(s);
		this.setCreativeTab(MainRegistry.missileTab);
		
		ModItems.ALL_ITEMS.add(this);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("xCoord", 0);
		stack.getTagCompound().setInteger("zCoord", 0);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(stack.getTagCompound() != null)
		{
			tooltip.add("Target Coordinates:");
			tooltip.add("X: " + String.valueOf(stack.getTagCompound().getInteger("xCoord")));
			tooltip.add("Z: " + String.valueOf(stack.getTagCompound().getInteger("zCoord")));
		} else {
			tooltip.add("Please select a target.");
		}
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(!(world.getBlockState(pos).getBlock() instanceof LaunchPad))
		{
			if(stack.getTagCompound() != null)
			{
				stack.getTagCompound().setInteger("xCoord", pos.getX());
				stack.getTagCompound().setInteger("zCoord", pos.getZ());
			} else {
				stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("xCoord", pos.getX());
				stack.getTagCompound().setInteger("zCoord", pos.getZ());
			}
	        if(world.isRemote)
			{
	        	player.sendMessage(new TextComponentTranslation("Position set!"));
			}

	        world.playSound(player.posX, player.posY, player.posZ, HBMSoundHandler.techBleep, SoundCategory.PLAYERS, 1.0F, 1.0F, true);
        	
	        return EnumActionResult.SUCCESS;
		}
    	
        return EnumActionResult.PASS;
	}
}
