package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.util.enums.ArmsEnum;
import dev.razorplay.customplayeranimations.util.enums.ModifiersEnum;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.BowItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.*;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.BOW_IDLE_ANIMATION;
import static dev.razorplay.customplayeranimations.util.Util.LEFT_PREFIX;
import static dev.razorplay.customplayeranimations.util.Util.RIGHT_PREFIX;

public class BowAnimation {
    private BowAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof BowItem) {
            if (!CONFIG.bowAnimationsConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
                disableBothArms(context);
                if (context.playerData().getMainArmPose().equals(HumanoidModel.ArmPose.BOW_AND_ARROW) ||
                        context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.BOW_AND_ARROW)) {
                    disableBothArms(context);
                }
            } else {
                context.overlayAnimationContainer().setAnimationSpeed(CONFIG.bowAnimationsConfig.getSpeedMultiplier());
                context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.bowAnimationsConfig.getFadeTime());
                context.overlayAnimationContainer().setAnimationPriority(CONFIG.bowAnimationsConfig.getPriority());

                if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                    setBowAnimationForHand(context, true);
                } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                    setBowAnimationForHand(context, false);
                }
            }
        }
    }

    private static void setBowAnimationForHand(AnimationContext context, boolean isRightHand) {
        if (context.player().isCrouching()) {
            context.overlayAnimationContainer().setCurrentAnimation(BOW_SNEAK_ANIMATION.animation());
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + BOW_SNEAK_ANIMATION.animationId());
            context.overlayAnimationContainer().setAnimationFadeTime(1);
        } else {
            context.overlayAnimationContainer().setCurrentAnimation(BOW_IDLE_ANIMATION.animation());
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + BOW_IDLE_ANIMATION.animationId());
        }
        disableArmOverlayPos(context, isRightHand ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);

        if (isRightHand) {
            ((AdjustmentModifier) context.overlayAnimationContainer().getAnimationModifiers().get(ModifiersEnum.RIGHT_BOW_MODIFIER.getModifierId())).enabled = true;
            context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() - 90);
        } else {
            ((AdjustmentModifier) context.overlayAnimationContainer().getAnimationModifiers().get(ModifiersEnum.LEFT_BOW_MODIFIER.getModifierId())).enabled = true;
            context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() + 90);
        }
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(!isRightHand);
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

    private static void disableArmInBuilder(AnimationContext context, ArmsEnum arm) {
        KeyframeAnimation.AnimationBuilder builder = context.mainAnimationContainer().getCurrentAnimation().mutableCopy();
        var armPart = builder.getPart(arm.getArmId());
        if (armPart != null) {
            armPart.pitch.setEnabled(false);
            armPart.yaw.setEnabled(false);
            armPart.roll.setEnabled(false);
        }
    }

    private static void disableBothArms(AnimationContext context) {
        KeyframeAnimation.AnimationBuilder builder = context.mainAnimationContainer().getCurrentAnimation().mutableCopy();
        disableArmInBuilder(context, ArmsEnum.RIGHT_ARM);
        disableArmInBuilder(context, ArmsEnum.LEFT_ARM);
        context.mainAnimationContainer().setCurrentAnimation(builder.build());
    }
}
