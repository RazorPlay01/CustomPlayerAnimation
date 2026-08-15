package com.github.razorplay01.cpa.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BodyParts {
	//? if >=1.21.1{

	HEAD("head"),
	BODY("body"),
	TORSO("torso"),
	RIGHT_ARM("right_arm"),
	LEFT_ARM("left_arm"),
	RIGHT_LEG("right_leg"),
	LEFT_LEG("left_leg"),
	RIGHT_ITEM("right_item"),
	LEFT_ITEM("left_item"),
	//?}
	//? if <1.21.1{
	/*HEAD("head"),
	BODY("body"),
	RIGHT_ARM("rightArm"),
	LEFT_ARM("leftArm"),
	RIGHT_LEG("rightLeg"),
	LEFT_LEG("leftLeg"),
	RIGHT_ITEM("rightItem"),
	LEFT_ITEM("leftItem"),
	*///?}


	ELYTRA("elytra"),
	CAPE("cape");

	@Getter
	private final String partId;
}
