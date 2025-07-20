package com.github.razorplay01.customplayeranimation.animation.animations.base;

import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static java.lang.Math.abs;

public class TurnSneakAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
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

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.WALK_SNEAK_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.WALK_SNEAK_ANIMATION.getAnimationId());
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isCrouching() && context.playerData().getMovementSpeed() == 0 && !context.playerData().isMovingBackwards() && context.playerData().getBodyYawDelta() != 0;
    }
}
