package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.enums.Modifiers;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.TridentItem;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.*;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class SwordAnimation implements ICustomAnimation {
    private static int currentComboCount = 0;
    private static long lastSwingTick = 0;
    private static final int COMBO_RESET_TICKS = 50;

    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.swordAnimations.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
        } else {
            handleSwordComboAnimation(context);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return isPlayerSwingingWeapon(context.player()) && !context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }

    private static void handleSwordComboAnimation(AnimationContext context) {
        long currentTick = context.player().level().getGameTime();
        if (currentTick - lastSwingTick > COMBO_RESET_TICKS) {
            currentComboCount = 0;
        }

        if (context.overlayAnimationContainer().getAnimationController().getCurrentAnimation() != null) {
            if (context.overlayAnimationContainer().getAnimationController().isActive() &&
                    context.overlayAnimationContainer().getAnimationController().getCurrentAnimation().animation() == getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId())) {
                if (currentComboCount < 2) {
                    currentComboCount++;
                } else {
                    currentComboCount = 0;
                }
            }

            lastSwingTick = currentTick;
            switch (currentComboCount) {
                case 2 -> {
                    context.overlayAnimationContainer().setAnimationSpeed(CONFIG.swordAnimations.swordAttack2AnimationConfig.getSpeedMultiplier());
                    context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.swordAnimations.swordAttack2AnimationConfig.getFadeTime());
                    context.overlayAnimationContainer().setAnimationPriority(CONFIG.swordAnimations.swordAttack2AnimationConfig.getPriority());
                }
                case 3 -> {
                    context.overlayAnimationContainer().setAnimationSpeed(CONFIG.swordAnimations.swordAttack3AnimationConfig.getSpeedMultiplier());
                    context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.swordAnimations.swordAttack3AnimationConfig.getFadeTime());
                    context.overlayAnimationContainer().setAnimationPriority(CONFIG.swordAnimations.swordAttack3AnimationConfig.getPriority());
                }
                default -> {
                    context.overlayAnimationContainer().setAnimationSpeed(CONFIG.swordAnimations.swordAttack1AnimationConfig.getSpeedMultiplier());
                    context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.swordAnimations.swordAttack1AnimationConfig.getFadeTime());
                    context.overlayAnimationContainer().setAnimationPriority(CONFIG.swordAnimations.swordAttack1AnimationConfig.getPriority());
                }
            }

            selectComboAnimation(context);
            //todo: ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(context.playerData().getRightHand() != MAIN_HAND);
        }
    }

    private static void selectComboAnimation(AnimationContext context) {
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
        return player.swinging && (player.getMainHandItem().getItem().getDefaultInstance().getComponents().has(DataComponents.WEAPON) || player.getMainHandItem().getItem() instanceof TridentItem) && player.swingingArm.equals(MAIN_HAND);
    }
}
