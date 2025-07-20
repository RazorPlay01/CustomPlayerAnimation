package com.github.razorplay01.customplayeranimation.animation.animations.base;

import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.configureAnimationContainer;

public class InWaterBackwardsAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.inWaterAnimationsConfig.inWaterBackwardsAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.inWaterAnimationsConfig.inWaterBackwardsAnimationConfig, context.mainAnimationContainer());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.IN_WATER_BACKWARDS_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.IN_WATER_BACKWARDS_ANIMATION.getAnimationId());
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return (context.player().isUnderWater() || context.player().isInLava()) && !context.player().onGround() && !context.player().isVisuallySwimming() && (context.playerData().getMovementSpeed() > 0 && context.playerData().isMovingBackwards());
    }
}
