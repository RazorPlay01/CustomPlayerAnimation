package dev.razorplay.customplayeranimations.mixin;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.razorplay.customplayeranimations.animation.PlayerAnimations;
import dev.razorplay.customplayeranimations.config.ClientConfig;
import dev.razorplay.customplayeranimations.util.ArmsEnum;
import dev.razorplay.customplayeranimations.util.ICustomAnimatedPlayer;
import dev.razorplay.customplayeranimations.util.ITorsoControl;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Optional;

import static dev.kosmx.playerAnim.core.util.Ease.INOUTSINE;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.PlayerAnimations.Animations.*;
import static java.lang.Math.*;
import static java.lang.Math.abs;
import static net.minecraft.world.InteractionHand.*;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin extends Player implements ITorsoControl, ICustomAnimatedPlayer {
    @Unique
    private final ModifierLayer<IAnimation> modAnimationContainer = new ModifierLayer<>();
    @Unique
    private final ModifierLayer<IAnimation> modAnimationContainer2 = new ModifierLayer<>();
    @Unique
    private static final String RIGHT_PREFIX = "right_";
    @Unique
    private static final String LEFT_PREFIX = "left_";

    @Unique
    private KeyframeAnimation currentAnimation = null;
    @Unique
    private KeyframeAnimation currentOverlay = null;
    @Unique
    private KeyframeAnimation.AnimationBuilder builder = null;

    @Unique
    private final MirrorModifier overlayMirrorModifier = new MirrorModifier();
    @Unique
    private final MirrorModifier animationMirrorModifier = new MirrorModifier();

    @Unique
    private ModifierLayer<IAnimation> animationContainer = modAnimationContainer;
    @Unique
    private ModifierLayer<IAnimation> animationContainer2 = modAnimationContainer2;

    @Unique
    private SpeedModifier overlaySpeedModifier = new SpeedModifier();
    @Unique
    private SpeedModifier animationSpeedModifier = new SpeedModifier();

    @Unique
    private InteractionHand rightHand = MAIN_HAND;
    @Unique
    private InteractionHand leftHand = OFF_HAND;

    @Unique
    private String currentAnimationId = "";
    @Unique
    private String prevAnimationId = "";
    @Unique
    private String currentOverlayId = "";
    @Unique
    private String prevModifyId = "";
    @Unique
    private String prevOverlayId = "";
    @Unique
    private String modifyId = "";

    @Unique
    private int fadeTime = 0;
    @Unique
    private int priority = 0;
    @Unique
    private int overlayFadeTime = 0;
    @Unique
    private int overlayPriority = 0;
    @Unique
    private int prevPriority = 0;
    @Unique
    private int flychecker = 0;
    @Unique
    private int prevOverlayPriority = 0;
    @Unique
    private float animationSpeed = 1;
    @Unique
    private float overlayAnimationSpeed = 1;
    @Unique
    private float playerBodyYaw = 0;
    @Unique
    private float playerHeadYaw = 0;
    @Unique
    private float vectorX = 0;
    @Unique
    private float vectorY = 0;
    @Unique
    private float vectorZ = 0;
    @Unique
    private float bodyYawDelta = 0;
    @Unique
    private float prevbyaw = 0;

    @Unique
    private double moveSpeed = 0;

    @Unique
    private boolean isMovingBackwards = false;
    @Unique
    private boolean swordSeq = true;
    @Unique
    private boolean isOnFence = false;
    @Unique
    private boolean isOnEdge = false;

    @Unique
    private boolean isDisableArms = false;
    @Unique
    private boolean isDisableLeftArmB = false;
    @Unique
    private boolean isDisableRightArmB = false;
    @Unique
    private boolean isDisableMainArmB = false;
    @Unique
    private boolean isDisableOffArmB = false;
    @Unique
    private boolean isDisableAnimationB = false;
    @Unique
    private boolean isDisableOverlayB = false;

    @Unique
    private Vec3 playerPosition;
    @Unique
    private Vec3 lastPlayerPosition = new Vec3(0, 0, 0);

    @Unique
    private AdjustmentModifier rightBowModifier = new AdjustmentModifier(partName -> {
        float rotationX = 0;
        float rotationY = 0;
        float rotationZ = 0;
        float offsetX = 0;
        float offsetY = 0;
        float offsetZ = 0;
        var pitch = getXRot();
        pitch = (float) Math.toRadians(pitch);
        switch (partName) {
            case "rightArm" -> {
                rotationZ = -pitch;
                offsetY = pitch;
            }
            case "leftArm" -> {
                rotationZ = (float) (-pitch * 0.25);
                offsetY = -pitch;
            }


            default -> {
                return Optional.empty();
            }
        }

        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(rotationX, rotationY, rotationZ),
                new Vec3f(offsetX, offsetY, offsetZ))
        );
    });
    @Unique
    private AdjustmentModifier leftBowModifier = new AdjustmentModifier(partName -> {
        float rotationX = 0;
        float rotationY = 0;
        float rotationZ = 0;
        float offsetX = 0;
        float offsetY = 0;
        float offsetZ = 0;
        var pitch = getXRot();
        pitch = (float) Math.toRadians(pitch);
        switch (partName) {
            case "leftArm" -> {
                rotationZ = pitch;
                offsetY = -pitch;
            }
            case "rightArm" -> {
                rotationZ = (float) (pitch * 0.25);
                offsetY = pitch;
            }


            default -> {
                return Optional.empty();
            }
        }

        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(rotationX, rotationY, rotationZ),
                new Vec3f(offsetX, offsetY, offsetZ))
        );
    });

    @Unique
    private HumanoidModel.ArmPose mainArmPosition = HumanoidModel.ArmPose.EMPTY;
    @Unique
    private HumanoidModel.ArmPose offArmPosition = HumanoidModel.ArmPose.EMPTY;

    protected AbstractClientPlayerEntityMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }


    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(ClientLevel world, GameProfile profile, CallbackInfo info) {
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(1, modAnimationContainer);
        modAnimationContainer.addModifierLast(animationSpeedModifier);
        modAnimationContainer.addModifierLast(animationMirrorModifier);
        animationMirrorModifier.setEnabled(false);


        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(2, modAnimationContainer2);
        modAnimationContainer2.addModifierLast(rightBowModifier);
        rightBowModifier.enabled = false;
        modAnimationContainer2.addModifierLast(leftBowModifier);
        leftBowModifier.enabled = false;

        modAnimationContainer2.addModifierLast(overlaySpeedModifier);
        modAnimationContainer2.addModifierLast(overlayMirrorModifier);
        overlayMirrorModifier.setEnabled(false);

        currentAnimation = IDLE_STANDING_ANIMATION.getAnimation();
        currentOverlay = BLANK_LOOP_ANIMATION.getAnimation();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        animatePlayer();
    }

    @Override
    public ModifierLayer<IAnimation> customPlayerAnimations_getModAnimation() {
        return modAnimationContainer;
    }

    @Override
    public void setMainArmPosition(HumanoidModel.ArmPose pos) {
        this.mainArmPosition = pos;
    }

    @Override
    public void setOffArmPosition(HumanoidModel.ArmPose pos) {
        this.offArmPosition = pos;
    }

    @Unique
    private void disableArmsB() {
        if (getUsedItemHand().equals(MAIN_HAND)) {
            isDisableMainArmB = true;
        } else {
            isDisableOffArmB = true;
        }
    }

    @Unique
    public void disableMainArm() {
        builder = currentAnimation.mutableCopy();
        var rightArm = builder.getPart(ArmsEnum.RIGHT_ARM.getArmId());
        assert rightArm != null;
        var leftArm = builder.getPart(ArmsEnum.LEFT_ARM.getArmId());
        assert leftArm != null;

        if (rightHand == MAIN_HAND) {
            rightArm.pitch.setEnabled(false);
            rightArm.yaw.setEnabled(false);
            rightArm.roll.setEnabled(false);
            currentAnimation = builder.build();
        } else if (leftHand == MAIN_HAND) {
            leftArm.pitch.setEnabled(false);
            leftArm.yaw.setEnabled(false);
            leftArm.roll.setEnabled(false);
            currentAnimation = builder.build();
        }

    }

    @Unique
    public void disableOffArm() {
        builder = currentAnimation.mutableCopy();
        var rightArm = builder.getPart(ArmsEnum.RIGHT_ARM.getArmId());
        assert rightArm != null;
        var leftArm = builder.getPart(ArmsEnum.LEFT_ARM.getArmId());
        assert leftArm != null;


        if (rightHand == OFF_HAND) {
            rightArm.pitch.setEnabled(false);
            rightArm.yaw.setEnabled(false);
            rightArm.roll.setEnabled(false);
            currentAnimation = builder.build();
        } else if (leftHand == OFF_HAND) {
            leftArm.pitch.setEnabled(false);
            leftArm.yaw.setEnabled(false);
            leftArm.roll.setEnabled(false);
            currentAnimation = builder.build();
        }

    }

    @Unique
    public void disableRightArm() {
        builder = currentAnimation.mutableCopy();
        var rightArm = builder.getPart(ArmsEnum.RIGHT_ARM.getArmId());
        assert rightArm != null;
        rightArm.pitch.setEnabled(false);
        rightArm.yaw.setEnabled(false);
        rightArm.roll.setEnabled(false);
        currentAnimation = builder.build();

    }

    @Unique
    public void disableLeftArm() {
        builder = currentAnimation.mutableCopy();
        var leftArm = builder.getPart(ArmsEnum.LEFT_ARM.getArmId());
        assert leftArm != null;
        leftArm.pitch.setEnabled(false);
        leftArm.yaw.setEnabled(false);
        leftArm.roll.setEnabled(false);
        currentAnimation = builder.build();
    }

    @Unique
    public void disableBothArms() {
        builder = currentAnimation.mutableCopy();
        var rightArm = builder.getPart(ArmsEnum.RIGHT_ARM.getArmId());
        assert rightArm != null;
        var leftArm = builder.getPart(ArmsEnum.LEFT_ARM.getArmId());
        assert leftArm != null;
        rightArm.pitch.setEnabled(false);
        rightArm.yaw.setEnabled(false);
        rightArm.roll.setEnabled(false);
        leftArm.pitch.setEnabled(false);
        leftArm.yaw.setEnabled(false);
        leftArm.roll.setEnabled(false);
        currentAnimation = builder.build();
    }

    @Unique
    public void disableRightArmOverlayPos() {
        builder = currentAnimation.mutableCopy();
        var rightArm = builder.getPart(ArmsEnum.RIGHT_ARM.getArmId());
        assert rightArm != null;
        rightArm.x.setEnabled(false);
        rightArm.y.setEnabled(false);
        rightArm.z.setEnabled(false);
        currentAnimation = builder.build();
    }

    @Unique
    public void disableLeftArmOverlayPos() {
        builder = currentAnimation.mutableCopy();
        var leftArm = builder.getPart(ArmsEnum.LEFT_ARM.getArmId());
        assert leftArm != null;
        leftArm.x.setEnabled(false);
        leftArm.y.setEnabled(false);
        leftArm.z.setEnabled(false);
        currentAnimation = builder.build();
    }

    @Unique
    public void disableAnimation() {
        currentAnimation = BLANK_LOOP_ANIMATION.getAnimation();
        currentAnimationId = BLANK_LOOP_ANIMATION.getAnimationId();
    }

    @Unique
    public void disableOverlay() {
        currentOverlay = BLANK_LOOP_ANIMATION.getAnimation();
        currentOverlayId = BLANK_LOOP_ANIMATION.getAnimationId();
    }

    @Unique
    public void loopedToolAnimation(PlayerAnimations.Animations animation, PlayerAnimations.Animations sneakAnimation, ClientConfig.AnimationConfig getconfig, int fade, float speed, int priority) {
        if (getconfig.isEnabled()) {
            overlayFadeTime = fade;
            overlayAnimationSpeed = speed * getconfig.getSpeedMultiplier();
            overlayPriority = priority;
            overlayMirrorModifier.setEnabled(rightHand != MAIN_HAND);

            if (isCrouching()) {
                currentOverlay = sneakAnimation.getAnimation();
                currentOverlayId = sneakAnimation.getAnimationId();
            } else {
                currentOverlay = animation.getAnimation();
                currentOverlayId = animation.getAnimationId();
            }
        } else {
            genericHandswing();
        }
    }

    @Unique
    public void genericHandswing() {
        if (swingingArm.equals(MAIN_HAND)) {
            disableMainArm();
        } else if (swingingArm.equals(OFF_HAND)) {
            disableOffArm();
        }
        currentAnimationId = "handswinging" + currentAnimationId;
        modifyId = "handswinging";
        fadeTime = 0;
        priority = 0;
    }

    @Unique
    public void animatePlayer() {
        if (getMainArm() == HumanoidArm.LEFT) {
            rightHand = OFF_HAND;
            leftHand = MAIN_HAND;
        } else {
            rightHand = MAIN_HAND;
            leftHand = OFF_HAND;
        }

        animationSpeedModifier.speed = animationSpeed * CONFIG.animationSpeedMultiplier;
        overlaySpeedModifier.speed = overlayAnimationSpeed * CONFIG.animationSpeedMultiplier;

        playerBodyYaw = getVisualRotationYInDegrees();
        playerHeadYaw = getYHeadRot();

        playerPosition = position();
        vectorX = (float) (playerPosition.x - lastPlayerPosition.x);
        vectorY = (float) (playerPosition.y - lastPlayerPosition.y);
        vectorZ = (float) (playerPosition.z - lastPlayerPosition.z);
        moveSpeed = sqrt(vectorX * vectorX + vectorZ * vectorZ);
        bodyYawDelta = playerBodyYaw - prevbyaw;
        double bodyYawRadians = toRadians(yBodyRot + 90);
        Vector3f movementVector = new Vector3f(vectorX, 0, vectorZ);
        Vector3f lookVector = new Vector3f((float) cos(bodyYawRadians), 0, (float) sin(bodyYawRadians));
        isMovingBackwards = movementVector.length() > 0 && movementVector.dot(lookVector) < 0;

        Block standingBlock = this.level().getBlockState(blockPosition().below()).getBlock();
        isOnFence = (standingBlock instanceof FenceBlock || standingBlock instanceof WallBlock || standingBlock instanceof StainedGlassPaneBlock) && onGround();
        isOnEdge = standingBlock instanceof AirBlock && onGround();

        disableOverlay();

        overlayFadeTime = 10;
        overlayAnimationSpeed = 1;
        overlayPriority = 0;

        playBaseAnimations();
        //playJumpAnimation();
        playFlyAnimation();
        playFallAnimation();
        playClimbAnimation();
        playCrawlAnimation();
        playInWaterAnimations();
        playRidingAnimations();
        playElytraAnimation();
        playHandSwingAnimations();
        playSleepAnimation();
        playUseItemAnimations();

        /*if (FabricLoader.getInstance().isModLoaded("carryon")) {
            CarryOnData carry = CarryOnDataManager.getCarryData(this);
            if (carry.isCarrying() && !this.isVisuallySwimming() && !this.isFallFlying()) {
                this.disableArms(true);
            }
        }*/

        if (!getMainHandItem().isEmpty()) {
            isDisableArms =
                    mainArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_HOLD)
                            || mainArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE)
                            || (mainArmPosition.equals(HumanoidModel.ArmPose.BOW_AND_ARROW) && !(getOffhandItem().getItem() instanceof BowItem));
        }
        if (!getOffhandItem().isEmpty()) {
            isDisableArms =
                    offArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_HOLD)
                            || offArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE)
                            || (offArmPosition.equals(HumanoidModel.ArmPose.BOW_AND_ARROW) && !(getOffhandItem().getItem() instanceof BowItem));
        }

        if (isDisableRightArmB) {
            disableRightArm();
            isDisableRightArmB = false;
            modifyId = "disable_right";
            if (!Objects.equals(prevModifyId, modifyId)) {
                fadeTime = 1;
            }
        }
        if (isDisableLeftArmB) {
            disableLeftArm();
            isDisableLeftArmB = false;
            modifyId = "disable_left";
            if (!Objects.equals(prevModifyId, modifyId)) {
                fadeTime = 1;
            }
        }
        if (isDisableMainArmB) {
            disableMainArm();
            isDisableMainArmB = false;
            modifyId = "disable_main";
            if (!Objects.equals(prevModifyId, modifyId)) {
                fadeTime = 1;
            }
        }
        if (isDisableOffArmB) {
            disableOffArm();
            isDisableOffArmB = false;
            modifyId = "disable_off";
            if (!Objects.equals(prevModifyId, modifyId)) {
                fadeTime = 1;
            }
        }
        if (isDisableArms) {
            disableBothArms();
            isDisableArms = false;
            modifyId = "disable_both";
            if (!Objects.equals(prevModifyId, modifyId)) {
                fadeTime = 1;
            }

        }
        if (isDisableAnimationB) {
            disableAnimation();
            isDisableAnimationB = false;
        }
        if (isDisableOverlayB) {
            disableOverlay();
            isDisableAnimationB = false;
        }

        if ((!Objects.equals(currentAnimationId, prevAnimationId) && priority >= prevPriority) || !animationContainer.isActive() || !Objects.equals(modifyId, prevModifyId)) {

            animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeTime, INOUTSINE), new KeyframeAnimationPlayer(currentAnimation));

            prevAnimationId = currentAnimationId;
            prevModifyId = modifyId;
            prevPriority = priority;
        }

        lastPlayerPosition = new Vec3(playerPosition.x, playerPosition.y, playerPosition.z);
        prevbyaw = playerBodyYaw;

        if ((!Objects.equals(currentOverlayId, prevOverlayId) && overlayPriority >= prevOverlayPriority) || !animationContainer2.isActive()) {
            rightBowModifier.enabled = false;
            leftBowModifier.enabled = false;
            if (prevOverlayId.contains("trident") && !currentOverlayId.contains("trident")) {
                overlayFadeTime = 3;
            }

            if (currentOverlayId.equals(SWORD_ATTACK_1_ANIMATION.getAnimationId()) || currentOverlayId.equals(SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId())) {
                swordSeq = !swordSeq;
                animationContainer2.setAnimation(null);
                animationContainer2.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(overlayFadeTime, INOUTSINE), new KeyframeAnimationPlayer(currentOverlay));

            } else {
                animationContainer2.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(overlayFadeTime, INOUTSINE), new KeyframeAnimationPlayer(currentOverlay), true);
            }

            prevOverlayId = currentOverlayId;
            prevOverlayPriority = overlayPriority;
        }
    }

    @Unique
    private void playUseItemAnimations() {
        if (isUsingItem()) {
            Item activeItem = getUseItem().getItem();
            if (activeItem.components().get(DataComponents.FOOD) != null) {
                //eating
                if (CONFIG.getEatingAnimationsConfig().isEnabled()) {
                    overlayFadeTime = 9;
                    overlayAnimationSpeed = 1 * CONFIG.getEatingAnimationsConfig().getSpeedMultiplier();
                    overlayPriority = 0;
                    if (getUsedItemHand().equals(rightHand)) {
                        currentOverlay = EATINHG_ANIMATION.getAnimation();
                        currentOverlayId = RIGHT_PREFIX + EATINHG_ANIMATION.getAnimationId();

                        disableRightArmOverlayPos();
                        overlayMirrorModifier.setEnabled(false);
                    } else if (getUsedItemHand().equals(leftHand)) {
                        currentOverlay = EATINHG_ANIMATION.getAnimation();
                        currentOverlayId = LEFT_PREFIX + EATINHG_ANIMATION.getAnimationId();

                        disableLeftArmOverlayPos();
                        overlayMirrorModifier.setEnabled(true);
                    }
                }
            } else if (isScoping() || activeItem instanceof InstrumentItem || activeItem instanceof BrushItem) {
                //goat horn //spyglass  //brush
                disableArmsB();
                fadeTime = 1;
                priority = 0;
            } else if (activeItem instanceof TridentItem) {
                //trident
                if (CONFIG.getTridentAnimationConfig().isEnabled()) {
                    overlayFadeTime = 5;
                    overlayAnimationSpeed = CONFIG.getTridentAnimationConfig().getSpeedMultiplier();
                    overlayPriority = 0;

                    if (getUsedItemHand().equals(rightHand)) {
                        if (isCrouching()) {
                            disableRightArmOverlayPos();
                            disableLeftArmOverlayPos();
                        } else {
                            currentOverlay = TRIDENT_ANIMATION.getAnimation();
                            currentOverlayId = RIGHT_PREFIX + TRIDENT_ANIMATION.getAnimationId();
                        }
                        setYBodyRot(playerHeadYaw + 55);
                        overlayMirrorModifier.setEnabled(false);
                    } else if (getUsedItemHand().equals(leftHand)) {
                        if (isCrouching()) {
                            disableRightArmOverlayPos();
                            disableLeftArmOverlayPos();
                        } else {
                            currentOverlay = TRIDENT_ANIMATION.getAnimation();
                            currentOverlayId = LEFT_PREFIX + TRIDENT_ANIMATION.getAnimationId();
                        }
                        setYBodyRot(playerHeadYaw - 55);
                        overlayMirrorModifier.setEnabled(true);
                    }

                } else {
                    disableArmsB();
                    priority = 0;
                    fadeTime = 1;
                }
            } else if (activeItem instanceof BowItem) {
                //bow
                if (isPassenger() || isVisuallyCrawling() || !CONFIG.getBowAnimationsConfig().isEnabled()) {
                    disableRightArm();
                    disableLeftArm();
                    modifyId = "bow_idle";
                    fadeTime = 1;

                } else {
                    overlayFadeTime = 6;
                    overlayAnimationSpeed = 1 * CONFIG.getBowAnimationsConfig().getSpeedMultiplier();
                    overlayPriority = 0;


                    if (getUsedItemHand().equals(rightHand)) {
                        if (isCrouching()) {
                            currentOverlay = BOW_SNEAK_ANIMATION.getAnimation();
                            currentOverlayId = RIGHT_PREFIX + BOW_SNEAK_ANIMATION.getAnimationId();
                            overlayFadeTime = 1;
                        } else {
                            currentOverlay = BOW_IDLE_ANIMATION.getAnimation();
                            currentOverlayId = RIGHT_PREFIX + BOW_IDLE_ANIMATION.getAnimationId();
                        }
                        disableRightArmOverlayPos();

                        rightBowModifier.enabled = true;
                        this.setYBodyRot(playerHeadYaw - 90);
                        overlayMirrorModifier.setEnabled(false);
                    } else if (getUsedItemHand().equals(leftHand)) {
                        if (isCrouching()) {
                            currentOverlay = BOW_SNEAK_ANIMATION.getAnimation();
                            currentOverlayId = LEFT_PREFIX + BOW_SNEAK_ANIMATION.getAnimationId();
                            overlayFadeTime = 1;
                        } else {
                            currentOverlay = BOW_IDLE_ANIMATION.getAnimation();
                            currentOverlayId = LEFT_PREFIX + BOW_IDLE_ANIMATION.getAnimationId();
                        }
                        disableLeftArmOverlayPos();
                        leftBowModifier.enabled = true;
                        this.setYBodyRot(playerHeadYaw + 90);
                        overlayMirrorModifier.setEnabled(true);
                    }
                }


            } else if (activeItem instanceof ShieldItem) {
                //shield
                if (CONFIG.getShieldAnimationConfig().isEnabled()) {
                    overlayFadeTime = 5;
                    overlayAnimationSpeed = CONFIG.getShieldAnimationConfig().getSpeedMultiplier();
                    overlayPriority = 0;
                    if (getUsedItemHand().equals(rightHand)) {
                        if (isCrouching()) {
                            currentOverlay = SHIELD_SNEAK_ANIMATION.getAnimation();
                            currentOverlayId = RIGHT_PREFIX + SHIELD_SNEAK_ANIMATION.getAnimationId();
                            overlayFadeTime = 1;
                        } else {
                            currentOverlay = SHIELD_ANIMATION.getAnimation();
                            currentOverlayId = RIGHT_PREFIX + SHIELD_ANIMATION.getAnimationId();
                        }

                        overlayMirrorModifier.setEnabled(false);
                    } else if (getUsedItemHand().equals(leftHand)) {
                        if (isCrouching()) {
                            currentOverlay = SHIELD_SNEAK_ANIMATION.getAnimation();
                            currentOverlayId = LEFT_PREFIX + SHIELD_SNEAK_ANIMATION.getAnimationId();
                            overlayFadeTime = 1;
                        } else {
                            currentOverlay = SHIELD_ANIMATION.getAnimation();
                            currentOverlayId = LEFT_PREFIX + SHIELD_ANIMATION.getAnimationId();
                        }
                        overlayMirrorModifier.setEnabled(true);
                    }
                } else {
                    disableArmsB();
                    priority = 0;
                    fadeTime = 1;
                }


            } else if (activeItem instanceof CrossbowItem) {
                //crossbow
                isDisableArms = true;
            } else {
                disableArmsB();
                priority = 0;
            }
        } else {
            modifyId = "";
        }
    }

    @Unique
    private void playHandSwingAnimations() {
        if (swinging) {
            //sword attack
            if ((getMainHandItem().getItem() instanceof SwordItem || getMainHandItem().getItem() instanceof TridentItem) && !isUsingItem() && swingingArm.equals(MAIN_HAND)) {
                overlayAnimationSpeed = 1.4f * CONFIG.getSwordAttackAnimationsConfig().getSpeedMultiplier();

                overlayFadeTime = 0;
                overlayPriority = 1;

                overlayMirrorModifier.setEnabled(rightHand != MAIN_HAND);

                if (swordSeq) {
                    if (isCrouching()) {
                        currentOverlay = SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimation();
                        currentOverlayId = SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId();
                    } else {
                        currentOverlay = SWORD_ATTACK_1_ANIMATION.getAnimation();
                        currentOverlayId = SWORD_ATTACK_1_ANIMATION.getAnimationId();
                    }

                } else {
                    if (isCrouching()) {
                        currentOverlay = SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimation();
                        currentOverlayId = SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimationId();
                    } else {
                        currentOverlay = SWORD_ATTACK_2_ANIMATION.getAnimation();
                        currentOverlayId = SWORD_ATTACK_2_ANIMATION.getAnimationId();
                    }
                }

                if (!CONFIG.getSwordAttackAnimationsConfig().isEnabled()) {
                    disableOverlay();
                    genericHandswing();
                }

                //pickaxe
            } else if (getMainHandItem().getItem() instanceof PickaxeItem && swingingArm.equals(MAIN_HAND)) {
                loopedToolAnimation(PICKAXE_ANIMATION, PICKAXE_SNEAK_ANIMATION, CONFIG.getPickaxeAnimationsConfig(), 1, 2, 0);
                //axe
            } else if (getMainHandItem().getItem() instanceof AxeItem && swingingArm.equals(MAIN_HAND)) {
                loopedToolAnimation(AXE_ANIMATION, AXE_SNEAK_ANIMATION, CONFIG.getAxeAnimationsConfig(), 1, 1.5f, 0);
                //shovel
            } else if (getMainHandItem().getItem() instanceof ShovelItem && swingingArm.equals(MAIN_HAND)) {
                loopedToolAnimation(SHOVEL_ANIMATION, SHOVEL_SNEAK_ANIMATION, CONFIG.getShovelAnimationsConfig(), 1, 1.5f, 0);
            } else {
                genericHandswing();
            }
        }
    }

    @Unique
    private void playSleepAnimation() {
        if (isSleeping()) {
            fadeTime = 2;
            animationSpeed = 2;
            priority = 0;
            disableAnimation();

            currentOverlay = SLEEPING_ANIMATION.getAnimation();
            currentOverlayId = SLEEPING_ANIMATION.getAnimationId();

            if (!CONFIG.getSleepingAnimationsConfig().isEnabled()) {
                disableOverlay();
            }
        }
    }

    @Unique
    private void playElytraAnimation() {
        if (isFallFlying()) {
            currentAnimation = ELYTRA_ANIMATION.getAnimation();
            currentAnimationId = ELYTRA_ANIMATION.getAnimationId();
            if (!CONFIG.getElytraAnimationsConfig().isEnabled()) {
                disableAnimation();
            }
            animationSpeed = 1 * CONFIG.getElytraAnimationsConfig().getSpeedMultiplier();
            priority = 0;
            fadeTime = 5;
        }
    }

    @Unique
    private void playRidingAnimations() {
        if (isPassenger()) {
            var vehicle = getVehicle();
            if (vehicle instanceof Minecart) {
                currentAnimation = MINECART_IDLE_ANIMATION.getAnimation();
                currentAnimationId = MINECART_IDLE_ANIMATION.getAnimationId();
                if (!CONFIG.getMinecartAnimationsConfig().isEnabled()) {
                    disableAnimation();
                }
                animationSpeed = 1 * CONFIG.getMinecartAnimationsConfig().getSpeedMultiplier();
                fadeTime = 2;
                priority = 0;
            } else if (vehicle instanceof Horse
                    || vehicle instanceof SkeletonHorse
                    || vehicle instanceof ZombieHorse
                    || vehicle instanceof Donkey
                    || vehicle instanceof Mule) {
                if (moveSpeed > 0 && !isMovingBackwards) {
                    currentAnimation = HORSE_RUNNING_ANIMATION.getAnimation();
                    currentAnimationId = HORSE_RUNNING_ANIMATION.getAnimationId();
                    if (!CONFIG.getHorseRunningAnimationConfig().isEnabled()) {
                        disableAnimation();
                    }
                } else {
                    fadeTime = 5;
                    currentAnimation = HORSE_IDLE_ANIMATION.getAnimation();
                    currentAnimationId = HORSE_IDLE_ANIMATION.getAnimationId();
                    if (!CONFIG.getHorseIdleAnimationConfig().isEnabled()) {
                        disableAnimation();
                    }
                }
                if (isUsingItem()) {
                    currentAnimation = HORSE_IDLE_ANIMATION.getAnimation();
                    currentAnimationId = HORSE_IDLE_ANIMATION.getAnimationId();
                    if (!CONFIG.getHorseIdleAnimationConfig().isEnabled()) {
                        disableAnimation();
                    }
                }
                animationSpeed = 1 * CONFIG.getHorseRunningAnimationConfig().getSpeedMultiplier();
                fadeTime = 1;
                priority = 0;
            } else if (vehicle instanceof Boat || vehicle instanceof ChestBoat) {
                currentAnimation = BOAT_IDLE_ANIMATION.getAnimation();
                currentAnimationId = BOAT_IDLE_ANIMATION.getAnimationId();

                boolean isLeftPaddleMoving = ((Boat) getVehicle()).getPaddleState(0);
                boolean isRightPaddleMoving = ((Boat) getVehicle()).getPaddleState(1);
                if (moveSpeed > 0 && !isMovingBackwards) {
                    if (isLeftPaddleMoving && isRightPaddleMoving) {
                        currentAnimation = BOAT_FORWARD_ANIMATION.getAnimation();
                        currentAnimationId = BOAT_FORWARD_ANIMATION.getAnimationId();
                    } else if (isLeftPaddleMoving) {
                        currentAnimation = BOAT_TURN_LEFT_ANIMATION.getAnimation();
                        currentAnimationId = BOAT_TURN_LEFT_ANIMATION.getAnimationId();
                    } else if (isRightPaddleMoving) {
                        currentAnimation = BOAT_TURN_RIGHT_ANIMATION.getAnimation();
                        currentAnimationId = BOAT_TURN_RIGHT_ANIMATION.getAnimationId();
                    }
                }

                if (!CONFIG.getBoatAnimationsConfig().isEnabled()) {
                    disableAnimation();
                }
                animationSpeed = 0.7f * CONFIG.getBoatAnimationsConfig().getSpeedMultiplier();
                fadeTime = 1;
                priority = 0;
            } else {
                currentAnimation = HORSE_IDLE_ANIMATION.getAnimation();
                currentAnimationId = HORSE_IDLE_ANIMATION.getAnimationId();
                if (!CONFIG.getHorseIdleAnimationConfig().isEnabled()) {
                    disableAnimation();
                }
                animationSpeed = 1 * CONFIG.getHorseIdleAnimationConfig().getSpeedMultiplier();
                fadeTime = 1;
                priority = 0;
            }
        }
    }

    @Unique
    private void playInWaterAnimations() {
        if ((isInWaterOrBubble() || isInLava()) && !onGround() && !isVisuallySwimming()) {
            if (moveSpeed > 0 && !isMovingBackwards) {
                currentAnimation = IN_WATER_FORWARD_ANIMATION.getAnimation();
                currentAnimationId = IN_WATER_FORWARD_ANIMATION.getAnimationId();
                animationSpeed = 1 * CONFIG.getInWaterAnimationsConfig().getSpeedMultiplier();
                fadeTime = 7;
                priority = 0;
            } else if (moveSpeed > 0) {
                currentAnimation = IN_WATER_BACKWARDS_ANIMATION.getAnimation();
                currentAnimationId = IN_WATER_BACKWARDS_ANIMATION.getAnimationId();
                animationSpeed = 1 * CONFIG.getInWaterAnimationsConfig().getSpeedMultiplier();
                fadeTime = 7;
                priority = 0;
            } else if (vectorY > 0) {
                currentAnimation = IN_WATER_UP_ANIMATION.getAnimation();
                currentAnimationId = IN_WATER_UP_ANIMATION.getAnimationId();
                animationSpeed = 1 * CONFIG.getInWaterAnimationsConfig().getSpeedMultiplier();
                fadeTime = 7;
                priority = 0;
            } else {
                currentAnimation = IN_WATER_IDLE_ANIMATION.getAnimation();
                currentAnimationId = IN_WATER_IDLE_ANIMATION.getAnimationId();
                fadeTime = 5;
                animationSpeed = 1 * CONFIG.getInWaterAnimationsConfig().getSpeedMultiplier();
                priority = 0;
            }
            if (!CONFIG.getInWaterAnimationsConfig().isEnabled()) {
                disableAnimation();
            }
        } else if (isInWaterOrBubble() && isVisuallySwimming()) {
            currentAnimation = IN_WATER_SWIMMING_ANIMATION.getAnimation();
            currentAnimationId = IN_WATER_SWIMMING_ANIMATION.getAnimationId();
            if (!CONFIG.getInWaterAnimationsConfig().isEnabled()) {
                disableAnimation();
            }
            animationSpeed = 1 * CONFIG.getInWaterAnimationsConfig().getSpeedMultiplier();
            fadeTime = 7;
            priority = 0;
        }
    }

    @Unique
    private void playCrawlAnimation() {
        if (isVisuallyCrawling()) {
            if (moveSpeed > 0 && !isMovingBackwards) {
                currentAnimation = CRAWLING_ANIMATION.getAnimation();
                currentAnimationId = CRAWLING_ANIMATION.getAnimationId();
                animationSpeed = (float) ((2 / 0.06) * moveSpeed * CONFIG.getCrawlingAnimationsConfig().getSpeedMultiplier());
                if (animationSpeed < 1) {
                    animationSpeed = 1 * CONFIG.getCrawlingAnimationsConfig().getSpeedMultiplier();
                }
            } else if (moveSpeed > 0) {
                currentAnimation = CRAWLING_BACKWARDS_ANIMATION.getAnimation();
                currentAnimationId = CRAWLING_BACKWARDS_ANIMATION.getAnimationId();
                animationSpeed = (float) ((2 / 0.06) * moveSpeed * CONFIG.getCrawlingAnimationsConfig().getSpeedMultiplier());
                if (animationSpeed < 1) {
                    animationSpeed = 1 * CONFIG.getCrawlingAnimationsConfig().getSpeedMultiplier();
                }
            } else {
                currentAnimation = CRAWLING_IDLE_ANIMATION.getAnimation();
                currentAnimationId = CRAWLING_IDLE_ANIMATION.getAnimationId();
                animationSpeed = 2 * CONFIG.getCrawlingAnimationsConfig().getSpeedMultiplier();
            }
            if (!CONFIG.getCrawlingAnimationsConfig().isEnabled()) {
                disableAnimation();
            }
            fadeTime = 7;
            priority = 0;
        }
    }

    @Unique
    private void playClimbAnimation() {
        if (!onGround() && !isPassenger()) {
            Block block = this.level().getBlockState(blockPosition()).getBlock();
            if ((block instanceof LadderBlock || block instanceof VineBlock)) {
                animationSpeed = 3 * CONFIG.getClimbingAnimationsConfig().getSpeedMultiplier();
                fadeTime = 7;
                priority = 0;
                if (!(getUseItem().getItem() instanceof BowItem)) {
                    String blockStateString = String.valueOf(this.level().getBlockState(blockPosition()));
                    playerBodyYaw = getVisualRotationYInDegrees();
                    playerHeadYaw = getYHeadRot();
                    if (blockStateString.contains("facing=north") || blockStateString.contains("south=true")) {
                        playerBodyYaw = 0;

                    } else if (blockStateString.contains("facing=south") || blockStateString.contains("north=true")) {
                        playerBodyYaw = 180;

                    } else if (blockStateString.contains("facing=west") || blockStateString.contains("east=true")) {
                        playerBodyYaw = 270;

                    } else if (blockStateString.contains("facing=east") || blockStateString.contains("west=true")) {
                        playerBodyYaw = 90;

                    }

                    playerBodyYaw = ((playerBodyYaw % 360) + 360) % 360;
                    playerHeadYaw = ((playerHeadYaw % 360) + 360) % 360;
                    setYBodyRot(playerBodyYaw);
                    playerHeadYaw = playerHeadYaw - playerBodyYaw;
                    playerHeadYaw = ((playerHeadYaw % 360) + 360) % 360;

                    if (playerHeadYaw > 90 && playerHeadYaw <= 180) {
                        setYHeadRot(playerBodyYaw + 90);
                    } else if (playerHeadYaw > 180 && playerHeadYaw < 270) {
                        setYHeadRot(playerBodyYaw + 270);
                    }
                }

                if (onClimbable() && vectorY > 0) {
                    if (isCrouching()) {
                        currentAnimation = CLIMBING_SNEAK_ANIMATION.getAnimation();
                        currentAnimationId = CLIMBING_SNEAK_ANIMATION.getAnimationId();
                    } else {
                        currentAnimation = CLIMBING_ANIMATION.getAnimation();
                        currentAnimationId = CLIMBING_ANIMATION.getAnimationId();
                    }
                } else if (onClimbable() && vectorY < 0) {
                    currentAnimation = CLIMBING_BACKWARDS_ANIMATION.getAnimation();
                    currentAnimationId = CLIMBING_BACKWARDS_ANIMATION.getAnimationId();
                } else if (isCrouching()) {
                    currentAnimation = CLIMBING_SNEAK_IDLE_ANIMATION.getAnimation();
                    currentAnimationId = CLIMBING_SNEAK_IDLE_ANIMATION.getAnimationId();
                } else {
                    currentAnimation = CLIMBING_IDLE_ANIMATION.getAnimation();
                    currentAnimationId = CLIMBING_IDLE_ANIMATION.getAnimationId();
                }
            } else if ((block instanceof TwistingVinesPlantBlock
                    || block instanceof WeepingVinesPlantBlock
                    || block instanceof TwistingVinesBlock
                    || block instanceof WeepingVinesBlock
                    || block instanceof ScaffoldingBlock)
            ) {
                animationSpeed = 3 * CONFIG.getClimbingAnimationsConfig().getSpeedMultiplier();
                fadeTime = 7;
                priority = 0;
                if (!(getUseItem().getItem() instanceof BowItem)) {
                    playerBodyYaw = ((float) toDegrees(atan2((blockPosition().getZ() + 0.5 - playerPosition.z), (blockPosition().getX()) + 0.5 - playerPosition.x)) - 90);
                    playerHeadYaw = getYHeadRot();
                    playerBodyYaw = ((playerBodyYaw % 360) + 360) % 360;
                    playerHeadYaw = ((playerHeadYaw % 360) + 360) % 360;
                    setYBodyRot(playerBodyYaw);
                    playerHeadYaw = playerHeadYaw - playerBodyYaw;
                    playerHeadYaw = ((playerHeadYaw % 360) + 360) % 360;

                    if (playerHeadYaw > 90 && playerHeadYaw <= 180) {
                        setYHeadRot(playerBodyYaw + 90);
                    } else if (playerHeadYaw > 180 && playerHeadYaw < 270) {
                        setYHeadRot(playerBodyYaw + 270);
                    }
                }

                if (onClimbable()) {
                    if (vectorY > 0) {
                        currentAnimation = isCrouching() ? CLIMBING_SNEAK_ANIMATION.getAnimation() : CLIMBING_ANIMATION.getAnimation();
                        currentAnimationId = isCrouching() ? CLIMBING_SNEAK_ANIMATION.getAnimationId() : CLIMBING_ANIMATION.getAnimationId();
                    } else if (vectorY < 0) {
                        currentAnimation = CLIMBING_BACKWARDS_ANIMATION.getAnimation();
                        currentAnimationId = CLIMBING_BACKWARDS_ANIMATION.getAnimationId();
                    } else {
                        currentAnimation = isCrouching() ? CLIMBING_SNEAK_IDLE_ANIMATION.getAnimation() : CLIMBING_IDLE_ANIMATION.getAnimation();
                        currentAnimationId = isCrouching() ? CLIMBING_SNEAK_IDLE_ANIMATION.getAnimationId() : CLIMBING_IDLE_ANIMATION.getAnimationId();
                    }
                } else {
                    currentAnimation = isCrouching() ? CLIMBING_SNEAK_IDLE_ANIMATION.getAnimation() : CLIMBING_IDLE_ANIMATION.getAnimation();
                    currentAnimationId = isCrouching() ? CLIMBING_SNEAK_IDLE_ANIMATION.getAnimationId() : CLIMBING_IDLE_ANIMATION.getAnimationId();
                }

            } else if (block instanceof PowderSnowBlock) {
                fadeTime = 7;
                priority = 0;

                if ((String.valueOf(getArmorSlots())).contains("leather_boots")) {
                    if (vectorY > 0) {
                        if (isCrouching()) {
                            currentAnimation = CLIMBING_SNEAK_ANIMATION.getAnimation();
                            currentAnimationId = CLIMBING_SNEAK_ANIMATION.getAnimationId();
                        } else {
                            currentAnimation = CLIMBING_ANIMATION.getAnimation();
                            currentAnimationId = CLIMBING_ANIMATION.getAnimationId();
                        }
                    } else if (vectorY < 0) {
                        currentAnimation = CLIMBING_BACKWARDS_ANIMATION.getAnimation();
                        currentAnimationId = CLIMBING_BACKWARDS_ANIMATION.getAnimationId();
                    } else if (isCrouching()) {
                        currentAnimation = CLIMBING_SNEAK_IDLE_ANIMATION.getAnimation();
                        currentAnimationId = CLIMBING_SNEAK_IDLE_ANIMATION.getAnimationId();
                    } else {
                        currentAnimation = CLIMBING_IDLE_ANIMATION.getAnimation();
                        currentAnimationId = CLIMBING_IDLE_ANIMATION.getAnimationId();
                    }
                }
            }
            if (!CONFIG.getClimbingAnimationsConfig().isEnabled()) {
                disableAnimation();
            }
        }
    }

    @Unique
    private void playFallAnimation() {
        if (vectorY < -0.6 && !isPassenger() && !onGround()) {
            currentAnimation = FALLING_ANIMATION.getAnimation();
            currentAnimationId = FALLING_ANIMATION.getAnimationId();
            if (!CONFIG.getFallingAnimationConfig().isEnabled()) {
                disableAnimation();
            }
            fadeTime = 8;
            animationSpeed = 1 * CONFIG.getFallingAnimationConfig().getSpeedMultiplier();
            priority = 0;
        } else if (!onGround()) {
            animationSpeed = (currentAnimationId.equals(WALKING_ANIMATION.getAnimationId())) ? 4 : 1;
            priority = 0;
        }
    }

    @Unique
    private void playFlyAnimation() {
        double vyfly = Math.round(vectorY * 1000.0) / 1000.0;
        if ((vyfly == 0.0 || Math.abs(vyfly) == 0.375) && !onGround() && !isInWaterOrBubble()) {
            flychecker++;
        } else if (Math.abs(vyfly) > 0.375 || onGround()) {
            flychecker = 0;
        }

        if (flychecker > 10) {
            currentAnimation = IDLE_CREATIVE_FLYING_ANIMATION.getAnimation();
            currentAnimationId = IDLE_CREATIVE_FLYING_ANIMATION.getAnimationId();
            if (!CONFIG.getIdleCreativeFlyingAnimationConfig().isEnabled()) {
                disableAnimation();
            }
            fadeTime = 5;
            animationSpeed = CONFIG.getIdleCreativeFlyingAnimationConfig().getSpeedMultiplier();
            priority = 0;
        }
    }

    @Unique
    private void playJumpAnimation() {
        if (vectorY > 0 && !onGround()) {
            currentAnimation = JUMP_ANIMATION.getAnimation();
            currentAnimationId = JUMP_ANIMATION.getAnimationId();
            animationSpeed = currentAnimationId.equals(WALKING_ANIMATION.getAnimationId()) ? 4 : 1;
            priority = 0;
        }
    }

    @Unique
    private void playBaseAnimations() {
        if (moveSpeed < 0.23 && moveSpeed > 0 && !isMovingBackwards && !isCrouching()) {
            if (isOnFence) {
                playOnFenceAnimation();
            } else {
                playWalkingAnimation();
            }
        } else if (isMovingBackwards && !isCrouching()) {
            playWalkingBackwardsAnimation();
        } else if (moveSpeed > 0.23 && isSprinting() && !isMovingBackwards && !isCrouching()) {
            playRunningAnimation();
        } else if (moveSpeed == 0 && !isCrouching() && !isSprinting()) {
            playTurningStandingAnimation();
        } else if (isCrouching() && moveSpeed == 0 && !isMovingBackwards) {
            playSneakingAnimation();
        } else if (isCrouching() && moveSpeed > 0 && !isMovingBackwards) {
            playWalkingSneakAnimation();
        } else if (isCrouching() && moveSpeed > 0) {
            playWalkingSneakBackwardsAnimation();
        }
    }

    @Unique
    private void playOnFenceAnimation() {
        if (((3 / 0.22) * moveSpeed) > 1) {
            animationSpeed = (float) ((9 / 0.22) * moveSpeed * CONFIG.getOnFenceWalkAnimationConfig().getSpeedMultiplier() * 0.1); // Ajustando por la nueva duración
        } else {
            animationSpeed = (float) (2 * CONFIG.getOnFenceWalkAnimationConfig().getSpeedMultiplier() * 0.1); // Ajustando por la nueva duración
        }
        currentAnimation = ON_FENCE_WALKING_ANIMATION.getAnimation();
        currentAnimationId = ON_FENCE_WALKING_ANIMATION.getAnimationId();
        if (!CONFIG.getOnFenceWalkAnimationConfig().isEnabled()) {
            playWalkingAnimation();
        }
        fadeTime = 7;
        priority = 0;
    }

    @Unique
    private void playWalkingAnimation() {
        if (((3 / 0.22) * moveSpeed) > 1) {
            animationSpeed = (float) ((9 / 0.22) * moveSpeed * CONFIG.getWalkingAnimationConfig().getSpeedMultiplier());
        } else {
            animationSpeed = 2 * CONFIG.getWalkingAnimationConfig().getSpeedMultiplier();
        }
        currentAnimation = WALKING_ANIMATION.getAnimation();
        currentAnimationId = WALKING_ANIMATION.getAnimationId();
        if (!CONFIG.getWalkingAnimationConfig().isEnabled()) {
            disableAnimation();
        }
        fadeTime = 7;
        priority = 0;
    }

    @Unique
    private void playWalkingBackwardsAnimation() {
        if (((4 / 0.22) * moveSpeed) > 1) {
            animationSpeed = (float) ((4 / 0.22) * moveSpeed * CONFIG.getWalkingBackwardsAnimationConfig().getSpeedMultiplier());
        } else {
            animationSpeed = 2 * CONFIG.getWalkingBackwardsAnimationConfig().getSpeedMultiplier();
        }
        if (vectorY > 0) {
            animationSpeed = 0.1F;
        }
        currentAnimation = WALKING_BACKWARDS_ANIMATION.getAnimation();
        currentAnimationId = WALKING_BACKWARDS_ANIMATION.getAnimationId();
        if (!CONFIG.getWalkingBackwardsAnimationConfig().isEnabled()) {
            disableAnimation();
        }
        fadeTime = 7;
        priority = 0;
    }

    @Unique
    private void playRunningAnimation() {
        if (((3 / 0.28) * moveSpeed) > 1) {
            animationSpeed = (float) ((3 / 0.28) * moveSpeed * CONFIG.getRunningAnimationConfig().getSpeedMultiplier());
        } else {
            animationSpeed = 1 * CONFIG.getRunningAnimationConfig().getSpeedMultiplier();
        }
        currentAnimation = RUNNING_ANIMATION.getAnimation();
        currentAnimationId = RUNNING_ANIMATION.getAnimationId();
        if (!CONFIG.getRunningAnimationConfig().isEnabled()) {
            disableAnimation();
        }
        fadeTime = 2;
        priority = 0;
    }

    @Unique
    private void playTurningStandingAnimation() {
        if (bodyYawDelta != 0) {
            currentAnimation = (bodyYawDelta < 0) ? TURN_LEFT_ANIMATION.getAnimation() : TURN_RIGHT_ANIMATION.getAnimation();
            currentAnimationId = (bodyYawDelta < 0) ? TURN_LEFT_ANIMATION.getAnimationId() : TURN_RIGHT_ANIMATION.getAnimationId();
            priority = 0;

            if (!CONFIG.getTurningStandingAnimationConfig().isEnabled()) {
                disableAnimation();
            }
            if ((abs((((float) 1 / 2) * bodyYawDelta)) > 5)) {
                animationSpeed = 2f * CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier();
            } else {
                animationSpeed = abs((((float) 1 / 2) * bodyYawDelta) * CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier());
            }

        } else {
            if (isOnFence) {
                currentAnimation = ON_FENCE_IDLE_ANIMATION.getAnimation();
                currentAnimationId = ON_FENCE_IDLE_ANIMATION.getAnimationId();
            } else if (isOnEdge) {
                currentAnimation = ON_EDGE_IDLE_ANIMATION.getAnimation();
                currentAnimationId = ON_EDGE_IDLE_ANIMATION.getAnimationId();
            } else {
                currentAnimation = IDLE_STANDING_ANIMATION.getAnimation();
                currentAnimationId = IDLE_STANDING_ANIMATION.getAnimationId();
            }

            if (!CONFIG.getIdleStandingAnimationConfig().isEnabled()) {
                disableAnimation();
            }
            if (prevAnimationId.equals(IDLE_STANDING_ANIMATION.getAnimationId())
                    || prevAnimationId.equals(WALKING_SNEAK_ANIMATION.getAnimationId())
                    || prevAnimationId.equals(JUMP_ANIMATION.getAnimationId())
            ) {
                fadeTime = 1;
            } else {
                fadeTime = 10;
            }
            animationSpeed = CONFIG.getIdleStandingAnimationConfig().getSpeedMultiplier();

            priority = 0;
        }
    }

    @Unique
    private void playSneakingAnimation() {
        if (bodyYawDelta != 0) {
            playWalkingSneakAnimation();
            if (!CONFIG.getTurningStandingAnimationConfig().isEnabled()) {
                disableAnimation();
            }
            if ((abs((((float) 1 / 2) * bodyYawDelta)) > 5)) {
                animationSpeed = 2f * CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier();
            } else {
                animationSpeed = abs((((float) 1 / 2) * bodyYawDelta) * CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier());
            }
        } else {
            currentAnimation = IDLE_SNEAK_ANIMATION.getAnimation();
            currentAnimationId = IDLE_SNEAK_ANIMATION.getAnimationId();
            if (!CONFIG.getIdleStandingAnimationConfig().isEnabled()) {
                disableAnimation();
            }
            if (prevAnimationId.equals(WALKING_SNEAK_ANIMATION.getAnimationId()) || prevAnimationId.equals(WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId())) {
                fadeTime = 10;
            } else {
                fadeTime = 1;
            }
            animationSpeed = CONFIG.getIdleStandingAnimationConfig().getSpeedMultiplier();

            priority = 0;
        }
    }

    @Unique
    private void playWalkingSneakAnimation() {
        currentAnimation = WALKING_SNEAK_ANIMATION.getAnimation();
        currentAnimationId = WALKING_SNEAK_ANIMATION.getAnimationId();
        if (!CONFIG.getIdleSneakAnimationConfig().isEnabled()) {
            disableAnimation();
        }
        animationSpeed = (float) ((2 / 0.06) * moveSpeed * CONFIG.getWalkingSneakAnimationConfig().getSpeedMultiplier());
        if (animationSpeed < 1) {
            animationSpeed = 1 * CONFIG.getWalkingSneakAnimationConfig().getSpeedMultiplier();
        }
        if (prevAnimationId.equals(IDLE_SNEAK_ANIMATION.getAnimationId())
                || prevAnimationId.equals(WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId())) {
            fadeTime = 7;
        } else {
            fadeTime = 1;
        }
        priority = 0;
    }

    @Unique
    private void playWalkingSneakBackwardsAnimation() {
        currentAnimation = WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimation();
        currentAnimationId = WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId();
        if (!CONFIG.getWalkingSneakBackwardsAnimationConfig().isEnabled()) {
            disableAnimation();
        }
        animationSpeed = (float) ((2 / 0.06) * moveSpeed * CONFIG.getWalkingSneakBackwardsAnimationConfig().getSpeedMultiplier());
        if (animationSpeed < 1) {
            animationSpeed = 1 * CONFIG.getWalkingSneakBackwardsAnimationConfig().getSpeedMultiplier();
        }
        if (prevAnimationId.equals(IDLE_SNEAK_ANIMATION.getAnimationId())
                || prevAnimationId.equals(WALKING_SNEAK_ANIMATION.getAnimationId())) {
            fadeTime = 7;
        } else {
            fadeTime = 1;
        }
        priority = 0;
    }
}