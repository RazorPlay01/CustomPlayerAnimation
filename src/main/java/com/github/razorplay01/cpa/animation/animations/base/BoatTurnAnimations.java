package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.cpa.util.Util.configureAnimationContainer;
import static com.github.razorplay01.cpa.util.Util.isBoat;

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
            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.BOAT_TURN_LEFT_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.BOAT_TURN_LEFT_ANIMATION.getAnimationId());
        } else if (isRightPaddleMoving) {
            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.BOAT_TURN_RIGHT_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.BOAT_TURN_RIGHT_ANIMATION.getAnimationId());
        }
    }
}