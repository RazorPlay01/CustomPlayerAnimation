package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.animation.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Minecart;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;

public class MinecartAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (shouldPlayAnimation(context)) {
            if (!CONFIG.mountAnimations.minecartAnimationsConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                configureAnimationContainer(CONFIG.mountAnimations.minecartAnimationsConfig, context.mainAnimationContainer());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.MINECART_IDLE_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.MINECART_IDLE_ANIMATION.getAnimationId());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        return context.player().isPassenger() && vehicle instanceof Minecart;
    }
}
