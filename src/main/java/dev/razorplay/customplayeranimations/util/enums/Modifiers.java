package dev.razorplay.customplayeranimations.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Modifiers {
    MIRROR_MODIFIER("MirrorModifier"),
    SPEED_MODIFIER("SpeedModifier"),
    BOW_MODIFIER("BowModifier"),
    SHIELD_MODIFIER("ShieldModifier"),
    ATTACK_MODIFIER("AttackModifier");

    private final String modifierId;
}
