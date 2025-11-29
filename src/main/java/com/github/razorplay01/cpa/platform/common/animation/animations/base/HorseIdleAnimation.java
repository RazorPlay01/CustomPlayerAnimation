package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;
import static com.github.razorplay01.cpa.platform.common.util.Util.isHorse;

public class HorseIdleAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().mountAnimations.horseAnimations.horseIdleAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.getMainAnimations().mountAnimations.horseAnimations.horseIdleAnimationConfig, context.mainAnimationContainer());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.HORSE_IDLE_ANIMATION);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        return context.player().isPassenger() && isHorse(vehicle) && context.playerData().getMovementSpeed() == 0;
    }
}
