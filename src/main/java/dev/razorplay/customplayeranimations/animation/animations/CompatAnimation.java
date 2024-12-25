package dev.razorplay.customplayeranimations.animation.animations;

import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.compat.CarryOnCompat;
import dev.razorplay.customplayeranimations.compat.SupplementariesCompat;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.fabricmc.loader.api.FabricLoader;

public class CompatAnimation implements ICustomAnimation {
    public void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && FabricLoader.getInstance().isModLoaded("supplementaries") && SupplementariesCompat.checkFluteItem(context.player().getUseItem().getItem())) {
            disableBothArms(context);
        }
        if (FabricLoader.getInstance().isModLoaded("carryon") && CarryOnCompat.check(context.player())) {
            disableBothArms(context);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return true;
    }

    private static void disableBothArms(AnimationContext context) {
        context.player().disableBodyPartAnimationInAllContainers(BodyParts.RIGHT_ARM);
        context.player().disableBodyPartAnimationInAllContainers(BodyParts.LEFT_ARM);
    }
}
