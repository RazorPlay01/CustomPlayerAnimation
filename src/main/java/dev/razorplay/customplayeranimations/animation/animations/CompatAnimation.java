package dev.razorplay.customplayeranimations.animation.animations;

import dev.razorplay.customplayeranimations.compat.CarryOnCompat;
import dev.razorplay.customplayeranimations.compat.SupplementariesCompat;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.fabricmc.loader.api.FabricLoader;

import static dev.razorplay.customplayeranimations.util.Util.disableArmInBuilder;

public class CompatAnimation {
    private CompatAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && FabricLoader.getInstance().isModLoaded("supplementaries") && SupplementariesCompat.checkFluteItem(context.player().getUseItem().getItem())) {
            disableBothArms(context);
        }
        if (FabricLoader.getInstance().isModLoaded("carryon") && CarryOnCompat.check(context.player())) {
            disableBothArms(context);
        }
    }

    private static void disableBothArms(AnimationContext context) {
        disableArmInBuilder(context, BodyParts.RIGHT_ARM);
        disableArmInBuilder(context, BodyParts.LEFT_ARM);
    }
}
