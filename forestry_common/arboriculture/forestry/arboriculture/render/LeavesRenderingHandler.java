/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.arboriculture.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import forestry.arboriculture.gadgets.BlockLeaves;
import forestry.arboriculture.gadgets.TileLeaves;
import forestry.core.render.OverlayRenderingHandler;
import forestry.plugins.PluginArboriculture;

/**
 * Ugly but serviceable renderer for leaves, taking fruits into account.
 */
public class LeavesRenderingHandler extends OverlayRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

		// Render the plain leaf block.
		renderer.renderStandardBlock(block, x, y, z);

		// Render overlay for fruit leaves.
		TileLeaves tile = BlockLeaves.getLeafTile(world, x, y, z);
		IIcon fruitIcon = null;
		int fruitColor = 0xffffff;
		if (tile != null) {
			fruitIcon = tile.getFruitTexture();
			fruitColor = tile.getFruitColour();
		}

		if (fruitIcon != null)
			renderFruitOverlay(world, block, x, y, z, renderer, fruitIcon, fruitColor);

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return PluginArboriculture.modelIdLeaves;
	}

	private boolean renderFruitOverlay(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer, IIcon texture, int multiplier) {

		float mR = (multiplier >> 16 & 255) / 255.0F;
		float mG = (multiplier >> 8 & 255) / 255.0F;
		float mB = (multiplier & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			mR = (mR * 30.0F + mG * 59.0F + mB * 11.0F) / 100.0F;
			mG = (mR * 30.0F + mG * 70.0F) / 100.0F;
			mB = (mR * 30.0F + mB * 70.0F) / 100.0F;
		}

		return renderFruitOverlayWithColorMultiplier(world, block, x, y, z, mR, mG, mB, renderer, texture);
	}

	private boolean renderFruitOverlayWithColorMultiplier(IBlockAccess world, Block block, int x, int y, int z, float r, float g, float b,
			RenderBlocks renderer, IIcon texture) {

		int mixedBrightness = block.getMixedBrightnessForBlock(world, x, y, z);

		float adjR = 0.5f * r;
		float adjG = 0.5f * g;
		float adjB = 0.5f * b;

		// Bottom
		renderBottomFace(world, block, x, y, z, renderer, texture, mixedBrightness, adjR, adjG, adjB);
		renderTopFace(world, block, x, y, z, renderer, texture, mixedBrightness, adjR, adjG, adjB);
		renderEastFace(world, block, x, y, z, renderer, texture, mixedBrightness, adjR, adjG, adjB);
		renderWestFace(world, block, x, y, z, renderer, texture, mixedBrightness, adjR, adjG, adjB);
		renderNorthFace(world, block, x, y, z, renderer, texture, mixedBrightness, adjR, adjG, adjB);
		renderSouthFace(world, block, x, y, z, renderer, texture, mixedBrightness, adjR, adjG, adjB);

		return true;
	}

}
