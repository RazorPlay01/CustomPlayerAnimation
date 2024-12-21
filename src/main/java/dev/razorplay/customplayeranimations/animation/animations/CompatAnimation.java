package dev.razorplay.customplayeranimations.animation.animations;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.compat.CarryOnCompat;
import dev.razorplay.customplayeranimations.compat.SupplementariesCompat;
import dev.razorplay.customplayeranimations.util.enums.ArmsEnum;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.CrossbowItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.IS_CARRYON_LOADED;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.IS_SUPPLEMENTARIES_LOADED;

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

    private static void disableArmInBuilder(AnimationContext context, ArmsEnum arm) {
        KeyframeAnimation.AnimationBuilder builder = context.mainAnimationContainer().getCurrentAnimation().mutableCopy();
        var armPart = builder.getPart(arm.getArmId());
        if (armPart != null) {
            armPart.pitch.setEnabled(false);
            armPart.yaw.setEnabled(false);
            armPart.roll.setEnabled(false);
        }
    }

    private static void disableBothArms(AnimationContext context) {
        KeyframeAnimation.AnimationBuilder builder = context.mainAnimationContainer().getCurrentAnimation().mutableCopy();
        disableArmInBuilder(context, ArmsEnum.RIGHT_ARM);
        disableArmInBuilder(context, ArmsEnum.LEFT_ARM);
        context.mainAnimationContainer().setCurrentAnimation(builder.build());
    }
}
