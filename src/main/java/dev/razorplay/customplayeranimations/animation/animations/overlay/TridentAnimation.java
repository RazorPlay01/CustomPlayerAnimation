package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.util.enums.ArmsEnum;
import dev.razorplay.customplayeranimations.util.enums.ModifiersEnum;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.TridentItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.TRIDENT_ANIMATION;
import static dev.razorplay.customplayeranimations.util.Util.LEFT_PREFIX;
import static dev.razorplay.customplayeranimations.util.Util.RIGHT_PREFIX;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class TridentAnimation {
    private TridentAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof TridentItem) {
            if (!CONFIG.tridentAnimationConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
                disableActiveArm(context);
                context.overlayAnimationContainer().setAnimationFadeTime(1);
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
            disableArmOverlayPos(context, ArmsEnum.RIGHT_ARM);
            disableArmOverlayPos(context, ArmsEnum.LEFT_ARM);
        } else {
            context.overlayAnimationContainer().setCurrentAnimation(TRIDENT_ANIMATION.animation());
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + TRIDENT_ANIMATION.animationId());
        }
        context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() + yawOffset);
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

    private static void disableActiveArm(AnimationContext context) {
        if (context.player().getUsedItemHand().equals(MAIN_HAND)) {
            if (context.player().getMainArm() == HumanoidArm.RIGHT) {
                disableArmInBuilder(context, ArmsEnum.RIGHT_ARM);
            } else {
                disableArmInBuilder(context, ArmsEnum.LEFT_ARM);
            }
        } else {
            if (context.player().getMainArm() == HumanoidArm.RIGHT) {
                disableArmInBuilder(context, ArmsEnum.LEFT_ARM);
            } else {
                disableArmInBuilder(context, ArmsEnum.RIGHT_ARM);
            }
        }
    }
}
