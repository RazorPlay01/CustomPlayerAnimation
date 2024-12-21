package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.BOAT_IDLE_ANIMATION;

public class BoatIdleAnimation {
    private BoatIdleAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            var vehicle = context.player().getVehicle();
            if (vehicle instanceof Boat || vehicle instanceof ChestBoat) {
                if (!CONFIG.boatAnimations.boatIdleAnimationConfig.isEnabled()) {
                    context.mainAnimationContainer().disableAnimation();
                } else {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.boatAnimations.boatIdleAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.boatAnimations.boatIdleAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.boatAnimations.boatIdleAnimationConfig.getPriority());

                    if (context.playerData().getMovementSpeed() == 0 || context.playerData().isMovingBackwards()) {
                        context.mainAnimationContainer().setCurrentAnimation(BOAT_IDLE_ANIMATION.animation());
                        context.mainAnimationContainer().setCurrentAnimationId(BOAT_IDLE_ANIMATION.animationId());
                    }
                }
            }
        }
    }
}