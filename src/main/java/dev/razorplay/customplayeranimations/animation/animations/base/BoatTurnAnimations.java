package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.animation.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;
import static dev.razorplay.customplayeranimations.util.Util.isBoat;

public class BoatTurnAnimations implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!context.player().isPassenger()) {
            return;
        }

        var vehicle = context.player().getVehicle();
        if (isBoat(vehicle)) {
            if (!CONFIG.mountAnimations.boatAnimations.boatTurnAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
                return;
            }

            configureAnimationContainer(CONFIG.mountAnimations.boatAnimations.boatTurnAnimationConfig, context.mainAnimationContainer());

            if (shouldPlayAnimation(context)) {
                playBoatTurnAnimation(context, (Boat) vehicle);
            }
        }
    }

    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards();
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