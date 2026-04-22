package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.Modifiers;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.world.item.ShieldItem;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.LEFT_PREFIX;
import static com.github.razorplay01.cpa.platform.common.util.Util.RIGHT_PREFIX;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;
import static net.minecraft.world.InteractionHand.MAIN_HAND;
//? if >=1.21.1{
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
//?}
//? if <1.21.1{
/*import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
*///?}
public class ShieldAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getOverlayAnimations().useItemAnimation.shieldAnimationConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
			context.iAnimationControl().disableActiveArm(context.mainAnimationContainer());
        } else {
            configureAnimationContainer(CONFIG.getOverlayAnimations().useItemAnimation.shieldAnimationConfig, context.overlayAnimationContainer());

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
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()))./*? if >=1.21.1 {*/enabled = isRightHand/*?} else {*/ /*setEnabled(isRightHand)*//*?}*/;
    }
}
