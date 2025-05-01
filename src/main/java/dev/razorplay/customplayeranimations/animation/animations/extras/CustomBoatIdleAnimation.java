package dev.razorplay.customplayeranimations.animation.animations.extras;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;
import static dev.razorplay.customplayeranimations.util.Util.isBoat;

public class CustomBoatIdleAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        if (isBoat(vehicle)) {
            if (!CONFIG.mountAnimations.boatAnimations.boatIdleAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
                return;
            }
            configureAnimationContainer(CONFIG.mountAnimations.boatAnimations.boatIdleAnimationConfig, context.mainAnimationContainer());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation("custom_boat_idle_animation"));
            context.mainAnimationContainer().setCurrentAnimationId("custom_boat_idle_animation");
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        if (!isBoat(vehicle)) {
            return false;
        }
        Boat boat = (Boat) vehicle;
        Boat.Type boatType = boat.getVariant();
        return context.player().isPassenger() &&
                context.playerData().getMovementSpeed() == 0 || context.playerData().isMovingBackwards() &&
                boatType == Boat.Type.OAK;
    }
}