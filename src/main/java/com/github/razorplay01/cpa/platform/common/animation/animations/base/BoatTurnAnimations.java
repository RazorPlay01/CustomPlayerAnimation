package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
//? if <= 1.21.10 {
import net.minecraft.world.entity.vehicle.Boat;
 //?}else{
/*import net.minecraft.world.entity.vehicle.boat.Boat;
*///?}

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;
import static com.github.razorplay01.cpa.platform.common.util.Util.isBoat;

public class BoatTurnAnimations implements ICustomAnimation {
	@Override
	public void playAnimation(AnimationContext context) {
		if (!CONFIG.getMainAnimations().mountAnimations.boatAnimations.boatTurnAnimationConfig.isEnabled()) {
			context.mainAnimationContainer().disableAnimation();
			return;
		}
		configureAnimationContainer(CONFIG.getMainAnimations().mountAnimations.boatAnimations.boatTurnAnimationConfig, context.mainAnimationContainer());
		playBoatTurnAnimation(context);
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
		// Solo si EXACTAMENTE UNA pala (turn)
		return context.playerData().getMovementSpeed() > 0
				&& !context.playerData().isMovingBackwards()
				&& (isLeftPaddleMoving != isRightPaddleMoving);
	}

	private static void playBoatTurnAnimation(AnimationContext context) {
		if (!(context.player().getVehicle() instanceof Boat boat)) return;

		boolean isLeftPaddleMoving = boat.getPaddleState(0);
		boolean isRightPaddleMoving = boat.getPaddleState(1);

		if (isLeftPaddleMoving && !isRightPaddleMoving) {
			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.BOAT_TURN_LEFT_ANIMATION);
		} else if (!isLeftPaddleMoving && isRightPaddleMoving) {
			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.BOAT_TURN_RIGHT_ANIMATION);
		} else {
			// SAFETY: Si llega aquí (desync raro), deshabilita para no dejar bucle
			context.mainAnimationContainer().disableAnimation();
		}
	}
}
