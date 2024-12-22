package dev.razorplay.customplayeranimations.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BodyParts {
    HEAD("head"),
    TORSO("torso"),
    RIGHT_ARM("rightArm"),
    LEFT_ARM("leftArm"),
    RIGHT_LEG("rightLeg"),
    LEFT_LEG("leftLeg");

    private final String partId;
}
