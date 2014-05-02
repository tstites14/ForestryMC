/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.factory.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

import forestry.core.gui.ContainerLiquidTanks;
import forestry.core.gui.slots.SlotLiquidContainer;
import forestry.factory.gadgets.MachineStill;

public class ContainerStill extends ContainerLiquidTanks {
	protected MachineStill processor;

	public ContainerStill(InventoryPlayer player, MachineStill tile) {
		super(tile, tile);

		this.processor = tile;
		this.addSlot(new Slot(tile, MachineStill.SLOT_PRODUCT, 150, 54));
		this.addSlot(new SlotLiquidContainer(tile, MachineStill.SLOT_RESOURCE, 150, 18, true));
		this.addSlot(new SlotLiquidContainer(tile, MachineStill.SLOT_CAN, 10, 36));

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlot(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for (int i = 0; i < 9; ++i)
			this.addSlot(new Slot(player, i, 8 + i * 18, 142));

	}

	// @Override client side only
	public void updateProgressBar(int i, int j) {
		processor.getGUINetworkData(i, j);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); i++)
			processor.sendGUINetworkData(this, (ICrafting) crafters.get(i));
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return processor.isUseableByPlayer(entityplayer);
	}
}
