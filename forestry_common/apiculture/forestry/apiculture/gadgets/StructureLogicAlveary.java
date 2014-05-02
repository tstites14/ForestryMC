/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.apiculture.gadgets;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

import forestry.api.apiculture.IAlvearyComponent;
import forestry.api.core.ITileStructure;
import forestry.core.config.ForestryBlock;
import forestry.core.gadgets.BlockStructure.EnumStructureState;
import forestry.core.gadgets.StructureLogic;
import forestry.core.utils.Schemata;
import forestry.core.utils.Schemata.EnumStructureBlock;
import forestry.core.utils.Vect;

public class StructureLogicAlveary extends StructureLogic {

	/* CONSTANTS */
	public static final String UID_ALVEARY = "alveary";
	public static final Schemata SCHEMATA_ALVEARY = new Schemata("alveary3x3", 5, 6, 5, "FFFFF", "FFFFF", "FFFFF", "FFFFF", "FFFFF", "FFFFF", "FFFFF", "FAAAF",
			"FAAAF", "FABAF", "FCCCF", "FFFFF", "FFFFF", "FAAAF", "FAAAF", "FBMBF", "FCCCF", "FFFFF", "FFFFF", "FAAAF", "FAAAF", "FABAF", "FCCCF", "FFFFF",
			"FFFFF", "FFFFF", "FFFFF", "FFFFF", "FFFFF", "FFFFF").setOffsets(-2, -3, -2);

	public static HashSet<Block> slabBlocks = new HashSet<Block>();
	static {
		slabBlocks.add(Blocks.stone_slab);
		slabBlocks.add(Blocks.wooden_slab);
		slabBlocks.add(ForestryBlock.slabs1);
		slabBlocks.add(ForestryBlock.slabs2);
		slabBlocks.add(ForestryBlock.slabs3);
		if (ForestryBlock.slabs4 != null)
			slabBlocks.add(ForestryBlock.slabs4);
	}

	public StructureLogicAlveary(ITileStructure structure) {
		super(UID_ALVEARY, structure);
		schematas = new Schemata[] { SCHEMATA_ALVEARY };
		metaOnValid.put(EnumStructureBlock.BLOCK_B, 1);
	}

	@Override
	protected EnumStructureState determineMasterState(Schemata schemata, boolean rotate) {

		Vect dimensions = schemata.getDimensions(rotate);
		int offsetX = schemata.getxOffset();
		int offsetZ = schemata.getzOffset();
		if (rotate) {
			offsetX = schemata.getzOffset();
			offsetZ = schemata.getxOffset();
		}

		for (int i = 0; i < dimensions.x; i++)
			for (int j = 0; j < schemata.getHeight(); j++)
				for (int k = 0; k < dimensions.z; k++) {
					int x = structureTile.xCoord + i + offsetX;
					int y = structureTile.yCoord + j + schemata.getyOffset();
					int z = structureTile.zCoord + k + offsetZ;

					if (!structureTile.getWorldObj().blockExists(x, y, z))
						return EnumStructureState.INDETERMINATE;

					EnumStructureBlock required = schemata.getAt(i, j, k, rotate);
					if (required == EnumStructureBlock.ANY)
						continue;

					TileEntity tile = structureTile.getWorldObj().getTileEntity(x, y, z);
					Block block = structureTile.getWorldObj().getBlock(x, y, z);

					switch (required) {
					case AIR:
						if (!block.isAir(structureTile.getWorldObj(), x, y, z))
							return EnumStructureState.INVALID;
						break;
					case BLOCK_A:
						if (tile == null || !(tile instanceof IAlvearyComponent))
							return EnumStructureState.INVALID;
						if (!((ITileStructure) tile).getTypeUID().equals(UID_ALVEARY))
							return EnumStructureState.INVALID;
						break;
					case MASTER:
					case BLOCK_B:
						if (tile == null || !(tile instanceof TileAlvearyPlain))
							return EnumStructureState.INVALID;
						break;
					case BLOCK_C:
						if (!slabBlocks.contains(block))
							return EnumStructureState.INVALID;
						if ((structureTile.getWorldObj().getBlockMetadata(x, y, z) & 8) != 0)
							return EnumStructureState.INVALID;
						break;
					case BLOCK_D:
						if (block != Blocks.spruce_stairs)
							return EnumStructureState.INVALID;
						break;
					case FOREIGN:
						if (tile instanceof ITileStructure)
							return EnumStructureState.INVALID;
						break;
					default:
						return EnumStructureState.INDETERMINATE;
					}
				}

		return EnumStructureState.VALID;
	}

}
