package com.github.razorplay01.cpa.animation.animations.overlay;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.enums.BodyParts;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.easing.EasingType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.cpa.util.Util.disableBothArms;

public class CrossbowAnimation implements ICustomAnimation {
    // Track if arms were disabled in previous tick
    private boolean wasMainArmDisabled = false;
    private boolean wasOffArmDisabled = false;
    private ItemStack lastMainHandItem = ItemStack.EMPTY;
    private ItemStack lastOffHandItem = ItemStack.EMPTY;

    // Track if we need to force enable arms in the next tick
    private boolean needToForceEnableMainArm = false;
    private boolean needToForceEnableOffArm = false;
    private BodyParts mainArmToEnable = null;
    private BodyParts offArmToEnable = null;

    @Override
    public void playAnimation(AnimationContext context) {
        // Primero, verificar si necesitamos forzar la habilitación de brazos desde el tick anterior
        if (needToForceEnableMainArm && mainArmToEnable != null) {
            forceEnableBodyPart(context, mainArmToEnable);
            needToForceEnableMainArm = false;
            mainArmToEnable = null;
        }

        if (needToForceEnableOffArm && offArmToEnable != null) {
            forceEnableBodyPart(context, offArmToEnable);
            needToForceEnableOffArm = false;
            offArmToEnable = null;
        }

        // Handle charging (both arms disabled)
        if (context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof CrossbowItem) {
            disableBothArms(context);
            wasMainArmDisabled = true;
            wasOffArmDisabled = true;
            return; // Skip pose checks during charging
        }

        // Track current items
        ItemStack currentMainHandItem = context.player().getMainHandItem();
        ItemStack currentOffHandItem = context.player().getOffhandItem();

        // Handle holding (disable only the arm holding the crossbow)
        if (context.playerData().getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD)) {
            context.player().disableBodyPartAnimationInAllContainers(
                    context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM
            );
            wasMainArmDisabled = true;
        } else if (wasMainArmDisabled &&
                (!(currentMainHandItem.getItem() instanceof CrossbowItem) ||
                        !currentMainHandItem.equals(lastMainHandItem))) {
            // Re-enable main arm if it was disabled and crossbow is no longer held
            wasMainArmDisabled = false;
            // Programar la reactivación del brazo principal para el próximo tick
            needToForceEnableMainArm = true;
            mainArmToEnable = context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM;
            // También forzar la habilitación inmediata
            forceEnableBodyPart(context, mainArmToEnable);
        }

        if (context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD)) {
            context.player().disableBodyPartAnimationInAllContainers(
                    context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM
            );
            wasOffArmDisabled = true;
        } else if (wasOffArmDisabled &&
                (!(currentOffHandItem.getItem() instanceof CrossbowItem) ||
                        !currentOffHandItem.equals(lastOffHandItem))) {
            // Re-enable off arm if it was disabled and crossbow is no longer held
            wasOffArmDisabled = false;
            // Programar la reactivación del brazo secundario para el próximo tick
            needToForceEnableOffArm = true;
            offArmToEnable = context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM;
            // También forzar la habilitación inmediata
            forceEnableBodyPart(context, offArmToEnable);
        }

        // Update last items
        lastMainHandItem = currentMainHandItem.copy();
        lastOffHandItem = currentOffHandItem.copy();
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        // Always check to handle arm re-enabling even when not holding crossbow anymore
        boolean holdingCrossbow = context.player().getMainHandItem().getItem() instanceof CrossbowItem ||
                context.player().getOffhandItem().getItem() instanceof CrossbowItem;

        // Also return true if we need to re-enable arms that were previously disabled
        return holdingCrossbow || wasMainArmDisabled || wasOffArmDisabled;
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
    private void updateAnimationController(com.github.razorplay01.cpa.animation.AnimationContainer container, String partIdToEnable) {
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
     * Esto ayuda a resolver el problema cuando el brazo queda deshabilitado al desequipar la ballesta
     * Guarda las animaciones actuales, aplica una animación en blanco y luego restaura las originales
     */
    private void forceAnimationChange(AnimationContext context) {
        // Guardar las animaciones actuales de cada contenedor
        Animation currentMainAnimation = context.mainAnimationContainer().getAnimationController().getCurrentAnimation().animation();
        Animation currentOverlayAnimation = context.overlayAnimationContainer().getAnimationController().getCurrentAnimation().animation();
        Animation currentSpecialAnimation = context.specialAnimationContainer().getAnimationController().getCurrentAnimation().animation();
        
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
        // Solo restaurar si la animación original no era nula
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
