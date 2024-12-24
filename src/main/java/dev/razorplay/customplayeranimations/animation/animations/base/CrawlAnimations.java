package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.animation.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;

public class CrawlAnimations implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (shouldPlayAnimation(context)) {
            if (!CONFIG.extraAnimations.crawlingAnimationsConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                configureAnimationContainer(CONFIG.extraAnimations.crawlingAnimationsConfig, context.mainAnimationContainer());

                if (context.playerData().getMovementSpeed() > 0.0649) {
                    context.mainAnimationContainer().setAnimationSpeed(context.mainAnimationContainer().getAnimationSpeed() + (float) context.playerData().getMovementSpeed());
                }
                if (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards()) {
                    context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.CRAWLING_ANIMATION.getAnimationId()));
                    context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.CRAWLING_ANIMATION.getAnimationId());
                } else if (context.playerData().getMovementSpeed() > 0) {
                    context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.CRAWLING_BACKWARDS_ANIMATION.getAnimationId()));
                    context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.CRAWLING_BACKWARDS_ANIMATION.getAnimationId());
                } else {
                    context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.CRAWLING_IDLE_ANIMATION.getAnimationId()));
                    context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.CRAWLING_IDLE_ANIMATION.getAnimationId());
                }
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isVisuallyCrawling() && !context.player().isPassenger();
    }
}
