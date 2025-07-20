package com.github.razorplay01.customplayeranimation.animation.animations.base;

import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.configureAnimationContainer;
import static com.github.razorplay01.customplayeranimation.util.Util.isBoat;

public class BoatTurnAnimations implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        if (isBoat(vehicle)) {
            if (!CONFIG.mountAnimations.boatAnimations.boatTurnAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
                return;
            }

            configureAnimationContainer(CONFIG.mountAnimations.boatAnimations.boatTurnAnimationConfig, context.mainAnimationContainer());

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