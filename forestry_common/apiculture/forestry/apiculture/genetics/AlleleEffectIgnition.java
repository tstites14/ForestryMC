/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.apiculture.genetics;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IEffectData;
import forestry.apiculture.items.ItemArmorApiarist;
import forestry.plugins.PluginApiculture;

public class AlleleEffectIgnition extends AlleleEffectThrottled {

	int ignitionChance = 50;
	int fireDuration = 500;

	public AlleleEffectIgnition(String uid) {
		super(uid, "ignition", false, 20, false, true);
	}

	@Override
	public IEffectData doEffect(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {

		World world = housing.getWorld();

		if (isHalted(storedData, housing))
			return storedData;

		AxisAlignedBB hurtBox = getBounding(genome, housing, 1.0f);
		@SuppressWarnings("rawtypes")
		List list = world.getEntitiesWithinAABB(EntityLivingBase.class, hurtBox);

		for (Object obj : list) {
			EntityLivingBase entity = (EntityLivingBase) obj;

			int chance = ignitionChance;
			int duration = fireDuration;

			// Players are not attacked if they wear a full set of apiarist's
			// armor.
			if (entity instanceof EntityPlayer) {
				int count = ItemArmorApiarist.wearsItems((EntityPlayer) entity, getUID(), true);
				// Full set, no damage/effect
				if (count > 3)
					continue;
				else if (count > 2) {
					chance = 5;
					duration = 50;
				} else if (count > 1) {
					chance = 20;
					duration = 200;
				} else if (count > 0) {
					chance = 35;
					duration = 350;
				}
			}

			if (world.rand.nextInt(1000) >= chance)
				continue;

			entity.setFire(duration);
		}

		return storedData;
	}

	@Override
	public IEffectData doFX(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {

		int[] area = getModifiedArea(genome, housing);

		if (housing.getWorld().rand.nextBoolean())
			PluginApiculture.proxy.addBeeHiveFX("particles/swarm_bee", housing.getWorld(), housing.getXCoord(), housing.getYCoord(),
					housing.getZCoord(), genome.getPrimary().getIconColour(0), area[0], area[1], area[2]);
		else
			PluginApiculture.proxy.addBeeHiveFX("particles/ember", housing.getWorld(), housing.getXCoord(), housing.getYCoord(),
					housing.getZCoord(), 0xffffff, area[0], area[1], area[2]);
		return storedData;
	}

}
