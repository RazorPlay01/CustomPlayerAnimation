package com.github.razorplay01.cpa.animation.animations.overlay;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.enums.Modifiers;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.world.item.ShovelItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.cpa.util.Util.*;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class ShovelAnimation implements ICustomAnimation {
    private final Map<UUID, Boolean> animationsInProgress = new HashMap<>();
    private final Map<UUID, Long> animationStartTimes = new HashMap<>();
    private final Map<UUID, Float> animationDurations = new HashMap<>();

    @Override
    public void playAnimation(AnimationContext context) {
        UUID uuid = context.player().getUUID();
        if (!CONFIG.getOverlayAnimations().toolsAnimations.shovelAnimationsConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
            animationsInProgress.put(uuid, false);
        } else {
            // Si el jugador está balanceando la pala o la animación está en progreso
            if (context.player().swinging &&
                    isShovel(context.player().getMainHandItem()) &&
                    context.player().getMainHandItem().getItem() instanceof ShovelItem &&
                    context.player().swingingArm.equals(MAIN_HAND)) {

                // Iniciar una nueva animación
                animationStartTimes.put(uuid, context.player().level().getGameTime());
                animationsInProgress.put(uuid, true);

                // Obtener la duración de la animación
                com.zigythebird.playeranimcore.animation.Animation animation = context.player().isCrouching() ?
                        getAnimation(AnimationsId.SHOVEL_SNEAK_ANIMATION.getAnimationId()) :
                        getAnimation(AnimationsId.SHOVEL_ANIMATION.getAnimationId());
                animationDurations.put(uuid, animation.length() - 4);

                configureAnimationContainer(CONFIG.getOverlayAnimations().toolsAnimations.shovelAnimationsConfig, context.overlayAnimationContainer());
                context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? getAnimation(AnimationsId.SHOVEL_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.SHOVEL_ANIMATION.getAnimationId()));
                context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AnimationsId.SHOVEL_SNEAK_ANIMATION.getAnimationId() : AnimationsId.SHOVEL_ANIMATION.getAnimationId());
                ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (context.playerData().getRightHand() != MAIN_HAND);
            } else if (Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) {
                // Continuar la animación si no ha pasado el tiempo mínimo
                long currentTime = context.player().level().getGameTime();
                if (currentTime - animationStartTimes.getOrDefault(uuid, 0L) < animationDurations.getOrDefault(uuid, 0f)) {
                    configureAnimationContainer(CONFIG.getOverlayAnimations().toolsAnimations.shovelAnimationsConfig, context.overlayAnimationContainer());
                    context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? getAnimation(AnimationsId.SHOVEL_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.SHOVEL_ANIMATION.getAnimationId()));
                    context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AnimationsId.SHOVEL_SNEAK_ANIMATION.getAnimationId() : AnimationsId.SHOVEL_ANIMATION.getAnimationId());
                    ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (context.playerData().getRightHand() != MAIN_HAND);
                } else {
                    // La animación ha terminado
                    animationsInProgress.put(uuid, false);
                }
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        UUID uuid = context.player().getUUID();
        // Reproducir la animación si el jugador está balanceando la pala o si una animación está en progreso
        return ((context.player().swinging &&
                isShovel(context.player().getMainHandItem()) &&
                context.player().getMainHandItem().getItem() instanceof ShovelItem &&
                context.player().swingingArm.equals(MAIN_HAND)) || Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) &&
                !context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }
}