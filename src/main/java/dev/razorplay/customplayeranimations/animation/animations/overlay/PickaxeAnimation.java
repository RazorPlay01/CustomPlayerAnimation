package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.PickaxeItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class PickaxeAnimation {
    private PickaxeAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().swinging && context.player().getMainHandItem().getItem() instanceof PickaxeItem && context.player().swingingArm.equals(MAIN_HAND)) {
            if (!CONFIG.toolsAnimations.pickaxeAnimationsConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
            } else {
                context.overlayAnimationContainer().setAnimationSpeed(CONFIG.toolsAnimations.pickaxeAnimationsConfig.getSpeedMultiplier());
                context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.toolsAnimations.pickaxeAnimationsConfig.getFadeTime());
                context.overlayAnimationContainer().setAnimationPriority(CONFIG.toolsAnimations.pickaxeAnimationsConfig.getPriority());

                ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(context.playerData().getRightHand() != MAIN_HAND);

                context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? getAnimation(AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.PICKAXE_ANIMATION.getAnimationId()));
                context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId() : AnimationsId.PICKAXE_ANIMATION.getAnimationId());
            }
        }
    }
}
