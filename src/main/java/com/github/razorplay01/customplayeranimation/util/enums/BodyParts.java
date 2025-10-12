package com.github.razorplay01.customplayeranimation.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BodyParts {
    BODY("body"),
    HEAD("head"),
    TORSO("torso"),
    RIGHT_ARM("right_arm"),
    LEFT_ARM("left_arm"),
    RIGHT_LEG("right_leg"),
    LEFT_LEG("left_leg"),
    RIGHT_ITEM("right_item"),
    LEFT_ITEM("left_item"),
    ELYTRA("elytra"),
    CAPE("cape");

    @Getter
    private final String partId;
}
