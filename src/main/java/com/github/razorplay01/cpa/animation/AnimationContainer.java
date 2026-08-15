package com.github.razorplay01.cpa.animation;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.github.razorplay01.cpa.ModTemplate.getAnimation;

//? if >=1.21.1{
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractModifier;
//?}
//? if <1.21.1{
/*import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
*///?}


@Getter
@Setter
@AllArgsConstructor
public class AnimationContainer {
	private final /*? if >=1.21.1 {*/AnimationController/*?} else {*/ /*ModifierLayer<IAnimation>*//*?}*/ animationController;
	private final HashMap<String, AbstractModifier> animationModifiers;
	private /*? if >=1.21.1 {*/Animation/*?} else {*/ /*KeyframeAnimation*//*?}*/ currentAnimation;
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

	public void setCurrentAnimation(/*? if >=1.21.1 {*/Animation/*?} else {*/ /*KeyframeAnimation*//*?}*/ animation) {
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
