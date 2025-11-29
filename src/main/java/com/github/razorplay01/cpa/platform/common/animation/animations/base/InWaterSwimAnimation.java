package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;

public class InWaterSwimAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().inWaterAnimations.inWaterSwimAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.getMainAnimations().inWaterAnimations.inWaterSwimAnimationConfig, context.mainAnimationContainer());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.IN_WATER_SWIM_ANIMATION);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isUnderWater() && context.player().isVisuallySwimming();
    }
}
