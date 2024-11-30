package dev.razorplay.customplayeranimations.compat;

import eu.midnightdust.swordblocking.SwordBlockingClient;
import net.minecraft.world.entity.LivingEntity;

public class SwordBlockingCompat {
    public static boolean check(LivingEntity player) {
        return SwordBlockingClient.isWeaponBlocking(player);
    }
}
