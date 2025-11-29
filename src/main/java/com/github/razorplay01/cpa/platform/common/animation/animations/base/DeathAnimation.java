package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;

public class DeathAnimation implements ICustomAnimation {
	@Override
	public void playAnimation(AnimationContext context) {
		if (!CONFIG.getMainAnimations().deathAnimations.isEnabled()) {
			context.mainAnimationContainer().disableAnimation();
		} else {
			AnimationsId animationId = getDeathAnimationId(context);

			configureAnimations(context, animationId);

			AnimationContainer.setAnimation(context.mainAnimationContainer(), animationId);
		}
	}

	private static void configureAnimations(AnimationContext context, AnimationsId animationId) {
		switch (animationId) {
			case DEATH_BURN_ANIMATION ->
					configureAnimationContainer(CONFIG.getMainAnimations().deathAnimations.deathBurnAnimationConfig, context.mainAnimationContainer());
			case DEATH_EXPLOSION_ANIMATION ->
					configureAnimationContainer(CONFIG.getMainAnimations().deathAnimations.deathExplosionAnimationConfig, context.mainAnimationContainer());
			case DEATH_DROWN_ANIMATION ->
					configureAnimationContainer(CONFIG.getMainAnimations().deathAnimations.deathDrownAnimationConfig, context.mainAnimationContainer());
			default ->
					configureAnimationContainer(CONFIG.getMainAnimations().deathAnimations.deathAnimationConfig, context.mainAnimationContainer());
		}
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		return context.player().getHealth() <= 0;
	}

	private AnimationsId getDeathAnimationId(AnimationContext context) {
		DamageSource lastDamageSource = context.player().getLastDamageSource();

		if (lastDamageSource == null) {
			return AnimationsId.DEATH_DEFAULT_ANIMATION;
		}

		if (lastDamageSource.is(DamageTypes.IN_FIRE) || lastDamageSource.is(DamageTypes.ON_FIRE) || lastDamageSource.is(DamageTypes.CAMPFIRE)) {
			return getAnimationIfEnabled(CONFIG.getMainAnimations().deathAnimations.deathBurnAnimationConfig.isEnabled(), AnimationsId.DEATH_BURN_ANIMATION);
		} else if (lastDamageSource.is(DamageTypes.EXPLOSION) || lastDamageSource.is(DamageTypes.PLAYER_EXPLOSION)) {
			return getAnimationIfEnabled(CONFIG.getMainAnimations().deathAnimations.deathExplosionAnimationConfig.isEnabled(), AnimationsId.DEATH_EXPLOSION_ANIMATION);
		} else if (lastDamageSource.is(DamageTypes.DROWN)) {
			return getAnimationIfEnabled(CONFIG.getMainAnimations().deathAnimations.deathDrownAnimationConfig.isEnabled(), AnimationsId.DEATH_DROWN_ANIMATION);
		}

		return AnimationsId.DEATH_DEFAULT_ANIMATION;
	}

	private AnimationsId getAnimationIfEnabled(boolean isEnabled, AnimationsId specificAnimationId) {
		return isEnabled ? specificAnimationId : AnimationsId.DEATH_DEFAULT_ANIMATION;
	}
}
