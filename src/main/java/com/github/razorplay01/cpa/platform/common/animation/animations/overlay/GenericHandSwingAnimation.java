package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.BodyParts;
import com.github.razorplay01.cpa.platform.common.util.interfaces.IAnimationControl;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.easing.EasingType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;

public class GenericHandSwingAnimation implements ICustomAnimation {
    // Rastrear si el brazo estaba deshabilitado en el tick anterior
    private boolean wasArmDisabled = false;
    private InteractionHand lastSwingingArm = null;

    // Rastrear si necesitamos forzar la habilitación del brazo en el próximo tick
    private boolean needToForceEnableArm = false;
    private BodyParts armToEnable = null;

    @Override
    public void playAnimation(AnimationContext context) {
        // Primero, verificar si necesitamos forzar la habilitación del brazo desde el tick anterior
        if (needToForceEnableArm && armToEnable != null) {
            forceEnableBodyPart(context, armToEnable);
            needToForceEnableArm = false;
            armToEnable = null;
        }

        // Si el jugador está golpeando, deshabilitar el brazo correspondiente
        if (context.player().swinging) {
            disableArmBasedOnHand(context, context.player().swingingArm);
            wasArmDisabled = true;
            lastSwingingArm = context.player().swingingArm;
        } else if (wasArmDisabled && !context.player().swinging) {
            // Si el jugador ya no está golpeando pero el brazo estaba deshabilitado
            wasArmDisabled = false;
            // Programar la reactivación del brazo para el próximo tick
            needToForceEnableArm = true;
            armToEnable = lastSwingingArm == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM;
            // También forzar la habilitación inmediata
            forceEnableBodyPart(context, armToEnable);
        }

        context.overlayAnimationContainer().setCurrentAnimationId("hand_swing" + context.overlayAnimationContainer().getCurrentAnimationId());
        context.overlayAnimationContainer().setAnimationFadeTime(0);
        context.overlayAnimationContainer().setAnimationPriority(0);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        // Siempre verificar para manejar la reactivación del brazo incluso cuando ya no está golpeando
        boolean isSwinging = context.player().swinging &&
                !(CONFIG.getOverlayAnimations().swordAnimations.isEnabled() && context.player().getMainHandItem().getItem().getDefaultInstance().getComponents().has(DataComponents.WEAPON) || context.player().getMainHandItem().getItem() instanceof TridentItem) &&
                !(CONFIG.getOverlayAnimations().toolsAnimations.axeAnimationsConfig.isEnabled() && context.player().getMainHandItem().getItem() instanceof AxeItem) &&
                !(CONFIG.getOverlayAnimations().toolsAnimations.pickaxeAnimationsConfig.isEnabled() && context.player().getMainHandItem().getItem().getDefaultInstance().getComponents().has(DataComponents.TOOL)) &&
                !(CONFIG.getOverlayAnimations().toolsAnimations.shovelAnimationsConfig.isEnabled() && context.player().getMainHandItem().getItem() instanceof ShovelItem);

        // También devolver true si necesitamos reactivar un brazo que estaba previamente deshabilitado
        return isSwinging || wasArmDisabled || needToForceEnableArm;
    }

    private static void disableArmBasedOnHand(AnimationContext context, InteractionHand hand) {
		context.iAnimationControl().disableBodyPartAnimation(context.mainAnimationContainer(), hand == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
		context.iAnimationControl().disableBodyPartAnimation(context.overlayAnimationContainer(), hand == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
		context.iAnimationControl().disableBodyPartAnimation(context.specialAnimationContainer(), hand == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
    }

    /**
     * Método para forzar la reactivación de una parte del cuerpo específica
     * Esto es necesario porque el método enableAllBodyPartsInAllContainers se llama al inicio del tick
     * pero luego las animaciones pueden desactivar partes del cuerpo nuevamente
     */
    private void forceEnableBodyPart(AnimationContext context, BodyParts bodyPart) {
        // Eliminar la parte del cuerpo de la lista de partes desactivadas en todos los contenedores
        String partIdToEnable = bodyPart.getPartId();
        context.mainAnimationContainer().getDisabledBoneIds().remove(partIdToEnable);
        context.overlayAnimationContainer().getDisabledBoneIds().remove(partIdToEnable);
        context.specialAnimationContainer().getDisabledBoneIds().remove(partIdToEnable);

        // Forzar un cambio de animación para asegurar que se actualice el estado del brazo
        forceAnimationChange(context);

        // Actualizar los controladores de animación para reflejar los cambios inmediatamente
        // y asegurarse de que la parte del cuerpo esté habilitada incluso después de applyDisables()
        updateAnimationController(context.mainAnimationContainer(), partIdToEnable);
        updateAnimationController(context.overlayAnimationContainer(), partIdToEnable);
        updateAnimationController(context.specialAnimationContainer(), partIdToEnable);
    }

    /**
     * Actualiza el controlador de animación para asegurar que la parte del cuerpo especificada
     * esté habilitada, incluso después de que se apliquen otras deshabilitaciones
     */
    private void updateAnimationController(AnimationContainer container, String partIdToEnable) {
        container.getAnimationController().setPostAnimationSetupConsumer(getBoneFunc -> {
            // Primero habilitar explícitamente la parte del cuerpo que queremos reactivar
            getBoneFunc.apply(partIdToEnable).setEnabled(true);

            // Luego desactivar solo las partes que deben permanecer desactivadas
            for (String boneId : container.getDisabledBoneIds()) {
                // Verificar que no estamos desactivando la parte que acabamos de habilitar
                if (!boneId.equals(partIdToEnable)) {
                    getBoneFunc.apply(boneId).setEnabled(false);
                }
            }
        });
    }

    /**
     * Fuerza un cambio de animación para asegurar que se actualice el estado del brazo
     * Esto ayuda a resolver el problema cuando el brazo queda deshabilitado al dejar de golpear
     * Guarda las animaciones actuales, aplica una animación en blanco y luego restaura las originales
     */
    private void forceAnimationChange(AnimationContext context) {
        // Guardar las animaciones actuales de cada contenedor, con chequeo de null
        Animation currentMainAnimation = null;
        var mainQueued = context.mainAnimationContainer().getAnimationController().getCurrentAnimation();
        if (mainQueued != null) {
            currentMainAnimation = mainQueued.animation();
        }

        Animation currentOverlayAnimation = null;
        var overlayQueued = context.overlayAnimationContainer().getAnimationController().getCurrentAnimation();
        if (overlayQueued != null) {
            currentOverlayAnimation = overlayQueued.animation();
        }

        Animation currentSpecialAnimation = null;
        var specialQueued = context.specialAnimationContainer().getAnimationController().getCurrentAnimation();
        if (specialQueued != null) {
            currentSpecialAnimation = specialQueued.animation();
        }

        // Crear una animación en blanco para resetear el estado
        RawAnimation blankAnimation =
                RawAnimation.begin().thenPlay(getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()));

        // Aplicar la animación en blanco con una transición muy rápida
        context.mainAnimationContainer().getAnimationController().replaceAnimationWithFade(
                AbstractFadeModifier.standardFadeIn(1, EasingType.EASE_IN_OUT_SINE),
                blankAnimation, false
        );

        context.overlayAnimationContainer().getAnimationController().replaceAnimationWithFade(
                AbstractFadeModifier.standardFadeIn(1, EasingType.EASE_IN_OUT_SINE),
                blankAnimation, false
        );

        context.specialAnimationContainer().getAnimationController().replaceAnimationWithFade(
                AbstractFadeModifier.standardFadeIn(1, EasingType.EASE_IN_OUT_SINE),
                blankAnimation, false
        );

        // Restaurar las animaciones originales después de un breve retraso
        // Solo restaurar si la animación original no era null
        if (currentMainAnimation != null) {
            context.mainAnimationContainer().getAnimationController().replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(2, EasingType.EASE_IN_OUT_SINE),
                    currentMainAnimation, false
            );
        }

        if (currentOverlayAnimation != null) {
            context.overlayAnimationContainer().getAnimationController().replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(2, EasingType.EASE_IN_OUT_SINE),
                    currentOverlayAnimation, false
            );
        }

        if (currentSpecialAnimation != null) {
            context.specialAnimationContainer().getAnimationController().replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(2, EasingType.EASE_IN_OUT_SINE),
                    currentSpecialAnimation, false
            );
        }
    }
}
