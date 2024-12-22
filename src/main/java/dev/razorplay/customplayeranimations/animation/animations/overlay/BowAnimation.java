package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.BowItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
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
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.BOW_SNEAK_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.BOW_SNEAK_ANIMATION.getAnimationId());
            context.overlayAnimationContainer().setAnimationFadeTime(1);
        } else {
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.BOW_IDLE_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.BOW_IDLE_ANIMATION.getAnimationId());
        }
        disableArmOverlayPos(context, isRightHand ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);

        if (isRightHand) {
            ((AdjustmentModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.RIGHT_BOW_MODIFIER.getModifierId())).enabled = true;
            context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() - 90);
        } else {
            ((AdjustmentModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.LEFT_BOW_MODIFIER.getModifierId())).enabled = true;
            context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() + 90);
        }
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(!isRightHand);
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

    private static void disableArmInBuilder(AnimationContext context, BodyParts arm) {
        KeyframeAnimation.AnimationBuilder builder = context.mainAnimationContainer().getCurrentAnimation().mutableCopy();
        var armPart = builder.getPart(arm.getPartId());
        if (armPart != null) {
            armPart.pitch.setEnabled(false);
            armPart.yaw.setEnabled(false);
            armPart.roll.setEnabled(false);
        }
    }

    private static void disableBothArms(AnimationContext context) {
        KeyframeAnimation.AnimationBuilder builder = context.mainAnimationContainer().getCurrentAnimation().mutableCopy();
        disableArmInBuilder(context, BodyParts.RIGHT_ARM);
        disableArmInBuilder(context, BodyParts.LEFT_ARM);
        context.mainAnimationContainer().setCurrentAnimation(builder.build());
    }
}
