package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.enums.Modifiers;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.core.component.DataComponents;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.*;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class PickaxeAnimation implements ICustomAnimation {
    // Variable para rastrear si una animación está en progreso
    private static boolean isAnimationInProgress = false;
    private static long animationStartTime = 0;
    private static float animationDuration = 0; // Duración de la animación en ticks (se calculará dinámicamente)

    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.toolsAnimations.pickaxeAnimationsConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
            isAnimationInProgress = false;
        } else {
            // Si el jugador está balanceando el pico o la animación está en progreso
            if (context.player().swinging &&
                    isPickaxe(context.player().getMainHandItem()) &&
                    context.player().getMainHandItem().getItem().getDefaultInstance().getComponents().has(DataComponents.TOOL) &&
                    context.player().swingingArm.equals(MAIN_HAND)) {

                // Iniciar una nueva animación
                Animation animation = context.player().isCrouching() ?
                        getAnimation(AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId()) :
                        getAnimation(AnimationsId.PICKAXE_ANIMATION.getAnimationId());

                animationDuration = animation.length() - 3; // Obtener la duración real de la animación
                animationStartTime = context.player().level().getGameTime();
                isAnimationInProgress = true;

                configureAnimationContainer(CONFIG.toolsAnimations.pickaxeAnimationsConfig, context.overlayAnimationContainer());
                context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? getAnimation(AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.PICKAXE_ANIMATION.getAnimationId()));
                context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId() : AnimationsId.PICKAXE_ANIMATION.getAnimationId());
                ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (context.playerData().getRightHand() != MAIN_HAND);
            } else if (isAnimationInProgress) {
                // Continuar la animación si no ha pasado el tiempo mínimo
                long currentTime = context.player().level().getGameTime();
                if (currentTime - animationStartTime < animationDuration) {
                    configureAnimationContainer(CONFIG.toolsAnimations.pickaxeAnimationsConfig, context.overlayAnimationContainer());
                    context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? getAnimation(AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.PICKAXE_ANIMATION.getAnimationId()));
                    context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId() : AnimationsId.PICKAXE_ANIMATION.getAnimationId());
                    ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (context.playerData().getRightHand() != MAIN_HAND);
                } else {
                    // La animación ha terminado
                    isAnimationInProgress = false;
                }
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        // Reproducir la animación si el jugador está balanceando el pico o si una animación está en progreso
        return ((context.player().swinging &&
                isPickaxe(context.player().getMainHandItem()) &&
                context.player().getMainHandItem().getItem().getDefaultInstance().getComponents().has(DataComponents.TOOL) &&
                context.player().swingingArm.equals(MAIN_HAND)) || isAnimationInProgress) &&
                !context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }
}
