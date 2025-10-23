package com.github.razorplay01.cpa.animation.animations.overlay;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.enums.Modifiers;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.world.item.AxeItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.cpa.util.Util.*;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class AxeAnimation implements ICustomAnimation {
    private final Map<UUID, Boolean> animationsInProgress = new HashMap<>();
    private final Map<UUID, Long> animationStartTimes = new HashMap<>();
    private final Map<UUID, Float> animationDurations = new HashMap<>();

    @Override
    public void playAnimation(AnimationContext context) {
        UUID uuid = context.avatar().getUUID();
        if (!CONFIG.getOverlayAnimations().toolsAnimations.axeAnimationsConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
            animationsInProgress.put(uuid, false);
        } else {
            // Si el jugador está usando el hacha o la animación está en progreso
            if (context.avatar().swinging &&
                    isAxe(context.avatar().getMainHandItem()) &&
                    context.avatar().getMainHandItem().getItem() instanceof AxeItem &&
                    context.avatar().swingingArm.equals(MAIN_HAND)) {

                // Iniciar una nueva animación
                animationStartTimes.put(uuid, context.avatar().level().getGameTime());
                animationsInProgress.put(uuid, true);

                // Obtener la duración de la animación
                Animation animation = context.avatar().isCrouching() ?
                        getAnimation(AnimationsId.AXE_SNEAK_ANIMATION.getAnimationId()) :
                        getAnimation(AnimationsId.AXE_ANIMATION.getAnimationId());
                animationDurations.put(uuid, animation.length() - 3);

                configureAnimationContainer(CONFIG.getOverlayAnimations().toolsAnimations.axeAnimationsConfig, context.overlayAnimationContainer());
                context.overlayAnimationContainer().setCurrentAnimation(context.avatar().isCrouching() ? getAnimation(AnimationsId.AXE_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.AXE_ANIMATION.getAnimationId()));
                context.overlayAnimationContainer().setCurrentAnimationId(context.avatar().isCrouching() ? AnimationsId.AXE_SNEAK_ANIMATION.getAnimationId() : AnimationsId.AXE_ANIMATION.getAnimationId());
                ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (context.playerData().getRightHand() != MAIN_HAND);
            } else if (Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) {
                // Continuar la animación si no ha pasado el tiempo mínimo
                long currentTime = context.avatar().level().getGameTime();
                if (currentTime - animationStartTimes.getOrDefault(uuid, 0L) < animationDurations.getOrDefault(uuid, 0f)) {
                    configureAnimationContainer(CONFIG.getOverlayAnimations().toolsAnimations.axeAnimationsConfig, context.overlayAnimationContainer());
                    context.overlayAnimationContainer().setCurrentAnimation(context.avatar().isCrouching() ? getAnimation(AnimationsId.AXE_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.AXE_ANIMATION.getAnimationId()));
                    context.overlayAnimationContainer().setCurrentAnimationId(context.avatar().isCrouching() ? AnimationsId.AXE_SNEAK_ANIMATION.getAnimationId() : AnimationsId.AXE_ANIMATION.getAnimationId());
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
        UUID uuid = context.avatar().getUUID();
        // Reproducir la animación si el jugador está balanceando el hacha o si una animación está en progreso
        return ((context.avatar().swinging &&
                isAxe(context.avatar().getMainHandItem()) &&
                context.avatar().getMainHandItem().getItem() instanceof AxeItem &&
                context.avatar().swingingArm.equals(MAIN_HAND)) || Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) &&
                !context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }
}