package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;
import static dev.razorplay.customplayeranimations.util.Util.isBoat;

public class BoatTurnAnimations {
    private BoatTurnAnimations() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (!context.player().isPassenger()) {
            return;
        }

        var vehicle = context.player().getVehicle();
        if (isBoat(vehicle)) {
            handleBoatAnimation(context, (Boat) vehicle);
        }
    }

    private static void handleBoatAnimation(AnimationContext context, Boat boat) {
        if (!CONFIG.boatAnimations.boatTurnAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            return;
        }

        configureAnimationContainer(CONFIG.boatAnimations.boatTurnAnimationConfig, context.mainAnimationContainer());

        if (shouldPlayAnimation(context)) {
            playBoatTurnAnimation(context, boat);
        }
    }

    private static boolean shouldPlayAnimation(AnimationContext context) {
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