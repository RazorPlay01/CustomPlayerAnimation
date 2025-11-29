package com.github.razorplay01.cpa.platform.common.animation.animations.special;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.world.item.ItemStack;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;


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
			configureAnimationContainer(CONFIG.getSpecialAnimations().itemSwapAnimationConfig, context.specialAnimationContainer());
			AnimationContainer.setAnimation(context.specialAnimationContainer(), AnimationsId.ITEM_SWAP_ANIMATION);
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
