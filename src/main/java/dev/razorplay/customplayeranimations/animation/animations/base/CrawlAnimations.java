package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.*;

public class CrawlAnimations {
    private CrawlAnimations() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isVisuallyCrawling()) {
            if (!CONFIG.crawlingAnimationsConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.crawlingAnimationsConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.crawlingAnimationsConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.crawlingAnimationsConfig.getPriority());

                if (context.playerData().getMovementSpeed() > 0.0649) {
                    context.mainAnimationContainer().setAnimationSpeed(context.mainAnimationContainer().getAnimationSpeed() + (float) context.playerData().getMovementSpeed());
                }
                if (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards()) {
                    context.mainAnimationContainer().setCurrentAnimation(CRAWLING_ANIMATION.animation());
                    context.mainAnimationContainer().setCurrentAnimationId(CRAWLING_ANIMATION.animationId());
                } else if (context.playerData().getMovementSpeed() > 0) {
                    context.mainAnimationContainer().setCurrentAnimation(CRAWLING_BACKWARDS_ANIMATION.animation());
                    context.mainAnimationContainer().setCurrentAnimationId(CRAWLING_BACKWARDS_ANIMATION.animationId());
                } else {
                    context.mainAnimationContainer().setCurrentAnimation(CRAWLING_IDLE_ANIMATION.animation());
                    context.mainAnimationContainer().setCurrentAnimationId(CRAWLING_IDLE_ANIMATION.animationId());
                }
            }
        }
    }
}
