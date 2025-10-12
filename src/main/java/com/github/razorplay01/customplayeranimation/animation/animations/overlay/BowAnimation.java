package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.enums.Modifiers;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.BowItem;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.*;

public class BowAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getOverlayAnimations().useItemAnimation.bowAnimationsConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
            if (context.playerData().getMainArmPose().equals(HumanoidModel.ArmPose.BOW_AND_ARROW) ||
                    context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.BOW_AND_ARROW)) {
                disableBothArms(context);
            }
        } else {
            configureAnimationContainer(CONFIG.getOverlayAnimations().useItemAnimation.bowAnimationsConfig, context.overlayAnimationContainer());

            if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                setBowAnimationForHand(context, true);
            } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                setBowAnimationForHand(context, false);
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isUsingItem() &&
                context.player().getUseItem().getItem() instanceof BowItem;
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

        if (isRightHand) {
            context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() - 90);
        } else {
            context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() + 90);
        }
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (!isRightHand);
    }
}
