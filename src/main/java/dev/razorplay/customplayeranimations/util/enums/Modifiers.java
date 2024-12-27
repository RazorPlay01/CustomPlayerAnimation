package dev.razorplay.customplayeranimations.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Modifiers {
    MIRROR_MODIFIER("MirrorModifier"),
    SPEED_MODIFIER("SpeedModifier"),
    RIGHT_BOW_MODIFIER("RightBowModifier"),
    LEFT_BOW_MODIFIER("LeftBowModifier"),
    SHIELD_MODIFIER("ShieldModifier"),
    RIGHT_UP_HAND_MODIFIER("RightUpHandModifier"),
    LEFT_UP_HAND_MODIFIER("LeftUpHandModifier");

    private final String modifierId;
}
