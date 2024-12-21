package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.util.enums.ModifiersEnum;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.PickaxeItem;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;


import static dev.razorplay.customplayeranimations.animation.AnimationProvider.AXE_ANIMATION;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.AXE_SNEAK_ANIMATION;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class AxeAnimation {
    private AxeAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().swinging && context.player().getMainHandItem().getItem() instanceof AxeItem && context.player().swingingArm.equals(MAIN_HAND)) {
            if (!CONFIG.axeAnimationsConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
            } else {
                context.overlayAnimationContainer().setAnimationSpeed(CONFIG.axeAnimationsConfig.getSpeedMultiplier());
                context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.axeAnimationsConfig.getFadeTime());
                context.overlayAnimationContainer().setAnimationPriority(CONFIG.axeAnimationsConfig.getPriority());

                ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(context.playerData().getRightHand() != MAIN_HAND);

                context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? AXE_SNEAK_ANIMATION.animation() : AXE_ANIMATION.animation());
                context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AXE_SNEAK_ANIMATION.animationId() : AXE_ANIMATION.animationId());
            }
        }
    }
}
