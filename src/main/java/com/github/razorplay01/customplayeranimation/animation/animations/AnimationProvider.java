package com.github.razorplay01.customplayeranimation.animation.animations;

import com.github.razorplay01.customplayeranimation.animation.animations.base.*;
import com.github.razorplay01.customplayeranimation.animation.animations.overlay.*;
import com.github.razorplay01.customplayeranimation.animation.animations.special.*;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;

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
            new PickaxeAnimation(),
            new AxeAnimation(),
            new ShovelAnimation()
            //new SwordAnimation()

            // Compat Animations
            //new SupplementariesCompatAnimation(),
            //new CarryOnCompatAnimation()
    );
    public static final List<ICustomAnimation> SPECIAL_ANIMATIONS = List.of(
            new UpHandAnimation(),
            new ItemSwapAnimation()
    );

    private AnimationProvider() {
        // []
    }
}
