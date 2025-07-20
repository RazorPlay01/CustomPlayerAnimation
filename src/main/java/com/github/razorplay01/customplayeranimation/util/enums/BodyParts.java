package com.github.razorplay01.customplayeranimation.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BodyParts {
    BODY("body"),
    HEAD("head"),
    TORSO("torso"),
    RIGHT_ARM("right_arm"),
    LEFT_ARM("left_arm"),
    RIGHT_LEG("right_leg"),
    LEFT_LEG("left_leg"),
    CAPE("cape");

    private final String partId;
}
