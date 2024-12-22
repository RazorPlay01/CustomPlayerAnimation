package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

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
}
