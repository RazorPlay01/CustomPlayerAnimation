package dev.razorplay.customplayeranimations.util.enums;

import lombok.Getter;

@Getter
public enum ModifiersEnum {
    MIRROR_MODIFIER("MirrorModifier"),
    SPEED_MODIFIER("SpeedModifier"),
    RIGHT_BOW_MODIFIER("RightBowModifier"),
    LEFT_BOW_MODIFIER("LeftBowModifier"),
    RIGHT_UP_HAND_MODIFIER("RightUpHandModifier"),
    LEFT_UP_HAND_MODIFIER("LeftUpHandModifier");

    private final String modifierId;

    ModifiersEnum(String modifierId) {
        this.modifierId = modifierId;
    }
}
