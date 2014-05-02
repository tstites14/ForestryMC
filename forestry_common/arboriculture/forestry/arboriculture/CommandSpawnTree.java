/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.arboriculture;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.gen.feature.WorldGenerator;

import forestry.api.world.ITreeGenData;
import forestry.arboriculture.genetics.TreeTemplates;
import forestry.arboriculture.worldgen.WorldGenBalsa;
import forestry.core.utils.CommandMC;
import forestry.plugins.PluginArboriculture;

public class CommandSpawnTree extends CommandMC {

	public CommandSpawnTree() {
	}

	public String getCommandName() {
		return "spawntree";
	}

	public String getCommandUsage(ICommandSender sender) {
		return "/" + getCommandName() + " <player-name> <species-name>";
	}

	public void processCommand(ICommandSender sender, String[] arguments) {

		EntityPlayer player = this.getPlayerFromName(sender.getCommandSenderName());

		int x = (int) player.posX;
		int y = (int) (player.posY);
		int z = (int) player.posZ;

		WorldGenerator gen = new WorldGenBalsa((ITreeGenData) PluginArboriculture.treeInterface.getTree(player.worldObj,
				TreeTemplates.templateAsGenome(PluginArboriculture.treeInterface.getTemplate("treeBalsa"))));

		gen.generate(player.worldObj, player.worldObj.rand, x, y, z);

	}
}
