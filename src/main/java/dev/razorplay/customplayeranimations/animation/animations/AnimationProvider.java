package dev.razorplay.customplayeranimations.animation.animations;

import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.animation.animations.base.*;
import dev.razorplay.customplayeranimations.animation.animations.overlay.*;
import dev.razorplay.customplayeranimations.animation.animations.special.ItemSwapAnimation;
import dev.razorplay.customplayeranimations.animation.animations.special.UpHandAnimation;

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
            new SleepAnimation()
    );
    public static final List<ICustomAnimation> OVERLAY_ANIMATIONS = List.of(
            new EatAnimation(),
            new TridentAnimation(),
            new BowAnimation(),
            new ShieldAnimation(),
            new CrossbowAnimation(),
            new CompatAnimation(),
            new GenericHandSwingAnimation(),
            new PickaxeAnimation(),
            new AxeAnimation(),
            new ShovelAnimation(),
            new SwordAnimation()
    );
    public static final List<ICustomAnimation> SPECIAL_ANIMATIONS = List.of(
            new UpHandAnimation(),
            new ItemSwapAnimation()
    );

    private AnimationProvider() {
        // []
    }
}
