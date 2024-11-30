package dev.razorplay.customplayeranimations.compat;

import net.minecraft.client.player.AbstractClientPlayer;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.CarryOnDataManager;

public class CarryOnCompat {
    public static void check(AbstractClientPlayer player) {
        CarryOnData carry = CarryOnDataManager.getCarryData(player);
        if (carry.isCarrying() && !player.isSwimming() && !player.isFallFlying()) {
            player.disableArmsAnimation(true);
        }
    }
}
