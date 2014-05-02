/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.arboriculture.gadgets;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import forestry.core.proxy.Proxies;
import forestry.core.render.TextureManager;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.StackUtils;
import forestry.plugins.PluginArboriculture;

public class BlockFruitPod extends BlockCocoa {

	public BlockFruitPod() {
		super();
	}

	public static TileFruitPod getPodTile(IBlockAccess world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (!(tile instanceof TileFruitPod))
			return null;

		return (TileFruitPod) tile;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {

		if (!canBlockStay(world, x, y, z)) {
			dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
			return;
		}

		TileFruitPod tile = getPodTile(world, x, y, z);
		if (tile == null)
			return;

		tile.onBlockTick();
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if(Proxies.common.isSimulating(world)) {
			TileFruitPod tile = getPodTile(world, x, y, z);
			if (tile != null) {
				for(ItemStack drop : tile.getDrop()) {
					StackUtils.dropItemStackAsEntity(drop, world, x, y, z);
				}
			}
		}

		return super.removedByPlayer(world, player, x, y, z);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		return new ArrayList<ItemStack>();
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return BlockUtil.getDirectionalMetadata(world, x, y, z) >= 0;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		world.removeTileEntity(x, y, z);
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public boolean hasTileEntity(int meta) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new TileFruitPod();
	}

	/* ICONS */
	@SideOnly(Side.CLIENT)
	private static IIcon defaultIcon;

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		defaultIcon = TextureManager.getInstance().registerTex(register, "pods/papaya.2");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int par1, int par2) {
		return defaultIcon;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileFruitPod pod = getPodTile(world, x, y, z);
		if (pod != null) {
			IIcon podIcon = pod.getIcon(world.getBlockMetadata(x, y, z), side);
			if (podIcon != null)
				return podIcon;
		}

		return defaultIcon;
	}

	@Override
	public int getRenderType() {
		return PluginArboriculture.modelIdPods;
	}

}
