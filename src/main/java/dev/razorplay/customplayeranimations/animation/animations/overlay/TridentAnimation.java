package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.TridentItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.*;

public class TridentAnimation {
    private TridentAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof TridentItem) {
            if (!CONFIG.tridentAnimationConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
                context.overlayAnimationContainer().setAnimationFadeTime(1);
                disableActiveArm(context, context.mainAnimationContainer());
            } else {
                context.overlayAnimationContainer().setAnimationSpeed(CONFIG.tridentAnimationConfig.getSpeedMultiplier());
                context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.tridentAnimationConfig.getFadeTime());
                context.overlayAnimationContainer().setAnimationPriority(CONFIG.tridentAnimationConfig.getPriority());

                if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                    setTridentAnimation(context, true, 55);
                } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                    setTridentAnimation(context, false, -55);
                }
            }
        }
    }

    private static void setTridentAnimation(AnimationContext context, boolean isRightHand, int yawOffset) {
        if (context.player().isCrouching()) {
            disableBodyPart(context.mainAnimationContainer(), isRightHand ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        } else {
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.TRIDENT_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.TRIDENT_ANIMATION.getAnimationId());
        }
        context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() + yawOffset);
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(!isRightHand);
    }
}
