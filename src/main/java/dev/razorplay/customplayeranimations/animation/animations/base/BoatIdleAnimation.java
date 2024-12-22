package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;
import static dev.razorplay.customplayeranimations.util.Util.isBoat;

public class BoatIdleAnimation {
    private BoatIdleAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (!context.player().isPassenger()) {
            return;
        }
        var vehicle = context.player().getVehicle();
        if (isBoat(vehicle)) {
            handleBoatAnimation(context);
        }
    }

    private static void handleBoatAnimation(AnimationContext context) {
        if (!CONFIG.boatAnimations.boatIdleAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            return;
        }
        configureAnimationContainer(CONFIG.boatAnimations.boatIdleAnimationConfig, context.mainAnimationContainer());

        if (shouldPlayAnimation(context)) {
            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.BOAT_IDLE_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.BOAT_IDLE_ANIMATION.getAnimationId());
        }
    }


    private static boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getMovementSpeed() == 0 || context.playerData().isMovingBackwards();
    }
}