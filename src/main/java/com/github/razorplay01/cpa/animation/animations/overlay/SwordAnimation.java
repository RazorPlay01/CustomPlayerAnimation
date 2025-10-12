package com.github.razorplay01.cpa.animation.animations.overlay;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.enums.Modifiers;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.*;
import static com.github.razorplay01.cpa.util.Util.*;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class SwordAnimation implements ICustomAnimation {
    private int currentComboCount = 0;
    private long lastSwingTick = 0;
    private final int COMBO_RESET_TICKS = 50;

    // Variable para rastrear si una animación está en progreso
    private boolean isAnimationInProgress = false;
    private long animationStartTime = 0;
    private float animationDuration = 0; // Duración de la animación en ticks (se calculará dinámicamente)

    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getOverlayAnimations().swordAnimations.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
            isAnimationInProgress = false;
        } else {
            // Si el jugador está balanceando el arma o la animación está en progreso
            if (isPlayerSwingingWeapon(context.player())) {
                // Iniciar una nueva animación
                animationStartTime = context.player().level().getGameTime();
                isAnimationInProgress = true;
                handleSwordComboAnimation(context);

                // Obtener la duración de la animación actual después de seleccionarla
                if (context.overlayAnimationContainer().getCurrentAnimation() != null) {
                    animationDuration = context.overlayAnimationContainer().getCurrentAnimation().length() - 5;
                }
            } else if (isAnimationInProgress) {
                // Continuar la animación si no ha pasado el tiempo mínimo
                long currentTime = context.player().level().getGameTime();
                if (currentTime - animationStartTime < animationDuration) {
                    handleSwordComboAnimation(context);
                } else {
                    // La animación ha terminado
                    isAnimationInProgress = false;
                }
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        // Reproducir la animación si el jugador está balanceando el arma o si una animación está en progreso
        return (isPlayerSwingingWeapon(context.player()) || isAnimationInProgress) &&
                !context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }

    private void handleSwordComboAnimation(AnimationContext context) {
        long currentTick = context.player().level().getGameTime();
        if (currentTick - lastSwingTick > COMBO_RESET_TICKS) {
            currentComboCount = 0;
        }

        if (context.overlayAnimationContainer().getAnimationController().getCurrentAnimation() != null) {
            if (context.overlayAnimationContainer().getAnimationController().isActive() &&
                    context.overlayAnimationContainer().getAnimationController().getCurrentAnimation().animation() == getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId())) {
                currentComboCount = (currentComboCount % 3) + 1;
            }

            lastSwingTick = currentTick;
            switch (currentComboCount) {
                case 2 -> {
                    context.overlayAnimationContainer().setAnimationSpeed(CONFIG.getOverlayAnimations().swordAnimations.swordAttack2AnimationConfig.getSpeedMultiplier());
                    context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.getOverlayAnimations().swordAnimations.swordAttack2AnimationConfig.getFadeTime());
                    context.overlayAnimationContainer().setAnimationPriority(CONFIG.getOverlayAnimations().swordAnimations.swordAttack2AnimationConfig.getPriority());
                }
                case 3 -> {
                    context.overlayAnimationContainer().setAnimationSpeed(CONFIG.getOverlayAnimations().swordAnimations.swordAttack3AnimationConfig.getSpeedMultiplier());
                    context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.getOverlayAnimations().swordAnimations.swordAttack3AnimationConfig.getFadeTime());
                    context.overlayAnimationContainer().setAnimationPriority(CONFIG.getOverlayAnimations().swordAnimations.swordAttack3AnimationConfig.getPriority());
                }
                default -> {
                    context.overlayAnimationContainer().setAnimationSpeed(CONFIG.getOverlayAnimations().swordAnimations.swordAttack1AnimationConfig.getSpeedMultiplier());
                    context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.getOverlayAnimations().swordAnimations.swordAttack1AnimationConfig.getFadeTime());
                    context.overlayAnimationContainer().setAnimationPriority(CONFIG.getOverlayAnimations().swordAnimations.swordAttack1AnimationConfig.getPriority());
                }
            }

            selectComboAnimation(context);
            ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (context.playerData().getRightHand() != MAIN_HAND);
        }
    }

    private void selectComboAnimation(AnimationContext context) {
        boolean isSneaking = context.player().isCrouching();

        switch (currentComboCount) {
            case 1 -> {
                context.overlayAnimationContainer().setCurrentAnimation(isSneaking ? getAnimation(AnimationsId.SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.SWORD_ATTACK_1_ANIMATION.getAnimationId()));
                context.overlayAnimationContainer().setCurrentAnimationId(isSneaking ? AnimationsId.SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId() : AnimationsId.SWORD_ATTACK_1_ANIMATION.getAnimationId());
            }
            case 2 -> {
                context.overlayAnimationContainer().setCurrentAnimation(isSneaking ? getAnimation(AnimationsId.SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.SWORD_ATTACK_2_ANIMATION.getAnimationId()));
                context.overlayAnimationContainer().setCurrentAnimationId(isSneaking ? AnimationsId.SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimationId() : AnimationsId.SWORD_ATTACK_2_ANIMATION.getAnimationId());
            }
            default -> {
                context.overlayAnimationContainer().setCurrentAnimation(isSneaking ? getAnimation(AnimationsId.SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.SWORD_ATTACK_3_ANIMATION.getAnimationId()));
                context.overlayAnimationContainer().setCurrentAnimationId(isSneaking ? AnimationsId.SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimationId() : AnimationsId.SWORD_ATTACK_3_ANIMATION.getAnimationId());
            }
        }
    }

    public static boolean isPlayerSwingingWeapon(AbstractClientPlayer player) {
        ItemStack itemStack = player.getMainHandItem();
        if (isAxe(itemStack) || isPickaxe(itemStack) || isShovel(itemStack)) return false;
        return player.swinging &&
                (isSword(itemStack) || itemStack.getItem() instanceof TridentItem) &&
                player.swingingArm.equals(MAIN_HAND);
    }
}
