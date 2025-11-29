package com.github.razorplay01.cpa.platform.common.animation;

import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractModifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.github.razorplay01.cpa.ModTemplate.getAnimation;

@Getter
@Setter
@AllArgsConstructor
public class AnimationContainer {
	private final AnimationController animationController;
	private final HashMap<String, AbstractModifier> animationModifiers;
	private Animation currentAnimation;
	private String currentAnimationId;
	private String prevAnimationId;
	private int animationFadeTime;
	private int animationPriority;
	private int prevAnimationPriority;
	private float animationSpeed;
	private final Set<String> disabledBoneIds = new HashSet<>();

	public void disableAnimation() {
		this.setCurrentAnimation(getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()));
		this.setCurrentAnimationId(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId());
	}

	public void resetAnimationProperties() {
		this.disableAnimation();
		this.setAnimationFadeTime(10);
		this.setAnimationSpeed(1);
		this.setAnimationPriority(0);
	}

	public static void setAnimation(AnimationContainer animationContainer, AnimationsId animationId) {
		animationContainer.setCurrentAnimation(animationId);
		animationContainer.setCurrentAnimationId(animationId);
	}

	public void setCurrentAnimation(Animation animation) {
		this.currentAnimation = animation;
	}

	public void setCurrentAnimation(AnimationsId animationId) {
		this.setCurrentAnimation(getAnimation(animationId.getAnimationId()));
	}

	public void setCurrentAnimationId(String animation) {
		this.currentAnimationId = animation;
	}

	public void setCurrentAnimationId(AnimationsId animationId) {
		this.setCurrentAnimationId(animationId.getAnimationId());
	}
}
