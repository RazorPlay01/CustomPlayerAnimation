package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.enums.BodyParts;
import com.github.razorplay01.customplayeranimation.util.enums.Modifiers;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.world.item.TridentItem;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.*;

public class TridentAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getOverlayAnimations().useItemAnimation.tridentAnimationConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
            context.player().disableActiveArm(context.mainAnimationContainer());
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
            context.player().disableBodyPartAnimation(context.mainAnimationContainer(), isRightHand ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        } else {
            context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.TRIDENT_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.TRIDENT_ANIMATION.getAnimationId());
        }
        context.player().setYBodyRot(context.playerData().getPlayerHeadYaw() + yawOffset);
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = !isRightHand;
    }
}
