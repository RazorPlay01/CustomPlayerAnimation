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
        var vehicle = context.player().getVehicle();
        if (isBoat(vehicle)) {
            if (!CONFIG.getMainAnimations().mountAnimations.boatAnimations.boatTurnAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
                return;
            }

            configureAnimationContainer(CONFIG.getMainAnimations().mountAnimations.boatAnimations.boatTurnAnimationConfig, context.mainAnimationContainer());

            playBoatTurnAnimation(context, (Boat) vehicle);
        }
    }

    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isPassenger() && context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards();
    }

    private static void playBoatTurnAnimation(AnimationContext context, Boat boat) {
        boolean isLeftPaddleMoving = boat.getPaddleState(0);
        boolean isRightPaddleMoving = boat.getPaddleState(1);

        if (isLeftPaddleMoving) {
			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.BOAT_TURN_LEFT_ANIMATION);
        } else if (isRightPaddleMoving) {
			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.BOAT_TURN_RIGHT_ANIMATION);
        }
    }
}
