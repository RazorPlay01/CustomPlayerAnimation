package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.IDLE_CREATIVE_FLYING_ANIMATION;

public class FlyAnimation {
    private FlyAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        double flyVectorY = Math.round(context.playerData().getVectorY() * 1000.0) / 1000.0;
        if ((flyVectorY == 0.0 || Math.abs(flyVectorY) == 0.375) && !context.player().onGround() && !context.player().isInWaterOrBubble()) {
            context.playerData().setFlychecker(context.playerData().getFlychecker() + 1);
        } else if (Math.abs(flyVectorY) > 0.375 || context.player().onGround()) {
            context.playerData().setFlychecker(0);
        }

        if (context.playerData().getFlychecker() > 10) {
            playFlyIdleCreativeAnimation(context);
        }
    }

    private static void playFlyIdleCreativeAnimation(AnimationContext context) {
        if (!CONFIG.idleCreativeFlyingAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            context.mainAnimationContainer().setCurrentAnimation(IDLE_CREATIVE_FLYING_ANIMATION.animation());
            context.mainAnimationContainer().setCurrentAnimationId(IDLE_CREATIVE_FLYING_ANIMATION.animationId());

            context.mainAnimationContainer().setAnimationSpeed(CONFIG.idleCreativeFlyingAnimationConfig.getSpeedMultiplier());
            context.mainAnimationContainer().setAnimationFadeTime(CONFIG.idleCreativeFlyingAnimationConfig.getFadeTime());
            context.mainAnimationContainer().setAnimationPriority(CONFIG.idleCreativeFlyingAnimationConfig.getPriority());
        }
    }
}
