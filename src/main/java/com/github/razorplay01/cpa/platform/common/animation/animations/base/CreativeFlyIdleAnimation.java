package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.world.entity.player.Player;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;

public class CreativeFlyIdleAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        playFlyIdleCreativeAnimation(context);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
		if (!(context.player() instanceof Player player)) return false;
		return context.playerData().getFlychecker() > 10 &&
				!player.isPassenger() &&
				player.isCreative();
    }

    private static void playFlyIdleCreativeAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().idleAnimations.idleCreativeFlyingAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.getMainAnimations().idleAnimations.idleCreativeFlyingAnimationConfig, context.mainAnimationContainer());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.IDLE_CREATIVE_FLY_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.IDLE_CREATIVE_FLY_ANIMATION.getAnimationId());
        }
    }
}
