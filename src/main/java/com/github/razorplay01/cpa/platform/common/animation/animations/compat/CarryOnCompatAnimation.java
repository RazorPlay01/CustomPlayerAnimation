package com.github.razorplay01.cpa.platform.common.animation.animations.compat;/*
package dev.razorplay.customplayeranimations.animation.animations.compat;

import dev.razorplay.customplayeranimations.CustomPlayerAnimations;
import dev.razorplay.customplayeranimations.util.FirstPersonConditionRegistry;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.CarryOnDataManager;

import static dev.razorplay.customplayeranimations.util.Util.disableBothArms;

public class CarryOnCompatAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!FirstPersonConditionRegistry.hasCondition(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, "carry_on"))) {
            FirstPersonConditionRegistry.register(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, "carry_on"), CarryOnCompatAnimation::check);
        }
        if (check(context.player())) {
            disableBothArms(context);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return FabricLoader.getInstance().isModLoaded("carryon");
    }

    public static boolean check(AbstractClientPlayer player) {
        CarryOnData carry = CarryOnDataManager.getCarryData(player);
        return carry.isCarrying() && !player.isSwimming() && !player.isFallFlying();
    }
}
*/
