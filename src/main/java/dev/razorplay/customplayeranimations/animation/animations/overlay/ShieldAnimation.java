package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.ShieldItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.*;

public class ShieldAnimation {
    private ShieldAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof ShieldItem) {
            if (!CONFIG.shieldAnimationConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
                disableActiveArm(context, context.mainAnimationContainer());
            } else {
                context.overlayAnimationContainer().setAnimationSpeed(CONFIG.shieldAnimationConfig.getSpeedMultiplier());
                context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.shieldAnimationConfig.getFadeTime());
                context.overlayAnimationContainer().setAnimationPriority(CONFIG.shieldAnimationConfig.getPriority());

                if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                    setShieldAnimation(context, true);
                } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                    setShieldAnimation(context, false);
                }
            }
        }
    }

    private static void setShieldAnimation(AnimationContext context, boolean isRightHand) {
        if (context.player().isCrouching()) {
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SHIELD_SNEAK_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.SHIELD_SNEAK_ANIMATION.getAnimationId());
        } else {
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SHIELD_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.SHIELD_ANIMATION.getAnimationId());
        }
        disableBodyPart(context.mainAnimationContainer(), isRightHand ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(!isRightHand);
    }
}
