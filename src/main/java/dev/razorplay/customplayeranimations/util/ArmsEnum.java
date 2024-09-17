package dev.razorplay.customplayeranimations.util;

public enum ArmsEnum {
    RIGHT_ARM("rightArm"),
    LEFT_ARM("leftArm");

    private final String armId;

    ArmsEnum(String id) {
        armId = id;
    }

    public String getArmId() {
        return this.armId;
    }
}
