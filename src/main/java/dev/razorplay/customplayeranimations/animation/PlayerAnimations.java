package dev.razorplay.customplayeranimations.animation;

import dev.kosmx.playerAnim.api.IPlayable;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.codec.AnimationCodecs;
import dev.razorplay.customplayeranimations.CustomPlayerAnimations;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerAnimations {
    private static final List<KeyframeAnimation> customPlayerAnimationsList = new ArrayList<>();

    public enum Animations {
        BLANK_LOOP_ANIMATION(0, "blank_loop_animation"),
        WALKING_ANIMATION(1, "walking_animation"),
        WALKING_BACKWARDS_ANIMATION(2, "walking_backwards_animation"),
        WALKING_SNEAK_ANIMATION(3, "walking_sneak_animation"),
        WALKING_SNEAK_BACKWARDS_ANIMATION(4, "walking_sneak_backwards_animation"),
        RUNNING_ANIMATION(5, "running_animation"),
        TURN_LEFT_ANIMATION(6, "turn_left_animation"),
        TURN_RIGHT_ANIMATION(7, "turn_right_animation"),
        IDLE_STANDING_ANIMATION(8, "idle_standing_animation"),
        IDLE_SNEAK_ANIMATION(9, "idle_sneak_animation"),
        IDLE_CREATIVE_FLYING_ANIMATION(10, "idle_creative_flying_animation"),
        FALLING_ANIMATION(11, "falling_animation"),
        CLIMBING_ANIMATION(12, "climbing_animation"),
        CLIMBING_BACKWARDS_ANIMATION(13, "climbing_backwards_animation"),
        CLIMBING_SNEAK_ANIMATION(14, "climbing_sneak_animation"),
        CLIMBING_IDLE_ANIMATION(15, "climbing_idle_animation"),
        CLIMBING_SNEAK_IDLE_ANIMATION(16, "climbing_sneak_idle_animation"),
        CRAWLING_ANIMATION(17, "crawling_animation"),
        CRAWLING_BACKWARDS_ANIMATION(18, "crawling_backwards_animation"),
        CRAWLING_IDLE_ANIMATION(19, "crawling_idle_animation"),
        ELYTRA_ANIMATION(20, "elytra_animation"),
        IN_WATER_FORWARD_ANIMATION(21, "in_water_forward_animation"),
        IN_WATER_BACKWARDS_ANIMATION(22, "in_water_backwards_animation"),
        IN_WATER_UP_ANIMATION(23, "in_water_up_animation"),
        IN_WATER_IDLE_ANIMATION(24, "in_water_idle_animation"),
        IN_WATER_SWIMMING_ANIMATION(25, "in_water_swimming_animation"),
        MINECART_IDLE_ANIMATION(26, "minecart_idle_animation"),
        HORSE_IDLE_ANIMATION(27, "horse_idle_animation"),
        HORSE_RUNNING_ANIMATION(28, "horse_running_animation"),
        BOAT_IDLE_ANIMATION(29, "boat_idle_animation"),
        SLEEPING_ANIMATION(30, "sleeping_animation"),
        SWORD_ATTACK_1_ANIMATION(31, "sword_attack_animation"),
        SWORD_ATTACK_2_ANIMATION(32, "sword_attack_animation"),
        SWORD_ATTACK_1_SNEAK_ANIMATION(33, "sword_attack_sneak_animation"),
        SWORD_ATTACK_2_SNEAK_ANIMATION(34, "sword_attack_sneak_animation"),
        PICKAXE_ANIMATION(35, "pickaxe_animation"),
        PICKAXE_SNEAK_ANIMATION(36, "pickaxe_sneak_animation"),
        AXE_ANIMATION(37, "axe_animation"),
        AXE_SNEAK_ANIMATION(38, "axe_sneak_animation"),
        SHOVEL_ANIMATION(39, "shovel_animation"),
        SHOVEL_SNEAK_ANIMATION(40, "shovel_sneak_animation"),
        EATINHG_ANIMATION(41, "eating_animation"),
        TRIDENT_ANIMATION(42, "trident_animation"),
        BOW_IDLE_ANIMATION(43, "bow_idle_animation"),
        BOW_SNEAK_ANIMATION(44, "bow_sneak_animation"),
        SHIELD_ANIMATION(45, "shield_animation"),
        SHIELD_SNEAK_ANIMATION(46, "shield_sneak_animation"),
        BOAT_TURN_LEFT_ANIMATION(47, "boat_turn_left_animation"),
        BOAT_TURN_RIGHT_ANIMATION(48, "boat_turn_right_animation"),
        BOAT_FORWARD_ANIMATION(49, "boat_forward_animation"),
        ON_FENCE_WALKING_ANIMATION(50, "on_fence_walk_animation"),
        ON_FENCE_IDLE_ANIMATION(51, "on_fence_idle_animation"),
        ON_EDGE_IDLE_ANIMATION(52, "on_edge_idle_animation"),
        JUMP_ANIMATION(53, "jump_animation"),
        SWORD_ATTACK_3_ANIMATION(54, "sword_attack_animation"),
        SWORD_ATTACK_3_SNEAK_ANIMATION(55, "sword_attack_sneak_animation"),
        UP_HAND_ANIMATION(56,"up_hand_animation");

        private final int index;
        @Getter
        private final String animationId;

        Animations(int index, String animationId) {
            this.index = index;
            this.animationId = animationId;
        }

        public KeyframeAnimation getAnimation() {
            return customPlayerAnimationsList.get(index);
        }
    }

    public static void loadAnimationsList() {
        customPlayerAnimationsList.clear();
        try {
            InputStream inputStream = Objects.requireNonNull(PlayerAnimations.class.getClassLoader()
                    .getResourceAsStream("assets/custom_player_animations/player_animations/custom_player_animations.json"));
            for (IPlayable animation : AnimationCodecs.deserialize("json", inputStream)) {
                customPlayerAnimationsList.add((KeyframeAnimation) animation);
            }
        } catch (IOException e) {
            CustomPlayerAnimations.LOGGER.error(e.toString());
        }
    }
}