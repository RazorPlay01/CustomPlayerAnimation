package dev.razorplay.customplayeranimations.animation.animations.compat;

import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.player.AbstractClientPlayer;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.CarryOnDataManager;

import static dev.razorplay.customplayeranimations.util.Util.disableBothArms;

public class CarryOnCompatAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
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
