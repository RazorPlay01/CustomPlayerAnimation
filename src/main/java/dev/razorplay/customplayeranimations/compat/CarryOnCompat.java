package dev.razorplay.customplayeranimations.compat;

import net.minecraft.client.player.AbstractClientPlayer;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.CarryOnDataManager;

public class CarryOnCompat {
    private CarryOnCompat() {
        // []
    }

    public static boolean check(AbstractClientPlayer player) {
        CarryOnData carry = CarryOnDataManager.getCarryData(player);
        return carry.isCarrying() && !player.isSwimming() && !player.isFallFlying();
    }
}
