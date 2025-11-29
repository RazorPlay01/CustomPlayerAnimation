package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;

public class TurnSneakAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().idleAnimations.turnAnimations.turningSneakAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            if ((((float) 1 / 2) * context.playerData().getBodyYawDelta()) > 1.5 || (((float) 1 / 2) * context.playerData().getBodyYawDelta()) < -1.5) {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.getMainAnimations().idleAnimations.turnAnimations.turningSneakAnimationConfig.getSpeedMultiplier());
            } else {
                context.mainAnimationContainer().setAnimationSpeed(Math.abs((((float) 1 / 2) * context.playerData().getBodyYawDelta()) * CONFIG.getMainAnimations().idleAnimations.turnAnimations.turningSneakAnimationConfig.getSpeedMultiplier()));
            }
            context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().idleAnimations.turnAnimations.turningSneakAnimationConfig.getFadeTime());
            context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().idleAnimations.turnAnimations.turningSneakAnimationConfig.getPriority());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.WALK_SNEAK_ANIMATION);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isCrouching() && context.playerData().getMovementSpeed() == 0 && !context.playerData().isMovingBackwards() && context.playerData().getBodyYawDelta() != 0;
    }
}
