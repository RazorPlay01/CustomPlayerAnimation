package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.util.enums.ModifiersEnum;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.PickaxeItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.*;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class PickaxeAnimation {
    private PickaxeAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().swinging && context.player().getMainHandItem().getItem() instanceof PickaxeItem && context.player().swingingArm.equals(MAIN_HAND)) {
            if (!CONFIG.pickaxeAnimationsConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
            } else {
                context.overlayAnimationContainer().setAnimationSpeed(CONFIG.pickaxeAnimationsConfig.getSpeedMultiplier());
                context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.pickaxeAnimationsConfig.getFadeTime());
                context.overlayAnimationContainer().setAnimationPriority(CONFIG.pickaxeAnimationsConfig.getPriority());

                ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(context.playerData().getRightHand() != MAIN_HAND);

                context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? PICKAXE_SNEAK_ANIMATION.animation() : PICKAXE_ANIMATION.animation());
                context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? PICKAXE_SNEAK_ANIMATION.animationId() : PICKAXE_ANIMATION.animationId());
            }
        }
    }
}
