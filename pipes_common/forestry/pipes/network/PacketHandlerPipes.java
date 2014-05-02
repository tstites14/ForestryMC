/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.pipes.network;

import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import buildcraft.transport.Pipe;
import buildcraft.transport.TileGenericPipe;
import cpw.mods.fml.common.network.Player;
import forestry.core.interfaces.IPacketHandler;
import forestry.core.network.PacketCoordinates;
import forestry.core.network.PacketIds;
import forestry.core.network.PacketNBT;
import forestry.core.network.PacketUpdate;
import forestry.core.proxy.Proxies;
import forestry.pipes.PipeItemsPropolis;
import forestry.pipes.PipeLogicPropolis;
import forestry.pipes.gui.ContainerPropolisPipe;

public class PacketHandlerPipes implements IPacketHandler {

	private Pipe getPipe(World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile == null)
			return null;

		if (!(tile instanceof TileGenericPipe))
			return null;

		return ((TileGenericPipe) tile).pipe;
	}

	@Override
	public void onPacketData(INetworkManager network, int packetID, DataInputStream data, Player player) {

		try {

			PacketUpdate packetU;

			switch (packetID) {

			// CLIENT
			case PacketIds.PROP_SEND_FILTER_SET:
				PacketNBT packetN = new PacketNBT();
				packetN.readData(data);
				onFilterSet(packetN);
				break;

			// SERVER
			case PacketIds.PROP_REQUEST_FILTER_SET:
				PacketCoordinates packetC = new PacketCoordinates();
				packetC.readData(data);
				onRequestFilterSet((EntityPlayer) player, packetC);
				break;
			case PacketIds.PROP_SEND_FILTER_CHANGE_TYPE:
				packetU = new PacketUpdate();
				packetU.readData(data);
				onTypeFilterChange((EntityPlayer) player, packetU);
				break;
			case PacketIds.PROP_SEND_FILTER_CHANGE_GENOME:
				packetU = new PacketUpdate();
				packetU.readData(data);
				onGenomeFilterChange((EntityPlayer) player, packetU);
				break;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void onFilterSet(PacketNBT packet) {
		Container container = Proxies.common.getClientInstance().thePlayer.openContainer;

		if (container instanceof ContainerPropolisPipe) {
			PipeLogicPropolis pipeLogic = ((ContainerPropolisPipe) container).pipeLogic;
			pipeLogic.handleFilterSet(packet);
		}
	}

	private void onTypeFilterChange(EntityPlayer player, PacketUpdate packet) {

		Pipe pipe = getPipe(player.worldObj, packet.posX, packet.posY, packet.posZ);
		if (pipe == null)
			return;

		if (pipe instanceof PipeItemsPropolis)
			((PipeItemsPropolis) pipe).pipeLogic.handleTypeFilterChange(packet.payload);

	}

	private void onGenomeFilterChange(EntityPlayer player, PacketUpdate packet) {

		Pipe pipe = getPipe(player.worldObj, packet.posX, packet.posY, packet.posZ);
		if (pipe == null)
			return;

		if (pipe instanceof PipeItemsPropolis)
			((PipeItemsPropolis) pipe).pipeLogic.handleGenomeFilterChange(packet.payload);
	}

	private void onRequestFilterSet(EntityPlayer player, PacketCoordinates packet) {

		Pipe pipe = getPipe(player.worldObj, packet.posX, packet.posY, packet.posZ);
		if (pipe == null)
			return;

		if (pipe instanceof PipeItemsPropolis)
			((PipeItemsPropolis) pipe).pipeLogic.sendFilterSet(player);
	}

}
