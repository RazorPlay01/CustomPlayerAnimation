package com.github.razorplay01.cpa.animation.animations;

import com.github.razorplay01.cpa.animation.animations.base.*;
import com.github.razorplay01.cpa.animation.animations.overlay.*;
import com.github.razorplay01.cpa.animation.animations.special.ItemSwapAnimation;
import com.github.razorplay01.cpa.animation.animations.special.UpHandAnimation;
import com.github.razorplay01.cpa.animation.animations.base.*;
import com.github.razorplay01.cpa.animation.animations.overlay.*;
import com.github.razorplay01.cpa.animation.animations.special.*;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;

import java.util.List;

public class AnimationProvider {
	public static final List<ICustomAnimation> MAIN_ANIMATIONS = List.of(
			new IdleStandingAnimation(),
			new IdleSneakAnimation(),
			new OnEdgeIdleAnimation(),
			new OnFenceIdleAnimation(),
			new TurnRigthAnimation(),
			new TurnLeftAnimation(),
			new TurnSneakAnimation(),
			new WalkAnimation(),
			new WalkBackwardsAnimation(),
			new RunAnimation(),
			new OnFenceWalkAnimation(),
			new WalkSneakAnimation(),
			new WalkSneakBackwardsAnimation(),
			new CreativeFlyIdleAnimation(),
			new JumpAnimation(),
			new FallAnimation(),
			new ClimbAnimations(),
			new CrawlAnimations(),
			new InWaterIdleAnimation(),
			new InWaterForwardAnimation(),
			new InWaterBackwardsAnimation(),
			new InWaterUpAnimation(),
			new InWaterDownAnimation(),
			new InWaterSwimAnimation(),
			new MountAnimation(),
			new MinecartAnimation(),
			new HorseIdleAnimation(),
			new HorseRunningAnimation(),
			new HorseRunningBackwardsAnimation(),
			new BoatTurnAnimations(),
			new BoatForwardAnimation(),
			new BoatIdleAnimation(),
			new ElytraAnimation(),
			new SleepAnimation(),
			new DeathAnimation()
	);
	public static final List<ICustomAnimation> OVERLAY_ANIMATIONS = List.of(
			new EatAnimation(),
			new TridentAnimation(),
			new BowAnimation(),
			new ShieldAnimation(),
			new CrossbowAnimation(),
			new GenericHandSwingAnimation(),
			new SwordAnimation(),
			//? if >= 1.21.11 {
			new SpearAnimation(),
			//?}
			new PickaxeAnimation(),
			new AxeAnimation(),
			new ShovelAnimation()

			// Compat Animations
			//new SupplementariesCompatAnimation(),
			//? if >= 1.20 && < 26 {
			/*, new com.github.razorplay01.cpa.animation.animations.compat.CarryOnCompatAnimation()
			*///?}
	);
	public static final List<ICustomAnimation> SPECIAL_ANIMATIONS = List.of(
			new UpHandAnimation(),
			new ItemSwapAnimation()
	);

	private AnimationProvider() {
		// []
	}
}
