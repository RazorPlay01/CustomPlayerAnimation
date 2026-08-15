package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
//? if <= 1.21.10 {
/*import net.minecraft.world.entity.vehicle.Boat;
*///?}
//? if >= 1.21.11 {
import net.minecraft.world.entity.vehicle.boat.Boat;
//?}

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.util.Util.configureAnimationContainer;
import static com.github.razorplay01.cpa.util.Util.isBoat;

public class BoatIdleAnimation implements ICustomAnimation {
	@Override
	public void playAnimation(AnimationContext context) {
		if (!CONFIG.getMainAnimations().mountAnimations.boatAnimations.boatIdleAnimationConfig.isEnabled()) {
			context.mainAnimationContainer().disableAnimation();
			return;
		}
		configureAnimationContainer(CONFIG.getMainAnimations().mountAnimations.boatAnimations.boatIdleAnimationConfig, context.mainAnimationContainer());
		AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.BOAT_IDLE_ANIMATION);
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		var vehicle = context.player().getVehicle();
		if (!(context.player().isPassenger() && isBoat(vehicle))) {
			return false;
		}
		Boat boat = (Boat) vehicle;
		boolean isLeftPaddleMoving = boat.getPaddleState(0);
		boolean isRightPaddleMoving = boat.getPaddleState(1);
		double speed = context.playerData().getMovementSpeed();
		return (speed < 0.01 || context.playerData().isMovingBackwards() || (!isLeftPaddleMoving && !isRightPaddleMoving));
	}
}
