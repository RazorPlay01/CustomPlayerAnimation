package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;

public class IdleSneakAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().idleAnimations.idleSneakAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.getMainAnimations().idleAnimations.idleSneakAnimationConfig, context.mainAnimationContainer());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.IDLE_SNEAK_ANIMATION);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isCrouching() && context.playerData().getMovementSpeed() == 0 && !context.playerData().isMovingBackwards() && context.playerData().getBodyYawDelta() == 0 && !context.player().isPassenger();
    }
}
