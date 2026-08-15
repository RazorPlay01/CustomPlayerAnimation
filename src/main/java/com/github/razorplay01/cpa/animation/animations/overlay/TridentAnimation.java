package com.github.razorplay01.cpa.animation.animations.overlay;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.enums.BodyParts;
import com.github.razorplay01.cpa.util.enums.Modifiers;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import net.minecraft.world.item.TridentItem;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.util.Util.LEFT_PREFIX;
import static com.github.razorplay01.cpa.util.Util.RIGHT_PREFIX;
import static com.github.razorplay01.cpa.util.Util.configureAnimationContainer;
//? if >=1.21.1{
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
//?}
//? if <1.21.1{
/*import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
*///?}
public class TridentAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getOverlayAnimations().useItemAnimation.tridentAnimationConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
			context.iAnimationControl().disableActiveArm(context.mainAnimationContainer());
        } else {
            configureAnimationContainer(CONFIG.getOverlayAnimations().useItemAnimation.tridentAnimationConfig, context.overlayAnimationContainer());

            if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                setTridentAnimation(context, true, 55);
            } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                setTridentAnimation(context, false, -55);
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof TridentItem;
    }

    private static void setTridentAnimation(AnimationContext context, boolean isRightHand, int yawOffset) {
        if (context.player().isCrouching()) {
			context.iAnimationControl().disableBodyPartAnimation(context.mainAnimationContainer(), isRightHand ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        } else {
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.TRIDENT_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.TRIDENT_ANIMATION.getAnimationId());
        }
        context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() + yawOffset);
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()))./*? if >=1.21.1 {*/enabled = isRightHand/*?} else {*/ /*setEnabled(isRightHand)*//*?}*/;
    }
}
