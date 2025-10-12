package com.github.razorplay01.cpa.animation.animations.overlay;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.enums.Modifiers;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.PotionItem;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.cpa.util.Util.*;

public class EatAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getOverlayAnimations().useItemAnimation.eatingAnimationsConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.getOverlayAnimations().useItemAnimation.eatingAnimationsConfig, context.overlayAnimationContainer());

            if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                setEatingAnimation(context, false);
            } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                setEatingAnimation(context, true);
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isUsingItem() &&
                context.player().getUseItem().getItem().components().get(DataComponents.FOOD) != null ||
                context.player().getUseItem().getItem() instanceof PotionItem;
    }

    private static void setEatingAnimation(AnimationContext context, boolean mirror) {
        context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.EAT_ANIMATION.getAnimationId()));
        context.overlayAnimationContainer().setCurrentAnimationId((mirror ? LEFT_PREFIX : RIGHT_PREFIX) + AnimationsId.EAT_ANIMATION.getAnimationId());
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = mirror;
    }
}
