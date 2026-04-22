package com.github.razorplay01.cpa.platform.common.animation.animations.compat;
//? if >= 1.20 && < 26 {

/*import com.github.razorplay01.cpa.ModTemplate;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.world.entity.player.Player;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.CarryOnDataManager;

import static com.github.razorplay01.cpa.platform.common.util.Util.disableBothArms;

public class CarryOnCompatAnimation implements ICustomAnimation {
	@Override
	public void playAnimation(AnimationContext context) {
		disableBothArms(context);
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		if (!ModTemplate.xplat().isModLoaded("carryon")) return false;
		CarryOnData carry = CarryOnDataManager.getCarryData((Player) context.player());
		return carry.isCarrying() && !context.player().isSwimming() && !context.player().isFallFlying();
	}

}
*///?}
