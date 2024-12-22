package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.isHorse;

public class HorseRunningBackwardsAnimation {
    private HorseRunningBackwardsAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            var vehicle = context.player().getVehicle();
            if (isHorse(vehicle) && context.playerData().getMovementSpeed() > 0 && context.playerData().isMovingBackwards()) {
                if (!CONFIG.horseAnimationsConfig.horseRunningBackwardsAnimationConfig.isEnabled()) {
                    context.mainAnimationContainer().disableAnimation();
                } else {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.horseAnimationsConfig.horseRunningBackwardsAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.horseAnimationsConfig.horseRunningBackwardsAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.horseAnimationsConfig.horseRunningBackwardsAnimationConfig.getPriority());

                    context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.HORSE_IDLE_ANIMATION.getAnimationId()));
                    context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.HORSE_IDLE_ANIMATION.getAnimationId());
                }
            }

        }
    }
}
