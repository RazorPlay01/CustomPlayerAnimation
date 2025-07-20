package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import net.minecraft.world.item.ShieldItem;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.*;

public class ShieldAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.useItemAnimation.shieldAnimationConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
            context.player().disableActiveArm(context.mainAnimationContainer());
        } else {
            configureAnimationContainer(CONFIG.useItemAnimation.shieldAnimationConfig, context.overlayAnimationContainer());

            if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                setShieldAnimation(context, true);
            } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                setShieldAnimation(context, false);
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isUsingItem() &&
                context.player().getUseItem().getItem() instanceof ShieldItem;
    }

    private static void setShieldAnimation(AnimationContext context, boolean isRightHand) {
        if (context.player().isCrouching()) {
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SHIELD_SNEAK_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.SHIELD_SNEAK_ANIMATION.getAnimationId());
        } else {
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SHIELD_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.SHIELD_ANIMATION.getAnimationId());
        }
        //todo: ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(isRightHand);
    }
}
