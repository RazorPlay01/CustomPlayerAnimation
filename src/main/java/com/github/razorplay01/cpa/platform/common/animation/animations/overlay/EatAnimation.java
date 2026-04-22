package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.Modifiers;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.world.item.PotionItem;

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
				//? if >=1.21.1{
                context.player().getUseItem().getItem().components().get(net.minecraft.core.component.DataComponents.FOOD) != null ||
				//?}
				//? if <1.21.1{
				/*context.player().getUseItem().getItem().getFoodProperties() != null ||
				*///?}
                context.player().getUseItem().getItem() instanceof PotionItem;

    }

    private static void setEatingAnimation(AnimationContext context, boolean mirror) {
        context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.EAT_ANIMATION.getAnimationId()));
        context.overlayAnimationContainer().setCurrentAnimationId((mirror ? LEFT_PREFIX : RIGHT_PREFIX) + AnimationsId.EAT_ANIMATION.getAnimationId());
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()))./*? if >=1.21.1 {*/enabled = mirror/*?} else {*/ /*setEnabled(mirror)*//*?}*/;
    }
}
