package com.github.razorplay01.cpa.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Modifiers {
	MIRROR_MODIFIER("MirrorModifier"),
	SPEED_MODIFIER("SpeedModifier"),
	FIRST_PERSON_MODIFIER("FirstPersonModifier"),
	BOW_MODIFIER("BowModifier"),
	SHIELD_MODIFIER("ShieldModifier"),
	HAND_SWING_MODIFIER("HandSwingModifier"),
	ADJUSTMENT_MODIFIER("AdjustmentModifier");

	private final String modifierId;
}
