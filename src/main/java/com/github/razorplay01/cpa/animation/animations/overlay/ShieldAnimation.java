package com.github.razorplay01.cpa.animation.animations.overlay;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.enums.Modifiers;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.world.item.ShieldItem;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.cpa.util.Util.*;

public class ShieldAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getOverlayAnimations().useItemAnimation.shieldAnimationConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
            context.iAnimationControl().disableActiveArm(context.mainAnimationContainer());
        } else {
            configureAnimationContainer(CONFIG.getOverlayAnimations().useItemAnimation.shieldAnimationConfig, context.overlayAnimationContainer());

            if (context.avatar().getUsedItemHand().equals(context.playerData().getRightHand())) {
                setShieldAnimation(context, true);
            } else if (context.avatar().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                setShieldAnimation(context, false);
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.avatar().isUsingItem() &&
                context.avatar().getUseItem().getItem() instanceof ShieldItem;
    }

    private static void setShieldAnimation(AnimationContext context, boolean isRightHand) {
        if (context.avatar().isCrouching()) {
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SHIELD_SNEAK_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.SHIELD_SNEAK_ANIMATION.getAnimationId());
        } else {
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SHIELD_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.SHIELD_ANIMATION.getAnimationId());
        }
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (isRightHand);
    }
}
