package dev.razorplay.customplayeranimations.util;

import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.config.ClientConfig;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.vehicle.Boat;

public class Util {
    public static final String RIGHT_PREFIX = "right_";
    public static final String LEFT_PREFIX = "left_";

    private Util() {
        //[]
    }

    public static boolean isBoat(Object vehicle) {
        return vehicle instanceof Boat;
    }

    public static boolean isHorse(Object vehicle) {
        return vehicle instanceof Horse || vehicle instanceof SkeletonHorse || vehicle instanceof ZombieHorse || vehicle instanceof Donkey || vehicle instanceof Mule;
    }

    public static void configureAnimationContainer(ClientConfig.AnimationConfig config, AnimationContainer animationContainer) {
        animationContainer.setAnimationSpeed(config.getSpeedMultiplier());
        animationContainer.setAnimationFadeTime(config.getFadeTime());
        animationContainer.setAnimationPriority(config.getPriority());
    }
}
