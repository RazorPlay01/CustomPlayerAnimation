package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.util.enums.ModifiersEnum;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ShovelItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.*;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class ShovelAnimation {
    private ShovelAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().swinging && context.player().getMainHandItem().getItem() instanceof ShovelItem && context.player().swingingArm.equals(MAIN_HAND)) {
            if (!CONFIG.shovelAnimationsConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
            } else {
                context.overlayAnimationContainer().setAnimationSpeed(CONFIG.shovelAnimationsConfig.getSpeedMultiplier());
                context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.shovelAnimationsConfig.getFadeTime());
                context.overlayAnimationContainer().setAnimationPriority(CONFIG.shovelAnimationsConfig.getPriority());

                ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(context.playerData().getRightHand() != MAIN_HAND);

                context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? SHOVEL_SNEAK_ANIMATION.animation() : SHOVEL_ANIMATION.animation());
                context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? SHOVEL_SNEAK_ANIMATION.animationId() : SHOVEL_ANIMATION.animationId());
            }
        }
    }
}
