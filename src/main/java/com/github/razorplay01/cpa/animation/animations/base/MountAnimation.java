package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.cpa.util.Util.*;

public class MountAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        configureAnimationContainer(CONFIG.getMainAnimations().mountAnimations.minecartAnimationsConfig, context.mainAnimationContainer());

        context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.MINECART_IDLE_ANIMATION.getAnimationId()));
        context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.MINECART_IDLE_ANIMATION.getAnimationId());
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        return context.player().isPassenger() && !isHorse(vehicle) && !isBoat(vehicle);
    }
}
