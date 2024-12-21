package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.util.enums.ArmsEnum;
import dev.razorplay.customplayeranimations.util.enums.ModifiersEnum;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import org.spongepowered.asm.mixin.Unique;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.*;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class SwordAnimation {
    private static int currentComboCount = 0;
    private static long lastSwingTick = 0;
    private static final int COMBO_RESET_TICKS = 50;

    private SwordAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().swinging && (context.player().getMainHandItem().getItem() instanceof SwordItem || context.player().getMainHandItem().getItem() instanceof TridentItem) && context.player().swingingArm.equals(MAIN_HAND)) {
            if (!CONFIG.swordAnimations.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
            } else {
                handleSwordComboAnimation(context);
            }
        }
    }

    private static void handleSwordComboAnimation(AnimationContext context) {
        long currentTick = context.player().level().getGameTime();
        if (currentTick - lastSwingTick > COMBO_RESET_TICKS) {
            currentComboCount = 0;
        }

        if (context.overlayAnimationContainer().getAnimationModifierLayer().getAnimation().isActive() &&
                ((KeyframeAnimationPlayer) context.overlayAnimationContainer().getAnimationModifierLayer().getAnimation()).getData().getName().equalsIgnoreCase(BLANK_LOOP_ANIMATION.animationId())) {
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

        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(ModifiersEnum.MIRROR_MODIFIER.getModifierId())).setEnabled(context.playerData().getRightHand() != MAIN_HAND);

        selectComboAnimation(context);
    }

    private static void selectComboAnimation(AnimationContext context) {
        boolean isSneaking = context.player().isCrouching();

        switch (currentComboCount) {
            case 1 -> {
                context.overlayAnimationContainer().setCurrentAnimation(isSneaking ? SWORD_ATTACK_1_SNEAK_ANIMATION.animation() : SWORD_ATTACK_1_ANIMATION.animation());
                context.overlayAnimationContainer().setCurrentAnimationId(isSneaking ? SWORD_ATTACK_1_SNEAK_ANIMATION.animationId() : SWORD_ATTACK_1_ANIMATION.animationId());
            }
            case 2 -> {
                context.overlayAnimationContainer().setCurrentAnimation(isSneaking ? SWORD_ATTACK_2_SNEAK_ANIMATION.animation() : SWORD_ATTACK_2_ANIMATION.animation());
                context.overlayAnimationContainer().setCurrentAnimationId(isSneaking ? SWORD_ATTACK_2_SNEAK_ANIMATION.animationId() : SWORD_ATTACK_2_ANIMATION.animationId());
            }
            default -> {
                context.overlayAnimationContainer().setCurrentAnimation(isSneaking ? SWORD_ATTACK_3_SNEAK_ANIMATION.animation() : SWORD_ATTACK_3_ANIMATION.animation());
                context.overlayAnimationContainer().setCurrentAnimationId(isSneaking ? SWORD_ATTACK_3_SNEAK_ANIMATION.animationId() : SWORD_ATTACK_3_ANIMATION.animationId());
            }
        }
    }
}
