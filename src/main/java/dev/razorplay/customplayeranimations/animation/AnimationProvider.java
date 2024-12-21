package dev.razorplay.customplayeranimations.animation;

import dev.razorplay.customplayeranimations.CustomPlayerAnimations;
import dev.razorplay.customplayeranimations.util.enums.AnimationsIdEnum;
import dev.razorplay.customplayeranimations.util.records.Animation;
import net.minecraft.resources.ResourceLocation;

public class AnimationProvider {
    private AnimationProvider() {
        // []
    }

    public static Animation BLANK_LOOP_ANIMATION =
            new Animation(AnimationsIdEnum.BLANK_LOOP_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.BLANK_LOOP_ANIMATION.getAnimationId())));

    public static final Animation WALK_ANIMATION =
            new Animation(AnimationsIdEnum.WALKING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.WALKING_ANIMATION.getAnimationId())));

    public static final Animation WALKING_BACKWARDS_ANIMATION =
            new Animation(AnimationsIdEnum.WALKING_BACKWARDS_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.WALKING_BACKWARDS_ANIMATION.getAnimationId())));

    public static final Animation WALKING_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.WALKING_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.WALKING_SNEAK_ANIMATION.getAnimationId())));

    public static final Animation WALKING_SNEAK_BACKWARDS_ANIMATION =
            new Animation(AnimationsIdEnum.WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId())));

    public static final Animation RUNNING_ANIMATION =
            new Animation(AnimationsIdEnum.RUNNING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.RUNNING_ANIMATION.getAnimationId())));

    public static final Animation TURN_RIGHT_ANIMATION =
            new Animation(AnimationsIdEnum.TURN_RIGHT_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.TURN_RIGHT_ANIMATION.getAnimationId())));

    public static final Animation TURN_LEFT_ANIMATION =
            new Animation(AnimationsIdEnum.TURN_LEFT_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.TURN_LEFT_ANIMATION.getAnimationId())));

    public static final Animation IDLE_STANDING_ANIMATION =
            new Animation(AnimationsIdEnum.IDLE_STANDING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.IDLE_STANDING_ANIMATION.getAnimationId())));

    public static final Animation IDLE_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.IDLE_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.IDLE_SNEAK_ANIMATION.getAnimationId())));

    public static final Animation IDLE_CREATIVE_FLYING_ANIMATION =
            new Animation(AnimationsIdEnum.IDLE_CREATIVE_FLYING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.IDLE_CREATIVE_FLYING_ANIMATION.getAnimationId())));

    public static final Animation FALLING_ANIMATION =
            new Animation(AnimationsIdEnum.FALLING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.FALLING_ANIMATION.getAnimationId())));

    public static final Animation CLIMBING_ANIMATION =
            new Animation(AnimationsIdEnum.CLIMBING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.CLIMBING_ANIMATION.getAnimationId())));

    public static final Animation CLIMBING_BACKWARDS_ANIMATION =
            new Animation(AnimationsIdEnum.CLIMBING_BACKWARDS_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.CLIMBING_BACKWARDS_ANIMATION.getAnimationId())));

    public static final Animation CLIMBING_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.CLIMBING_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.CLIMBING_SNEAK_ANIMATION.getAnimationId())));

    public static final Animation CLIMBING_IDLE_ANIMATION =
            new Animation(AnimationsIdEnum.CLIMBING_IDLE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.CLIMBING_IDLE_ANIMATION.getAnimationId())));

    public static final Animation CLIMBING_SNEAK_IDLE_ANIMATION =
            new Animation(AnimationsIdEnum.CLIMBING_SNEAK_IDLE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.CLIMBING_SNEAK_IDLE_ANIMATION.getAnimationId())));

    public static final Animation CRAWLING_ANIMATION =
            new Animation(AnimationsIdEnum.CRAWLING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.CRAWLING_ANIMATION.getAnimationId())));

    public static final Animation CRAWLING_BACKWARDS_ANIMATION =
            new Animation(AnimationsIdEnum.CRAWLING_BACKWARDS_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.CRAWLING_BACKWARDS_ANIMATION.getAnimationId())));

    public static final Animation CRAWLING_IDLE_ANIMATION =
            new Animation(AnimationsIdEnum.CRAWLING_IDLE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.CRAWLING_IDLE_ANIMATION.getAnimationId())));

    public static final Animation ELYTRA_ANIMATION =
            new Animation(AnimationsIdEnum.ELYTRA_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.ELYTRA_ANIMATION.getAnimationId())));

    public static final Animation IN_WATER_UP_ANIMATION =
            new Animation(AnimationsIdEnum.IN_WATER_UP_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.IN_WATER_UP_ANIMATION.getAnimationId())));

    public static final Animation IN_WATER_SWIMMING_ANIMATION =
            new Animation(AnimationsIdEnum.IN_WATER_SWIMMING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.IN_WATER_SWIMMING_ANIMATION.getAnimationId())));

    public static final Animation IN_WATER_IDLE_ANIMATION =
            new Animation(AnimationsIdEnum.IN_WATER_IDLE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.IN_WATER_IDLE_ANIMATION.getAnimationId())));

    public static final Animation IN_WATER_FORWARD_ANIMATION =
            new Animation(AnimationsIdEnum.IN_WATER_FORWARD_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.IN_WATER_FORWARD_ANIMATION.getAnimationId())));

    public static final Animation IN_WATER_BACKWARDS_ANIMATION =
            new Animation(AnimationsIdEnum.IN_WATER_BACKWARDS_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.IN_WATER_BACKWARDS_ANIMATION.getAnimationId())));

    public static final Animation MINECART_IDLE_ANIMATION =
            new Animation(AnimationsIdEnum.MINECART_IDLE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.MINECART_IDLE_ANIMATION.getAnimationId())));

    public static final Animation HORSE_IDLE_ANIMATION =
            new Animation(AnimationsIdEnum.HORSE_IDLE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.HORSE_IDLE_ANIMATION.getAnimationId())));

    public static final Animation HORSE_RUNNING_ANIMATION =
            new Animation(AnimationsIdEnum.HORSE_RUNNING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.HORSE_RUNNING_ANIMATION.getAnimationId())));

    public static final Animation SLEEPING_ANIMATION =
            new Animation(AnimationsIdEnum.SLEEPING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SLEEPING_ANIMATION.getAnimationId())));

    public static final Animation BOAT_IDLE_ANIMATION =
            new Animation(AnimationsIdEnum.BOAT_IDLE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.BOAT_IDLE_ANIMATION.getAnimationId())));

    public static final Animation BOAT_TURN_RIGHT_ANIMATION =
            new Animation(AnimationsIdEnum.BOAT_TURN_RIGHT_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.BOAT_TURN_RIGHT_ANIMATION.getAnimationId())));

    public static final Animation BOAT_TURN_LEFT_ANIMATION =
            new Animation(AnimationsIdEnum.BOAT_TURN_LEFT_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.BOAT_TURN_LEFT_ANIMATION.getAnimationId())));

    public static Animation BOAT_FORWARD_ANIMATION =
            new Animation(AnimationsIdEnum.BOAT_FORWARD_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.BOAT_FORWARD_ANIMATION.getAnimationId())));

    public static final Animation ON_FENCE_WALKING_ANIMATION =
            new Animation(AnimationsIdEnum.ON_FENCE_WALKING_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.ON_FENCE_WALKING_ANIMATION.getAnimationId())));

    public static final Animation ON_FENCE_IDLE_ANIMATION =
            new Animation(AnimationsIdEnum.ON_FENCE_IDLE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.ON_FENCE_IDLE_ANIMATION.getAnimationId())));

    public static final Animation ON_EDGE_IDLE_ANIMATION =
            new Animation(AnimationsIdEnum.ON_EDGE_IDLE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.ON_EDGE_IDLE_ANIMATION.getAnimationId())));

    public static final Animation JUMP_ANIMATION =
            new Animation(AnimationsIdEnum.JUMP_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.JUMP_ANIMATION.getAnimationId())));

    // Overlay Animations
    public static final Animation EAT_ANIMATION =
            new Animation(AnimationsIdEnum.EAT_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.EAT_ANIMATION.getAnimationId())));

    public static final Animation PICKAXE_ANIMATION =
            new Animation(AnimationsIdEnum.PICKAXE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.PICKAXE_ANIMATION.getAnimationId())));

    public static final Animation PICKAXE_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.PICKAXE_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.PICKAXE_SNEAK_ANIMATION.getAnimationId())));

    public static final Animation AXE_ANIMATION =
            new Animation(AnimationsIdEnum.AXE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.AXE_ANIMATION.getAnimationId())));

    public static final Animation AXE_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.AXE_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.AXE_SNEAK_ANIMATION.getAnimationId())));

    public static final Animation SHOVEL_ANIMATION =
            new Animation(AnimationsIdEnum.SHOVEL_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SHOVEL_ANIMATION.getAnimationId())));

    public static final Animation SHOVEL_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.SHOVEL_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SHOVEL_SNEAK_ANIMATION.getAnimationId())));

    public static final Animation BOW_IDLE_ANIMATION =
            new Animation(AnimationsIdEnum.BOW_IDLE_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.BOW_IDLE_ANIMATION.getAnimationId())));

    public static final Animation BOW_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.BOW_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.BOW_SNEAK_ANIMATION.getAnimationId())));

    public static final Animation SHIELD_ANIMATION =
            new Animation(AnimationsIdEnum.SHIELD_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SHIELD_ANIMATION.getAnimationId())));

    public static final Animation SHIELD_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.SHIELD_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SHIELD_SNEAK_ANIMATION.getAnimationId())));

    public static final Animation TRIDENT_ANIMATION =
            new Animation(AnimationsIdEnum.TRIDENT_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.TRIDENT_ANIMATION.getAnimationId())));

    // Sword Animations
    public static final Animation SWORD_ATTACK_1_ANIMATION =
            new Animation(AnimationsIdEnum.SWORD_ATTACK_1_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SWORD_ATTACK_1_ANIMATION.getAnimationId())));
    public static final Animation SWORD_ATTACK_1_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId())));
    public static final Animation SWORD_ATTACK_2_ANIMATION =
            new Animation(AnimationsIdEnum.SWORD_ATTACK_2_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SWORD_ATTACK_2_ANIMATION.getAnimationId())));
    public static final Animation SWORD_ATTACK_2_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimationId())));
    public static final Animation SWORD_ATTACK_3_ANIMATION =
            new Animation(AnimationsIdEnum.SWORD_ATTACK_3_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SWORD_ATTACK_3_ANIMATION.getAnimationId())));
    public static final Animation SWORD_ATTACK_3_SNEAK_ANIMATION =
            new Animation(AnimationsIdEnum.SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimationId())));

    // UpHand Animation
    public static final Animation UP_HAND_ANIMATION =
            new Animation(AnimationsIdEnum.UP_HAND_ANIMATION.getAnimationId(),
                    CustomPlayerAnimations.getAnimation(ResourceLocation.fromNamespaceAndPath(CustomPlayerAnimations.MOD_ID, AnimationsIdEnum.UP_HAND_ANIMATION.getAnimationId())));

}
