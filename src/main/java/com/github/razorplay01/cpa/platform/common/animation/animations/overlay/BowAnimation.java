package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.Modifiers;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.BowItem;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.LEFT_PREFIX;
import static com.github.razorplay01.cpa.platform.common.util.Util.RIGHT_PREFIX;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;
import static com.github.razorplay01.cpa.platform.common.util.Util.disableBothArms;
import static net.minecraft.world.InteractionHand.MAIN_HAND;
//? if >=1.21.1{
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
//?}
//? if <1.21.1{
/*import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
*///?}
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
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()))./*? if >=1.21.1 {*/enabled = !isRightHand/*?} else {*/ /*setEnabled(!isRightHand)*//*?}*/;
    }
}
