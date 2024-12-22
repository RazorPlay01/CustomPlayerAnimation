package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.isHorse;

public class HorseIdleAnimation {
    private HorseIdleAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            var vehicle = context.player().getVehicle();
            if (isHorse(vehicle) && context.playerData().getMovementSpeed() == 0) {
                if (!CONFIG.horseAnimationsConfig.horseIdleAnimationConfig.isEnabled()) {
                    context.mainAnimationContainer().disableAnimation();
                } else {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.horseAnimationsConfig.horseIdleAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.horseAnimationsConfig.horseIdleAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.horseAnimationsConfig.horseIdleAnimationConfig.getPriority());

                    context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.HORSE_IDLE_ANIMATION.getAnimationId()));
                    context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.HORSE_IDLE_ANIMATION.getAnimationId());
                }
            }
        }
    }
}
