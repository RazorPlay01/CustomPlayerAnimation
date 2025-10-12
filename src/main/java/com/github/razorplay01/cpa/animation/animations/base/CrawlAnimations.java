package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.cpa.util.Util.configureAnimationContainer;

public class CrawlAnimations implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().extraAnimations.crawlingAnimationsConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.getMainAnimations().extraAnimations.crawlingAnimationsConfig, context.mainAnimationContainer());

            if (context.playerData().getMovementSpeed() > 0.0649) {
                context.mainAnimationContainer().setAnimationSpeed(context.mainAnimationContainer().getAnimationSpeed() + (float) context.playerData().getMovementSpeed());
            }
            if (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards()) {
                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.CRAWL_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.CRAWL_ANIMATION.getAnimationId());
            } else if (context.playerData().getMovementSpeed() > 0) {
                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.CRAWL_BACKWARDS_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.CRAWL_BACKWARDS_ANIMATION.getAnimationId());
            } else {
                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.CRAWL_IDLE_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.CRAWL_IDLE_ANIMATION.getAnimationId());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isVisuallyCrawling() && !context.player().isPassenger();
    }
}
