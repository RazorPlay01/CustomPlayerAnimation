package dev.razorplay.customplayeranimations.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnimationsId {
    //Base Animation
    BLANK_LOOP_ANIMATION("blank_loop_animation"),
    //Main Animations
    WALKING_ANIMATION("walking_animation"),
    WALKING_BACKWARDS_ANIMATION("walking_backwards_animation"),
    WALKING_SNEAK_ANIMATION("walking_sneak_animation"),
    WALKING_SNEAK_BACKWARDS_ANIMATION("walking_sneak_backwards_animation"),
    RUNNING_ANIMATION("running_animation"),
    TURN_RIGHT_ANIMATION("turn_right_animation"),
    TURN_LEFT_ANIMATION("turn_left_animation"),
    IDLE_STANDING_ANIMATION("idle_standing_animation"),
    IDLE_SNEAK_ANIMATION("idle_sneak_animation"),
    IDLE_CREATIVE_FLYING_ANIMATION("idle_creative_flying_animation"),
    FALLING_ANIMATION("falling_animation"),
    CLIMBING_ANIMATION("climbing_animation"),
    CLIMBING_BACKWARDS_ANIMATION("climbing_backwards_animation"),
    CLIMBING_SNEAK_ANIMATION("climbing_sneak_animation"),
    CLIMBING_IDLE_ANIMATION("climbing_idle_animation"),
    CLIMBING_SNEAK_IDLE_ANIMATION("climbing_sneak_idle_animation"),
    CRAWLING_ANIMATION("crawling_animation"),
    CRAWLING_BACKWARDS_ANIMATION("crawling_backwards_animation"),
    CRAWLING_IDLE_ANIMATION("crawling_idle_animation"),
    ELYTRA_ANIMATION("elytra_animation"),
    IN_WATER_FORWARD_ANIMATION("in_water_forward_animation"),
    IN_WATER_BACKWARDS_ANIMATION("in_water_backwards_animation"),
    IN_WATER_UP_ANIMATION("in_water_up_animation"),
    IN_WATER_IDLE_ANIMATION("in_water_idle_animation"),
    IN_WATER_SWIMMING_ANIMATION("in_water_swimming_animation"),
    MINECART_IDLE_ANIMATION("minecart_idle_animation"),
    HORSE_IDLE_ANIMATION("horse_idle_animation"),
    HORSE_RUNNING_ANIMATION("horse_running_animation"),
    SLEEPING_ANIMATION("sleeping_animation"),
    BOAT_IDLE_ANIMATION("boat_idle_animation"),
    BOAT_TURN_RIGHT_ANIMATION("boat_turn_right_animation"),
    BOAT_TURN_LEFT_ANIMATION("boat_turn_left_animation"),
    BOAT_FORWARD_ANIMATION("boat_forward_animation"),
    ON_FENCE_WALKING_ANIMATION("on_fence_walk_animation"),
    ON_FENCE_IDLE_ANIMATION("on_fence_idle_animation"),
    ON_EDGE_IDLE_ANIMATION("on_edge_idle_animation"),
    JUMP_ANIMATION("jump_animation"),
    //Overlay Animations
    EAT_ANIMATION("eating_animation"),
    PICKAXE_ANIMATION("pickaxe_animation"),
    PICKAXE_SNEAK_ANIMATION("pickaxe_sneak_animation"),
    AXE_ANIMATION("axe_animation"),
    AXE_SNEAK_ANIMATION("axe_sneak_animation"),
    SHOVEL_ANIMATION("shovel_animation"),
    SHOVEL_SNEAK_ANIMATION("shovel_sneak_animation"),
    BOW_IDLE_ANIMATION("bow_idle_animation"),
    BOW_SNEAK_ANIMATION("bow_sneak_animation"),
    SHIELD_ANIMATION("shield_animation"),
    SHIELD_SNEAK_ANIMATION("shield_sneak_animation"),
    TRIDENT_ANIMATION("trident_animation"),
    // Sword Animations
    SWORD_ATTACK_1_ANIMATION("sword_attack_1_animation"),
    SWORD_ATTACK_1_SNEAK_ANIMATION("sword_attack_1_sneak_animation"),
    SWORD_ATTACK_2_ANIMATION("sword_attack_2_animation"),
    SWORD_ATTACK_2_SNEAK_ANIMATION("sword_attack_2_sneak_animation"),
    SWORD_ATTACK_3_ANIMATION("sword_attack_3_animation"),
    SWORD_ATTACK_3_SNEAK_ANIMATION("sword_attack_3_sneak_animation"),
    //UpHand Animation
    UP_HAND_ANIMATION("up_hand_animation"),
    ITEM_SWAP_ANIMATION("item_swap_animation");

    private final String animationId;
}
