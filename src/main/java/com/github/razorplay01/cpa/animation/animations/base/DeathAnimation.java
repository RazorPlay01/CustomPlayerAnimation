package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;

public class DeathAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().deathAnimations.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            // Determina la animación según la causa de la muerte
            String animationId = getDeathAnimationId(context);
            configureAnimationContainer(context, animationId);

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(animationId));
            context.mainAnimationContainer().setCurrentAnimationId(animationId);
        }
    }

    private static void configureAnimationContainer(AnimationContext context, String animationId) {
        switch (animationId) {
            case "death_burn" -> {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.getMainAnimations().deathAnimations.deathBurnAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().deathAnimations.deathBurnAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().deathAnimations.deathBurnAnimationConfig.getPriority());
            }
            case "death_explosion" -> {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.getMainAnimations().deathAnimations.deathExplosionAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().deathAnimations.deathExplosionAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().deathAnimations.deathExplosionAnimationConfig.getPriority());
            }
            case "death_drown" -> {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.getMainAnimations().deathAnimations.deathDrownAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().deathAnimations.deathDrownAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().deathAnimations.deathDrownAnimationConfig.getPriority());
            }
            default -> {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.getMainAnimations().deathAnimations.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.getMainAnimations().deathAnimations.getFadeTime());
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.getMainAnimations().deathAnimations.getPriority());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.avatar().getHealth() <= 0;
    }

    private String getDeathAnimationId(AnimationContext context) {
        DamageSource lastDamageSource = context.avatar().getLastDamageSource();
        if (lastDamageSource != null) {
            if (lastDamageSource.is(DamageTypes.IN_FIRE) || lastDamageSource.is(DamageTypes.ON_FIRE) || lastDamageSource.is(DamageTypes.CAMPFIRE)) {
                return AnimationsId.DEATH_BURN_ANIMATION.getAnimationId();
            } else if (lastDamageSource.is(DamageTypes.EXPLOSION) || lastDamageSource.is(DamageTypes.PLAYER_EXPLOSION)) {
                return AnimationsId.DEATH_EXPLOSION_ANIMATION.getAnimationId();
            } else if (lastDamageSource.is(DamageTypes.DROWN)) {
                return AnimationsId.DEATH_DROWN_ANIMATION.getAnimationId();
            }
        }
        return AnimationsId.DEATH_DEFAULT_ANIMATION.getAnimationId();
    }
}
