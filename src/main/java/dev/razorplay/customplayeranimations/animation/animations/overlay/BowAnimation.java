package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.BowItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.*;

public class BowAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.useItemAnimation.bowAnimationsConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
            if (context.playerData().getMainArmPose().equals(HumanoidModel.ArmPose.BOW_AND_ARROW) ||
                    context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.BOW_AND_ARROW)) {
                context.player().disableBodyPartAnimationInAllContainers(BodyParts.RIGHT_ARM);
                context.player().disableBodyPartAnimationInAllContainers(BodyParts.LEFT_ARM);
            }
        } else {
            configureAnimationContainer(CONFIG.useItemAnimation.bowAnimationsConfig, context.overlayAnimationContainer());

            if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                setBowAnimationForHand(context, true);
            } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                setBowAnimationForHand(context, false);
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof BowItem;
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
        context.player().disableBodyPartAnimation(context.mainAnimationContainer(), isRightHand ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);

        if (isRightHand) {
            ((AdjustmentModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.RIGHT_BOW_MODIFIER.getModifierId())).enabled = true;
            context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() - 90);
        } else {
            ((AdjustmentModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.LEFT_BOW_MODIFIER.getModifierId())).enabled = true;
            context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() + 90);
        }
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(!isRightHand);
    }
}
