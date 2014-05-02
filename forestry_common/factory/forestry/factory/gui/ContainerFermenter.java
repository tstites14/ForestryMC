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
import forestry.core.gui.slots.SlotCustom;
import forestry.core.gui.slots.SlotLiquidContainer;
import forestry.core.gui.slots.SlotOutput;
import forestry.factory.gadgets.MachineFermenter;

public class ContainerFermenter extends ContainerLiquidTanks {
	protected MachineFermenter fermenter;

	public ContainerFermenter(InventoryPlayer player, MachineFermenter fermenter) {
		super(fermenter, fermenter);

		this.fermenter = fermenter;
		this.addSlot(new SlotCustom(fermenter, MachineFermenter.SLOT_RESOURCE, 85, 23, true));
		this.addSlot(new SlotCustom(fermenter, MachineFermenter.SLOT_FUEL, 75, 57, true));
		this.addSlot(new SlotOutput(fermenter, MachineFermenter.SLOT_CAN_OUTPUT, 150, 58));
		this.addSlot(new SlotLiquidContainer(fermenter, MachineFermenter.SLOT_CAN_INPUT, 150, 22, true));
		this.addSlot(new SlotLiquidContainer(fermenter, MachineFermenter.SLOT_INPUT, 10, 40));

		for (int i = 0; i < 3; ++i)
			for (int var4 = 0; var4 < 9; ++var4)
				this.addSlot(new Slot(player, var4 + i * 9 + 9, 8 + var4 * 18, 84 + i * 18));

		for (int i = 0; i < 9; ++i)
			this.addSlot(new Slot(player, i, 8 + i * 18, 142));

	}

	@Override
	public void updateProgressBar(int i, int j) {
		fermenter.getGUINetworkData(i, j);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); i++)
			fermenter.sendGUINetworkData(this, (ICrafting) crafters.get(i));
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return fermenter.isUseableByPlayer(entityplayer);
	}
}
