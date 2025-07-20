package com.github.razorplay01.customplayeranimation.animation.animations.base;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;

public class DeathAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.deathAnimations.isEnabled()) {
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
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.deathAnimations.deathBurnAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.deathAnimations.deathBurnAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.deathAnimations.deathBurnAnimationConfig.getPriority());
            }
            case "death_explosion" -> {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.deathAnimations.deathExplosionAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.deathAnimations.deathExplosionAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.deathAnimations.deathExplosionAnimationConfig.getPriority());
            }
            case "death_drown" -> {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.deathAnimations.deathDrownAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.deathAnimations.deathDrownAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.deathAnimations.deathDrownAnimationConfig.getPriority());
            }
            default -> {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.deathAnimations.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.deathAnimations.getFadeTime());
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.deathAnimations.getPriority());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().getHealth() <= 0;
    }

    private String getDeathAnimationId(AnimationContext context) {
        DamageSource lastDamageSource = context.player().getLastDamageSource();
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
