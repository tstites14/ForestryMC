/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.factory.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

import forestry.core.gui.ContainerForestry;
import forestry.core.gui.slots.SlotClosed;
import forestry.factory.gadgets.MachineCentrifuge;

public class ContainerCentrifuge extends ContainerForestry {

	protected MachineCentrifuge tile;

	public ContainerCentrifuge(InventoryPlayer player, MachineCentrifuge tile) {
		super(tile);

		this.tile = tile;

		// Resource
		this.addSlot(new Slot(tile, 0, 34, 37));

		// Product Inventory
		for (int l = 0; l < 3; l++)
			for (int k = 0; k < 3; k++)
				addSlot(new SlotClosed(tile, 1 + k + l * 3, 98 + k * 18, 19 + l * 18));

		// Player inventory
		for (int i1 = 0; i1 < 3; i1++)
			for (int l1 = 0; l1 < 9; l1++)
				addSlot(new Slot(player, l1 + i1 * 9 + 9, 8 + l1 * 18, 84 + i1 * 18));
		// Player hotbar
		for (int j1 = 0; j1 < 9; j1++)
			addSlot(new Slot(player, j1, 8 + j1 * 18, 142));
	}

	@Override
	public void updateProgressBar(int i, int j) {
		tile.getGUINetworkData(i, j);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); i++)
			tile.sendGUINetworkData(this, (ICrafting) crafters.get(i));
	}

}
