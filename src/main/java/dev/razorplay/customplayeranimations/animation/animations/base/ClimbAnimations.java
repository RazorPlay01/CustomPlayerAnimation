package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.level.block.*;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static java.lang.Math.atan2;
import static java.lang.Math.toDegrees;

public class ClimbAnimations {
    private ClimbAnimations() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().onGround() || context.player().isPassenger()) {
            return;
        }

        if (!CONFIG.climbingAnimationsConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            return;
        }

        Block block = getPlayerBlock(context);

        if (isLadderOrVine(block)) {
            handleLadderOrVineAnimation(context);
        } else if (isClimbablePlant(block)) {
            handleClimbablePlantAnimation(context);
        } else if (isPowderSnow(block)) {
            handlePowderSnowAnimation(context);
        }
    }

    private static Block getPlayerBlock(AnimationContext context) {
        return context.player().clientLevel.getBlockState(context.player().blockPosition()).getBlock();
    }

    private static boolean isLadderOrVine(Block block) {
        return block instanceof LadderBlock || block instanceof VineBlock;
    }

    private static boolean isClimbablePlant(Block block) {
        return block instanceof TwistingVinesPlantBlock
                || block instanceof WeepingVinesPlantBlock
                || block instanceof TwistingVinesBlock
                || block instanceof WeepingVinesBlock
                || block instanceof ScaffoldingBlock;
    }

    private static boolean isPowderSnow(Block block) {
        return block instanceof PowderSnowBlock;
    }

    private static void handleLadderOrVineAnimation(AnimationContext context) {
        setBodyRotationInLeadderAndVineBlocks(context);
        playClimbAnimation(context);
    }

    private static void handleClimbablePlantAnimation(AnimationContext context) {
        setBodyRotationOnClimbableBlocks(context);
        playClimbAnimation(context);
    }

    private static void handlePowderSnowAnimation(AnimationContext context) {
        if (hasLeatherBoots(context)) {
            playClimbAnimation(context);
        }
    }

    private static boolean hasLeatherBoots(AnimationContext context) {
        return String.valueOf(context.player().getArmorSlots()).contains("leather_boots");
    }

    public static void playClimbAnimation(AnimationContext context) {
        if (!context.player().onClimbable()) {
            return;
        }
        configureAnimationContainer(context);
        setClimbingAnimation(context);
    }

    private static void configureAnimationContainer(AnimationContext context) {
        var config = CONFIG.climbingAnimationsConfig;
        var animationContainer = context.mainAnimationContainer();

        animationContainer.setAnimationSpeed(config.getSpeedMultiplier());
        animationContainer.setAnimationFadeTime(config.getFadeTime());
        animationContainer.setAnimationPriority(config.getPriority());
    }

    private static void setClimbingAnimation(AnimationContext context) {
        var player = context.player();
        var playerData = context.playerData();
        boolean isCrouching = player.isCrouching();
        double verticalSpeed = playerData.getVectorY();

        if (verticalSpeed > 0) {
            context.mainAnimationContainer().setCurrentAnimation(isCrouching ? getAnimation(AnimationsId.CLIMBING_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.CLIMBING_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(isCrouching ? AnimationsId.CLIMBING_SNEAK_ANIMATION.getAnimationId() : AnimationsId.CLIMBING_ANIMATION.getAnimationId());
        } else if (verticalSpeed < 0) {
            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.CLIMBING_BACKWARDS_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.CLIMBING_BACKWARDS_ANIMATION.getAnimationId());
        } else {
            context.mainAnimationContainer().setCurrentAnimation(isCrouching ? getAnimation(AnimationsId.CLIMBING_SNEAK_IDLE_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.CLIMBING_IDLE_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(isCrouching ? AnimationsId.CLIMBING_SNEAK_IDLE_ANIMATION.getAnimationId() : AnimationsId.CLIMBING_IDLE_ANIMATION.getAnimationId());
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
