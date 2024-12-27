package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;

public class CreativeFlyIdleAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        playFlyIdleCreativeAnimation(context);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getFlychecker() > 10 && !context.player().isPassenger() && context.player().isCreative();
    }

    private static void playFlyIdleCreativeAnimation(AnimationContext context) {
        if (!CONFIG.idleAnimations.idleCreativeFlyingAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.idleAnimations.idleCreativeFlyingAnimationConfig, context.mainAnimationContainer());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.IDLE_CREATIVE_FLY_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.IDLE_CREATIVE_FLY_ANIMATION.getAnimationId());
        }
    }
}
