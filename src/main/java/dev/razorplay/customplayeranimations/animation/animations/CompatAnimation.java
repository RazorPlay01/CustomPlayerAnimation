package dev.razorplay.customplayeranimations.animation.animations;

import dev.razorplay.customplayeranimations.compat.CarryOnCompat;
import dev.razorplay.customplayeranimations.compat.SupplementariesCompat;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.IS_CARRYON_LOADED;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.IS_SUPPLEMENTARIES_LOADED;
import static dev.razorplay.customplayeranimations.util.Util.disableArmInBuilder;

public class CompatAnimation {
    private CompatAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && IS_SUPPLEMENTARIES_LOADED && SupplementariesCompat.checkFluteItem(context.player().getUseItem().getItem())) {
            disableBothArms(context);
        }
        if (IS_CARRYON_LOADED && CarryOnCompat.check(context.player())) {
            disableBothArms(context);
        }
    }

    private static void disableBothArms(AnimationContext context) {
        disableArmInBuilder(context, BodyParts.RIGHT_ARM);
        disableArmInBuilder(context, BodyParts.LEFT_ARM);
    }
}
