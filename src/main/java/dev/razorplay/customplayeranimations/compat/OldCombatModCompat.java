package dev.razorplay.customplayeranimations.compat;

import com.spiderfrog.oldcombatmod.client.OldCombatModClient;
import net.minecraft.world.entity.LivingEntity;

public class OldCombatModCompat {
    public static boolean check(LivingEntity player) {
        return OldCombatModClient.isPlayerSwordblocking(player);
    }
}
