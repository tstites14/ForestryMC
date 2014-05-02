/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.mail;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import forestry.core.GuiHandlerBase;
import forestry.core.network.GuiId;
import forestry.mail.gadgets.MachineMailbox;
import forestry.mail.gadgets.MachinePhilatelist;
import forestry.mail.gadgets.MachineTrader;
import forestry.mail.gui.ContainerCatalogue;
import forestry.mail.gui.ContainerLetter;
import forestry.mail.gui.ContainerMailbox;
import forestry.mail.gui.ContainerPhilatelist;
import forestry.mail.gui.ContainerTradeName;
import forestry.mail.gui.ContainerTrader;
import forestry.mail.gui.GuiCatalogue;
import forestry.mail.gui.GuiLetter;
import forestry.mail.gui.GuiMailbox;
import forestry.mail.gui.GuiPhilatelist;
import forestry.mail.gui.GuiTradeName;
import forestry.mail.gui.GuiTrader;
import forestry.mail.items.ItemCatalogue;
import forestry.mail.items.ItemLetter;
import forestry.mail.items.ItemLetter.LetterInventory;

public class GuiHandlerMail extends GuiHandlerBase {

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		if (id >= GuiId.values().length)
			return null;

		switch (GuiId.values()[id]) {
		case CatalogueGUI:
			ItemStack cata = getEquippedItem(player);
			if (cata == null)
				return null;

			if (cata.getItem() instanceof ItemCatalogue)
				return new GuiCatalogue(player);
			else
				return null;
			
		case LetterGUI:
			ItemStack equipped = getEquippedItem(player);
			if (equipped == null)
				return null;

			if (equipped.getItem() instanceof ItemLetter)
				return new GuiLetter(player, new LetterInventory(equipped));
			else
				return null;

		case MailboxGUI:
			return new GuiMailbox(player.inventory, (MachineMailbox) getTileForestry(world, x, y, z));
		case PhilatelistGUI:
			return new GuiPhilatelist(player.inventory, (MachinePhilatelist) getTileForestry(world, x, y, z));
		case TraderGUI:
			return new GuiTrader(player.inventory, (MachineTrader) getTileForestry(world, x, y, z));
		case TraderNameGUI:
			return new GuiTradeName(player.inventory, (MachineTrader) getTileForestry(world, x, y, z));
		default:
			return null;

		}
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		if (id >= GuiId.values().length)
			return null;

		switch (GuiId.values()[id]) {
		case CatalogueGUI:
			ItemStack cata = getEquippedItem(player);
			if (cata == null)
				return null;

			if (cata.getItem() instanceof ItemCatalogue)
				return new ContainerCatalogue(player);
			else
				return null;
			
		case LetterGUI:
			ItemStack equipped = getEquippedItem(player);
			if (equipped == null)
				return null;

			if (equipped.getItem() instanceof ItemLetter)
				return new ContainerLetter(player, new LetterInventory(getEquippedItem(player)));
			else
				return null;

		case MailboxGUI:
			return new ContainerMailbox(player.inventory, (MachineMailbox) getTileForestry(world, x, y, z));
		case PhilatelistGUI:
			return new ContainerPhilatelist(player.inventory, (MachinePhilatelist) getTileForestry(world, x, y, z));
		case TraderGUI:
			return new ContainerTrader(player.inventory, (MachineTrader) getTileForestry(world, x, y, z));
		case TraderNameGUI:
			return new ContainerTradeName(player.inventory, (MachineTrader) getTileForestry(world, x, y, z));
		default:
			return null;

		}
	}

}
