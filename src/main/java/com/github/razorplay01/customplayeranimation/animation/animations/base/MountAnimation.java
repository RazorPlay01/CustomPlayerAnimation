package com.github.razorplay01.customplayeranimation.animation.animations.base;

import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.*;

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
