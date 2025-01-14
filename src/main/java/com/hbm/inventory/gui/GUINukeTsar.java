package com.hbm.inventory.gui;

import org.lwjgl.opengl.GL11;

import com.hbm.inventory.container.ContainerNukeTsar;
import com.hbm.lib.RefStrings;
import com.hbm.tileentity.bomb.TileEntityNukeTsar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUINukeTsar extends GuiContainer {
	
	private static ResourceLocation texture = new ResourceLocation(RefStrings.MODID + ":textures/gui/tsarBombaSchematic.png");
	private TileEntityNukeTsar testNuke;
	
	public GUINukeTsar(InventoryPlayer invPlayer, TileEntityNukeTsar tedf) {
		super(new ContainerNukeTsar(invPlayer, tedf));
		testNuke = tedf;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer( int i, int j) {
		String name = this.testNuke.hasCustomInventoryName() ? this.testNuke.getInventoryName() : I18n.format(this.testNuke.getInventoryName());
		
		this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		super.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		super.drawDefaultBackground();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if(testNuke.isReady())
		{
			drawTexturedModalRect(guiLeft + 133, guiTop + 34, 176, 0, 16, 16);
		}

		if(testNuke.isFilled())
		{
			drawTexturedModalRect(guiLeft + 133, guiTop + 34, 176, 18, 16, 16);
		}
	}
}
