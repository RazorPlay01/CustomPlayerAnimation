package dev.razorplay.customplayeranimations.util;

import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.config.ClientConfig;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.Arrays;
import java.util.List;

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

    public static boolean containsAnyAnimation(AnimationContainer container, List<String> animations) {
        String currentAnimation = container.getCurrentAnimationId();
        if (animations == null || animations.isEmpty()) {
            return false;
        }
        return animations.stream().anyMatch(currentAnimation::contains);
    }
}
