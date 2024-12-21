package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.util.enums.ArmsEnum;
import dev.razorplay.customplayeranimations.util.enums.ModifiersEnum;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.PotionItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.EAT_ANIMATION;
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
                    setEatingAnimation(context, ArmsEnum.RIGHT_ARM, false);
                } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                    setEatingAnimation(context, ArmsEnum.LEFT_ARM, true);
                }
            }
        }
    }

    private static void setEatingAnimation(AnimationContext context, ArmsEnum arm, boolean mirror) {
        context.overlayAnimationContainer().setCurrentAnimation(EAT_ANIMATION.animation());
        context.overlayAnimationContainer().setCurrentAnimationId((mirror ? LEFT_PREFIX : RIGHT_PREFIX) + EAT_ANIMATION.animationId());
        disableArmOverlayPos(context, arm);
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(mirror);
    }

    private static void disableArmOverlayPos(AnimationContext context, ArmsEnum arm) {
        KeyframeAnimation.AnimationBuilder builder = context.mainAnimationContainer().getCurrentAnimation().mutableCopy();
        var currentArm = builder.getPart(arm.getArmId());
        if (currentArm != null) {
            currentArm.setEnabled(false);
            currentArm.x.setEnabled(false);
            currentArm.y.setEnabled(false);
            currentArm.z.setEnabled(false);
        }
        context.mainAnimationContainer().setCurrentAnimation(builder.build());
    }
}
