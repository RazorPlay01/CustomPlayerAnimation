package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.PotionItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.LEFT_PREFIX;
import static dev.razorplay.customplayeranimations.util.Util.RIGHT_PREFIX;

public class EatAnimation {
    private EatAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && context.player().getUseItem().getItem().components().get(DataComponents.FOOD) != null || context.player().getUseItem().getItem() instanceof PotionItem) {
            if (!CONFIG.eatingAnimationsConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
            } else {
                context.overlayAnimationContainer().setAnimationSpeed(CONFIG.eatingAnimationsConfig.getSpeedMultiplier());
                context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.eatingAnimationsConfig.getFadeTime());
                context.overlayAnimationContainer().setAnimationPriority(CONFIG.eatingAnimationsConfig.getPriority());

                if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                    setEatingAnimation(context, BodyParts.RIGHT_ARM, false);
                } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                    setEatingAnimation(context, BodyParts.LEFT_ARM, true);
                }
            }
        }
    }

    private static void setEatingAnimation(AnimationContext context, BodyParts arm, boolean mirror) {
        context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.EAT_ANIMATION.getAnimationId()));
        context.overlayAnimationContainer().setCurrentAnimationId((mirror ? LEFT_PREFIX : RIGHT_PREFIX) + AnimationsId.EAT_ANIMATION.getAnimationId());
        disableArmOverlayPos(context, arm);
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(mirror);
    }

    private static void disableArmOverlayPos(AnimationContext context, BodyParts arm) {
        KeyframeAnimation.AnimationBuilder builder = context.mainAnimationContainer().getCurrentAnimation().mutableCopy();
        var currentArm = builder.getPart(arm.getPartId());
        if (currentArm != null) {
            currentArm.setEnabled(false);
            currentArm.x.setEnabled(false);
            currentArm.y.setEnabled(false);
            currentArm.z.setEnabled(false);
        }
        context.mainAnimationContainer().setCurrentAnimation(builder.build());
    }
}
