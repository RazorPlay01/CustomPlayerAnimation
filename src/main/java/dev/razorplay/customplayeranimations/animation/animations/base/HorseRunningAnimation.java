package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;
import static dev.razorplay.customplayeranimations.util.Util.isHorse;

public class HorseRunningAnimation {
    private HorseRunningAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            var vehicle = context.player().getVehicle();
            if (isHorse(vehicle) && context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards()) {
                if (!CONFIG.horseAnimationsConfig.horseRunningAnimationConfig.isEnabled()) {
                    context.mainAnimationContainer().disableAnimation();
                } else {
                    configureAnimationContainer(CONFIG.horseAnimationsConfig.horseRunningAnimationConfig, context.mainAnimationContainer());

                    context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.HORSE_RUNNING_ANIMATION.getAnimationId()));
                    context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.HORSE_RUNNING_ANIMATION.getAnimationId());
                }
            }

        }
    }
}
