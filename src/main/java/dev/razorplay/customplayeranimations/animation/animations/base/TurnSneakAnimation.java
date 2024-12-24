package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.animation.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static java.lang.Math.abs;

public class TurnSneakAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (shouldPlayAnimation(context)) {
            if (!CONFIG.idleAnimations.turnAnimations.turningSneakAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                if ((((float) 1 / 2) * context.playerData().getBodyYawDelta()) > 1.5 || (((float) 1 / 2) * context.playerData().getBodyYawDelta()) < -1.5) {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.idleAnimations.turnAnimations.turningSneakAnimationConfig.getSpeedMultiplier());
                } else {
                    context.mainAnimationContainer().setAnimationSpeed(abs((((float) 1 / 2) * context.playerData().getBodyYawDelta()) * CONFIG.idleAnimations.turnAnimations.turningSneakAnimationConfig.getSpeedMultiplier()));
                }
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.idleAnimations.turnAnimations.turningSneakAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.idleAnimations.turnAnimations.turningSneakAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.WALKING_SNEAK_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.WALKING_SNEAK_ANIMATION.getAnimationId());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isCrouching() && context.playerData().getMovementSpeed() == 0 && !context.playerData().isMovingBackwards() && context.playerData().getBodyYawDelta() != 0;
    }
}
