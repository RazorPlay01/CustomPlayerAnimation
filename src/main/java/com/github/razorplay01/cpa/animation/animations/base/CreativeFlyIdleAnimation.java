package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.cpa.util.Util.configureAnimationContainer;

public class CreativeFlyIdleAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        playFlyIdleCreativeAnimation(context);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getFlychecker() > 10 &&
                !context.player().isPassenger() &&
                context.player().isCreative();
    }

    private static void playFlyIdleCreativeAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().idleAnimations.idleCreativeFlyingAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.getMainAnimations().idleAnimations.idleCreativeFlyingAnimationConfig, context.mainAnimationContainer());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.IDLE_CREATIVE_FLY_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.IDLE_CREATIVE_FLY_ANIMATION.getAnimationId());
        }
    }
}
