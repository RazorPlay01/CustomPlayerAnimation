package com.github.razorplay01.cpa.animation.animations.special;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import net.minecraft.world.item.ItemStack;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;

public class ItemSwapAnimation implements ICustomAnimation {
    public void playAnimation(AnimationContext context) {
        boolean configEnabled = CONFIG.getSpecialAnimations().itemSwapAnimationConfig.isEnabled();
        ItemStack currentMain = context.player().getMainHandItem();
        ItemStack currentOff = context.player().getOffhandItem();
        ItemStack prevMain = context.playerData().getMainHandItem();
        ItemStack prevOff = context.playerData().getOffHandItem();
        boolean swapHappened = (!currentMain.isEmpty() || !currentOff.isEmpty())
                && prevMain.getItem() != prevOff.getItem()
                && prevMain.getItem() == currentOff.getItem()
                && prevOff.getItem() == currentMain.getItem();
        if (configEnabled && swapHappened) {
            context.specialAnimationContainer().setAnimationSpeed(CONFIG.getSpecialAnimations().itemSwapAnimationConfig.getSpeedMultiplier());
            context.specialAnimationContainer().setAnimationFadeTime(CONFIG.getSpecialAnimations().itemSwapAnimationConfig.getFadeTime());
            context.specialAnimationContainer().setAnimationPriority(CONFIG.getSpecialAnimations().itemSwapAnimationConfig.getPriority());
            context.specialAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.ITEM_SWAP_ANIMATION.getAnimationId()));
            context.specialAnimationContainer().setCurrentAnimationId(AnimationsId.ITEM_SWAP_ANIMATION.getAnimationId());
        } else if (!configEnabled) {
            // Solo deshabilita si está deshabilitado en config y es la animación actual (raro para one-shot)
            if (context.specialAnimationContainer().getCurrentAnimationId().equals(AnimationsId.ITEM_SWAP_ANIMATION.getAnimationId())) {
                context.specialAnimationContainer().disableAnimation();
            }
        }
        // No deshabilita si no hay swap pero config enabled: deja que termine si está reproduciéndose
        context.playerData().setMainHandItem(currentMain);
        context.playerData().setOffHandItem(currentOff);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return true;  // Similar a uphand, lógica en play para deshabilitar si necesario.
    }
}