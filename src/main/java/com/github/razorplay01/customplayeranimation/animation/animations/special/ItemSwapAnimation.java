package com.github.razorplay01.customplayeranimation.animation.animations.special;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;

public class ItemSwapAnimation implements ICustomAnimation {
    public void playAnimation(AnimationContext context) {
        if ((!context.player().getMainHandItem().isEmpty() || !context.player().getOffhandItem().isEmpty())
                && context.playerData().getMainHandItem().getItem() != context.playerData().getOffHandItem().getItem()
                && context.playerData().getMainHandItem().getItem() == context.player().getOffhandItem().getItem()
                && context.playerData().getOffHandItem().getItem() == context.player().getMainHandItem().getItem()) {
            context.specialAnimationContainer().setAnimationSpeed(CONFIG.specialAnimations.itemSwapAnimationConfig.getSpeedMultiplier());
            context.specialAnimationContainer().setAnimationFadeTime(CONFIG.specialAnimations.itemSwapAnimationConfig.getFadeTime());
            context.specialAnimationContainer().setAnimationPriority(CONFIG.specialAnimations.itemSwapAnimationConfig.getPriority());
            context.specialAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.ITEM_SWAP_ANIMATION.getAnimationId()));
            context.specialAnimationContainer().setCurrentAnimationId(AnimationsId.ITEM_SWAP_ANIMATION.getAnimationId());
        }
        context.playerData().setMainHandItem(context.player().getMainHandItem());
        context.playerData().setOffHandItem(context.player().getOffhandItem());
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return true;
    }
}
