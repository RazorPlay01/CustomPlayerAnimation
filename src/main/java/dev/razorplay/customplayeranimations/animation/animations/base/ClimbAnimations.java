package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.level.block.*;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.*;
import static java.lang.Math.atan2;
import static java.lang.Math.toDegrees;

public class ClimbAnimations {
    private ClimbAnimations() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (!context.player().onGround() && !context.player().isPassenger()) {
            if (!CONFIG.climbingAnimationsConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                Block block = context.player().clientLevel.getBlockState(context.player().blockPosition()).getBlock();
                if ((block instanceof LadderBlock || block instanceof VineBlock)) {
                    setBodyRotationInLeadderAndVineBlocks(context);
                    playClimbAnimation(context);
                } else if ((block instanceof TwistingVinesPlantBlock
                        || block instanceof WeepingVinesPlantBlock
                        || block instanceof TwistingVinesBlock
                        || block instanceof WeepingVinesBlock
                        || block instanceof ScaffoldingBlock)) {
                    setBodyRotationOnClimbableBlocks(context);
                    playClimbAnimation(context);
                } else if (block instanceof PowderSnowBlock) {
                    if ((String.valueOf(context.player().getArmorSlots())).contains("leather_boots")) {
                        playClimbAnimation(context);
                    }
                }
            }
        }
    }

    private static void playClimbAnimation(AnimationContext context) {
        if (context.player().onClimbable()) {
            context.mainAnimationContainer().setAnimationSpeed(CONFIG.climbingAnimationsConfig.getSpeedMultiplier());
            context.mainAnimationContainer().setAnimationFadeTime(CONFIG.climbingAnimationsConfig.getFadeTime());
            context.mainAnimationContainer().setAnimationPriority(CONFIG.climbingAnimationsConfig.getPriority());
            if (context.playerData().getVectorY() > 0) {
                context.mainAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? CLIMBING_SNEAK_ANIMATION.animation() : CLIMBING_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? CLIMBING_SNEAK_ANIMATION.animationId() : CLIMBING_ANIMATION.animationId());
            } else if (context.playerData().getVectorY() < 0) {
                context.mainAnimationContainer().setCurrentAnimation(CLIMBING_BACKWARDS_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(CLIMBING_BACKWARDS_ANIMATION.animationId());
            } else {
                context.mainAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? CLIMBING_SNEAK_IDLE_ANIMATION.animation() : CLIMBING_IDLE_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? CLIMBING_SNEAK_IDLE_ANIMATION.animationId() : CLIMBING_IDLE_ANIMATION.animationId());
            }
        }
    }

    private static void setBodyRotationInLeadderAndVineBlocks(AnimationContext context) {
        if (!(context.player().getUseItem().getItem() instanceof BowItem)) {
            String blockStateString = String.valueOf(context.player().clientLevel.getBlockState(context.player().blockPosition()));
            context.playerData().setPlayerBodyYaw(context.player().getVisualRotationYInDegrees());
            context.playerData().setPlayerHeadYaw(context.player().getYHeadRot());
            if (blockStateString.contains("facing=north") || blockStateString.contains("south=true")) {
                context.playerData().setPlayerBodyYaw(0);
            } else if (blockStateString.contains("facing=south") || blockStateString.contains("north=true")) {
                context.playerData().setPlayerBodyYaw(180);
            } else if (blockStateString.contains("facing=west") || blockStateString.contains("east=true")) {
                context.playerData().setPlayerBodyYaw(270);
            } else if (blockStateString.contains("facing=east") || blockStateString.contains("west=true")) {
                context.playerData().setPlayerBodyYaw(90);
            }

            context.playerData().setPlayerBodyYaw(((context.playerData().getPlayerBodyYaw() % 360) + 360) % 360);
            context.playerData().setPlayerHeadYaw(((context.playerData().getPlayerHeadYaw() % 360) + 360) % 360);
            context.player().setYBodyRot(context.playerData().getPlayerBodyYaw());
            context.playerData().setPlayerHeadYaw(context.playerData().getPlayerHeadYaw() - context.playerData().getPlayerBodyYaw());
            context.playerData().setPlayerHeadYaw(((context.playerData().getPlayerHeadYaw() % 360) + 360) % 360);

            if (context.playerData().getPlayerHeadYaw() > 90 && context.playerData().getPlayerHeadYaw() <= 180) {
                context.player().setYHeadRot(context.playerData().getPlayerBodyYaw() + 90);
            } else if (context.playerData().getPlayerHeadYaw() > 180 && context.playerData().getPlayerHeadYaw() < 270) {
                context.player().setYHeadRot(context.playerData().getPlayerBodyYaw() + 270);
            }
        }
    }

    private static void setBodyRotationOnClimbableBlocks(AnimationContext context) {
        if (!(context.player().getUseItem().getItem() instanceof BowItem)) {
            context.playerData().setPrevPlayerBodyYaw(((float) toDegrees(atan2((context.player().blockPosition().getZ() + 0.5 - context.playerData().getPlayerPosition().z), (context.player().blockPosition().getX()) + 0.5 - context.playerData().getPlayerPosition().x)) - 90));
            context.playerData().setPlayerHeadYaw(context.player().getYHeadRot());
            context.playerData().setPrevPlayerBodyYaw(((context.playerData().getPlayerBodyYaw() % 360) + 360) % 360);
            context.playerData().setPlayerHeadYaw(((context.playerData().getPlayerHeadYaw() % 360) + 360) % 360);
            context.player().setYBodyRot(context.playerData().getPlayerBodyYaw());
            context.playerData().setPlayerHeadYaw(context.playerData().getPlayerHeadYaw() - context.playerData().getPlayerBodyYaw());
            context.playerData().setPlayerHeadYaw(((context.playerData().getPlayerHeadYaw() % 360) + 360) % 360);

            if (context.playerData().getPlayerHeadYaw() > 90 && context.playerData().getPlayerHeadYaw() <= 180) {
                context.player().setYHeadRot(context.playerData().getPlayerBodyYaw() + 90);
            } else if (context.playerData().getPlayerHeadYaw() > 180 && context.playerData().getPlayerHeadYaw() < 270) {
                context.player().setYHeadRot(context.playerData().getPlayerBodyYaw() + 270);
            }
        }
    }
}
