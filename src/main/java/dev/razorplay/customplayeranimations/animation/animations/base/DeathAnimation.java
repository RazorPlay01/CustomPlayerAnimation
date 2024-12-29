package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class DeathAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        /*if (!CONFIG.deathAnimations.deathAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {*/
        // Determina la animación según la causa de la muerte
        String animationId = AnimationsId.RUN_ANIMATION.getAnimationId(); //getDeathAnimationId(context);

        // Configura la animación seleccionada
        context.mainAnimationContainer().setAnimationSpeed(1);
        context.mainAnimationContainer().setAnimationFadeTime(10);
        context.mainAnimationContainer().setAnimationPriority(0);

        context.mainAnimationContainer().setCurrentAnimation(getAnimation(animationId));
        context.mainAnimationContainer().setCurrentAnimationId(animationId);
        //}
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().getHealth() <= 0;
    }

    /*private String getDeathAnimationId(AnimationContext context) {
        // Obtener la última fuente de daño (causa de muerte)
        DamageSource lastDamageSource = context.player().getLastDamageSource();

        if (lastDamageSource != null) {
            if (lastDamageSource.is(DamageTypes.IN_FIRE) || lastDamageSource.is(DamageTypes.ON_FIRE)) {
                return AnimationsId.DEATH_BURN_ANIMATION.getAnimationId(); // Muerte por fuego
            } else if (lastDamageSource.isExplosive()) {
                return AnimationsId.DEATH_EXPLOSION_ANIMATION.getAnimationId(); // Muerte por explosión
            } else if (lastDamageSource.isMagic()) {
                return AnimationsId.DEATH_MAGIC_ANIMATION.getAnimationId(); // Muerte por magia
            } else if (lastDamageSource.isProjectile()) {
                return AnimationsId.DEATH_PROJECTILE_ANIMATION.getAnimationId(); // Muerte por proyectil
            } else if (lastDamageSource == DamageSource.DROWN) {
                return AnimationsId.DEATH_DROWN_ANIMATION.getAnimationId(); // Muerte por ahogamiento
            } else if (lastDamageSource == DamageSource.FALL) {
                return AnimationsId.DEATH_FALL_ANIMATION.getAnimationId(); // Muerte por caída
            }
        }
        // Animación predeterminada si no se reconoce la causa
        return AnimationsId.DEATH_DEFAULT_ANIMATION.getAnimationId();
    }*/
}
