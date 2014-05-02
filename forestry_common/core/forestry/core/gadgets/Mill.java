/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.core.gadgets;

import forestry.core.network.EntityNetData;

public abstract class Mill extends TilePowered {

	@EntityNetData
	public int charge = 0;
	@EntityNetData
	public float speed;
	@EntityNetData
	public int stage = 0;
	public float progress;

	public Mill() {
		speed = 0.01F;
	}

	@Override
	public void updateClientSide() {
		update(false);
	}

	@Override
	public void updateServerSide() {
		update(true);
	}

	private void update(boolean isSimulating) {

		// Stop gracefully if discharged.
		if (charge <= 0) {
			if (stage > 0)
				progress += speed;
			if (progress > 0.5)
				stage = 2;
			if (progress > 1) {
				progress = 0;
				stage = 0;
			}
			return;
		}

		// Update blades
		progress += speed;
		if (stage <= 0)
			stage = 1;

		if (progress > 0.5 && stage == 1) {
			stage = 2;
			if (charge < 7 && isSimulating) {
				charge++;
				sendNetworkUpdate();
			}
		}
		if (progress > 1) {
			progress = 0;
			stage = 0;

			// Fully charged! Do something!
			if (charge >= 7)
				activate();
		}

	}

	protected abstract void activate();

	@Override
	public boolean isWorking() {
		return charge != 0;
	}

}
