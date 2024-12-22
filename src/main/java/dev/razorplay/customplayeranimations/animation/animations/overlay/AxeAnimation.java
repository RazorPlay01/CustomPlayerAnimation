package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.AxeItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;


import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class AxeAnimation {
    private AxeAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().swinging && context.player().getMainHandItem().getItem() instanceof AxeItem && context.player().swingingArm.equals(MAIN_HAND)) {
            if (!CONFIG.toolsAnimations.axeAnimationsConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
            } else {
                context.overlayAnimationContainer().setAnimationSpeed(CONFIG.toolsAnimations.axeAnimationsConfig.getSpeedMultiplier());
                context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.toolsAnimations.axeAnimationsConfig.getFadeTime());
                context.overlayAnimationContainer().setAnimationPriority(CONFIG.toolsAnimations.axeAnimationsConfig.getPriority());

                ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(context.playerData().getRightHand() != MAIN_HAND);

                context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? getAnimation(AnimationsId.AXE_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.AXE_ANIMATION.getAnimationId()));
                context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AnimationsId.AXE_SNEAK_ANIMATION.getAnimationId() : AnimationsId.AXE_ANIMATION.getAnimationId());
            }
        }
    }
}
