package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.util.Util.configureAnimationContainer;
import static com.github.razorplay01.cpa.util.Util.isBoat;
import static com.github.razorplay01.cpa.util.Util.isHorse;

public class MountAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        configureAnimationContainer(CONFIG.getMainAnimations().mountAnimations.minecartAnimationsConfig, context.mainAnimationContainer());

		AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.MINECART_IDLE_ANIMATION);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        return context.player().isPassenger() && !isHorse(vehicle) && !isBoat(vehicle);
    }
}
