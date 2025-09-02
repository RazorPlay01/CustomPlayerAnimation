package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.enums.Modifiers;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;

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
                currentComboCount = (currentComboCount % 3) + 1;
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
            ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (context.playerData().getRightHand() != MAIN_HAND);
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
        return player.swinging &&
                (isSword(player.getMainHandItem()) || player.getMainHandItem().getItem() instanceof TridentItem) &&
                player.swingingArm.equals(MAIN_HAND);
    }

    public static boolean isSword(ItemStack itemStack) {
        Weapon weapon = itemStack.get(DataComponents.WEAPON);
        if (weapon != null) {
            // Opcionalmente, verifica los modificadores de atributos para confirmar que es una espada
            ItemAttributeModifiers attributes = itemStack.get(DataComponents.ATTRIBUTE_MODIFIERS);
            if (attributes != null) {
                for (ItemAttributeModifiers.Entry entry : attributes.modifiers()) {
                    if (entry.attribute().equals(Attributes.ATTACK_DAMAGE)) {
                        return true; // Es probable que sea una espada
                    }
                }
            }
            return true; // Si tiene el componente WEAPON, es una espada u otra arma cuerpo a cuerpo
        }
        return false;
    }
}
