package dev.razorplay.customplayeranimations.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.kosmx.playerAnim.api.layered.IActualAnimation;
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
import dev.razorplay.customplayeranimations.compat.CarryOnCompat;
import dev.razorplay.customplayeranimations.compat.OldCombatModCompat;
import dev.razorplay.customplayeranimations.compat.SupplementariesCompat;
import dev.razorplay.customplayeranimations.compat.SwordBlockingCompat;
import dev.razorplay.customplayeranimations.config.ClientConfig;
import dev.razorplay.customplayeranimations.util.ArmsEnum;
import dev.razorplay.customplayeranimations.util.ICustomAnimatedPlayer;
import dev.razorplay.customplayeranimations.util.ITorsoControl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.boss.wither.WitherBoss;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Optional;

import static dev.kosmx.playerAnim.core.util.Ease.INOUTSINE;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.*;
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
    private final ModifierLayer<IAnimation> animationContainer = modAnimationContainer;
    @Unique
    private final ModifierLayer<IAnimation> animationContainer2 = modAnimationContainer2;

    @Unique
    private final SpeedModifier overlaySpeedModifier = new SpeedModifier();
    @Unique
    private final SpeedModifier animationSpeedModifier = new SpeedModifier();

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
    private final AdjustmentModifier rightBowModifier = createBowModifier(true);
    @Unique
    private final AdjustmentModifier leftBowModifier = createBowModifier(false);

    @Unique
    private HumanoidModel.ArmPose mainArmPosition = HumanoidModel.ArmPose.EMPTY;
    @Unique
    private HumanoidModel.ArmPose offArmPosition = HumanoidModel.ArmPose.EMPTY;

    protected AbstractClientPlayerEntityMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(ClientLevel world, GameProfile profile, CallbackInfo info) {
        initializeAnimationLayers();
    }

    @Unique
    private void initializeAnimationLayers() {
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
        Minecraft.getInstance().player.displayClientMessage(Component.literal("Player Speed: " + moveSpeed), false);
        Minecraft.getInstance().player.displayClientMessage(Component.literal("Animation Speed: " + animationSpeed), false);
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

    @Override
    public void disableArmsAnimation(boolean b) {
        this.isDisableArms = b;
    }

    @Unique
    private void disableActiveArm() {
        if (getUsedItemHand().equals(MAIN_HAND)) {
            isDisableMainArmB = true;
        } else {
            isDisableOffArmB = true;
        }
    }

    @Unique
    private void disableArmInBuilder(ArmsEnum arm) {
        var armPart = builder.getPart(arm.getArmId());
        if (armPart != null) {
            armPart.pitch.setEnabled(false);
            armPart.yaw.setEnabled(false);
            armPart.roll.setEnabled(false);
        }
    }

    @Unique
    private AdjustmentModifier createBowModifier(boolean isRight) {
        return new AdjustmentModifier(partName -> {
            float pitch = (float) Math.toRadians(getXRot());
            String mainArm = isRight ? "rightArm" : "leftArm";
            String offArm = isRight ? "leftArm" : "rightArm";

            if (partName.equals(mainArm)) {
                return Optional.of(new AdjustmentModifier.PartModifier(
                        new Vec3f(0, 0, isRight ? -pitch : pitch),
                        new Vec3f(0, pitch, 0))
                );
            } else if (partName.equals(offArm)) {
                return Optional.of(new AdjustmentModifier.PartModifier(
                        new Vec3f(0, 0, isRight ? pitch * 0.25f : -pitch * 0.25f),
                        new Vec3f(0, -pitch, 0))
                );
            }

            return Optional.empty();
        });
    }

    @Unique
    public void animatePlayer() {
        updateHandOrientation();
        updateAnimationSpeeds();
        updatePlayerPosition();
        updateMovementInfo();
        updateEnvironmentInfo();

        disableAnimationOverlay();
        resetOverlayProperties();

        playAnimationSequence();

        if (IS_CARRYON_LOADED) {
            CarryOnCompat.check((AbstractClientPlayer) (Object) this);
        }

        handleArmDisabling();
        updateAnimationContainers();

        lastPlayerPosition = playerPosition;
        prevbyaw = playerBodyYaw;
    }

    @Unique
    private void updateHandOrientation() {
        if (getMainArm() == HumanoidArm.LEFT) {
            rightHand = OFF_HAND;
            leftHand = MAIN_HAND;
        } else {
            rightHand = MAIN_HAND;
            leftHand = OFF_HAND;
        }
    }

    @Unique
    private void updateAnimationSpeeds() {
        animationSpeedModifier.speed = animationSpeed * CONFIG.getAnimationSpeedMultiplier();
        overlaySpeedModifier.speed = overlayAnimationSpeed * CONFIG.getAnimationSpeedMultiplier();
    }

    @Unique
    private void updatePlayerPosition() {
        playerBodyYaw = getVisualRotationYInDegrees();
        playerHeadYaw = getYHeadRot();
        playerPosition = position();
        vectorX = (float) (playerPosition.x - lastPlayerPosition.x);
        vectorY = (float) (playerPosition.y - lastPlayerPosition.y);
        vectorZ = (float) (playerPosition.z - lastPlayerPosition.z);
        moveSpeed = sqrt(vectorX * vectorX + vectorZ * vectorZ);
        bodyYawDelta = playerBodyYaw - prevbyaw;
    }

    @Unique
    private void updateMovementInfo() {
        double bodyYawRadians = toRadians(yBodyRot + 90);
        Vector3f movementVector = new Vector3f(vectorX, 0, vectorZ);
        Vector3f lookVector = new Vector3f((float) cos(bodyYawRadians), 0, (float) sin(bodyYawRadians));
        isMovingBackwards = movementVector.length() > 0 && movementVector.dot(lookVector) < 0;
    }

    @Unique
    private void updateEnvironmentInfo() {
        Block standingBlock = this.level().getBlockState(blockPosition().below()).getBlock();
        isOnFence = (standingBlock instanceof FenceBlock || standingBlock instanceof WallBlock || standingBlock instanceof IronBarsBlock) && onGround();
        isOnEdge = standingBlock instanceof AirBlock && onGround();
    }

    @Unique
    private void resetOverlayProperties() {
        overlayFadeTime = 10;
        overlayAnimationSpeed = 1;
        overlayPriority = 0;
    }

    @Unique
    private void playAnimationSequence() {
        playBaseAnimations();
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
    }

    @Unique
    private void updateAnimationContainers() {
        updateMainAnimationContainer();
        updateOverlayAnimationContainer();
    }

    @Unique
    private void updateOverlayAnimationContainer() {
        if ((!Objects.equals(currentOverlayId, prevOverlayId) && overlayPriority >= prevOverlayPriority) ||
                !animationContainer2.isActive()) {

            rightBowModifier.enabled = false;
            leftBowModifier.enabled = false;

            if (prevOverlayId.contains("trident") && !currentOverlayId.contains("trident")) {
                overlayFadeTime = 3;
            }

            if (prevOverlayId.equals(BLANK_LOOP_ANIMATION.getAnimationId()) &&
                    currentOverlayId.equals(SWORD_ATTACK_1_ANIMATION.getAnimationId()) ||
                    currentOverlayId.equals(SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId())) {
                animationContainer2.setAnimation(null);
                animationContainer2.replaceAnimationWithFade(
                        AbstractFadeModifier.standardFadeIn(overlayFadeTime, INOUTSINE),
                        new KeyframeAnimationPlayer(currentOverlay)
                );
            } else {
                if (!prevOverlayId.equals(currentOverlayId)) {
                    animationContainer2.replaceAnimationWithFade(
                            AbstractFadeModifier.standardFadeIn(overlayFadeTime, INOUTSINE),
                            new KeyframeAnimationPlayer(currentOverlay),
                            true
                    );
                } else {
                    currentOverlayId = BLANK_LOOP_ANIMATION.getAnimationId();
                    currentOverlay = BLANK_LOOP_ANIMATION.getAnimation();
                    animationContainer2.replaceAnimationWithFade(
                            AbstractFadeModifier.standardFadeIn(overlayFadeTime, INOUTSINE),
                            new KeyframeAnimationPlayer(currentOverlay),
                            true
                    );
                }
            }

            prevOverlayId = currentOverlayId;
            prevOverlayPriority = overlayPriority;
        }
    }

    @Unique
    private int currentComboCount = 0;
    @Unique
    private long lastSwingTick = 0;
    @Unique
    private static final int COMBO_RESET_TICKS = 50;

    @Unique
    private void playHandSwingAnimations() {
        if (swinging) {
            //sword attack
            if ((getMainHandItem().getItem() instanceof SwordItem || getMainHandItem().getItem() instanceof TridentItem) && !isUsingItem() && swingingArm.equals(MAIN_HAND)) {
                /*Item swordItem = getMainHandItem().getItem();
                swordItem.components().get(DataComponents.CUSTOM_MODEL_DATA).equals(100);*/

                handleSwordComboAnimation();

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
    private void handleSwordComboAnimation() {
        long currentTick = level().getGameTime();
        if (currentTick - lastSwingTick > COMBO_RESET_TICKS) {
            currentComboCount = 0;
        }

        KeyframeAnimationPlayer currentAnimation = (KeyframeAnimationPlayer) animationContainer2.getAnimation();
        if (currentAnimation.isActive()) {
            if (currentAnimation.getData().getName().equalsIgnoreCase(BLANK_LOOP_ANIMATION.getAnimationId())) {
                if (currentComboCount < 2) {
                    currentComboCount++;
                } else {
                    currentComboCount = 0;
                }
            }
        }


        lastSwingTick = currentTick;

        overlayAnimationSpeed = 1.4f * CONFIG.getSwordAttackAnimationsConfig().getSpeedMultiplier();
        overlayFadeTime = 0;
        overlayPriority = 1;
        overlayMirrorModifier.setEnabled(rightHand != MAIN_HAND);

        selectComboAnimation();

        if (!CONFIG.getSwordAttackAnimationsConfig().isEnabled()) {
            disableAnimationOverlay();
            genericHandswing();
        }
    }

    @Unique
    private void selectComboAnimation() {
        boolean isSneaking = isCrouching();

        switch (currentComboCount) {
            case 1 -> {
                currentOverlay = isSneaking ? SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimation()
                        : SWORD_ATTACK_1_ANIMATION.getAnimation();
                currentOverlayId = isSneaking ? SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId()
                        : SWORD_ATTACK_1_ANIMATION.getAnimationId();
            }
            case 2 -> {
                currentOverlay = isSneaking ? SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimation()
                        : SWORD_ATTACK_2_ANIMATION.getAnimation();
                currentOverlayId = isSneaking ? SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimationId()
                        : SWORD_ATTACK_2_ANIMATION.getAnimationId();
            }
            default -> {
                currentOverlay = isSneaking ? SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimation()
                        : SWORD_ATTACK_3_ANIMATION.getAnimation();
                currentOverlayId = isSneaking ? SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimationId()
                        : SWORD_ATTACK_3_ANIMATION.getAnimationId();
            }
        }
    }

    @Unique
    private void handleArmDisabling() {
        checkMainHandItemForArmDisabling();
        checkOffHandItemForArmDisabling();
        applyArmDisabling();
    }

    @Unique
    private void checkMainHandItemForArmDisabling() {
        if (!getMainHandItem().isEmpty()) {
            // Check if the offhand item is a map
            boolean isMap = getMainHandItem().getItem() instanceof MapItem;
            boolean isCompass = getMainHandItem().getItem() instanceof CompassItem;

            // Check if the arm pose is for a crossbow or bow, and if the offhand item is not a bow
            boolean isCrossbowOrBow = offArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                    offArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE) ||
                    (offArmPosition.equals(HumanoidModel.ArmPose.BOW_AND_ARROW) && !(getMainHandItem().getItem() instanceof BowItem));

            isDisableArms = isCrossbowOrBow || (IS_NEA_LOADED && isMap);
            if (IS_NEA_LOADED && isCompass) {
                disableArm(ArmsEnum.RIGHT_ARM);
            }
        }
    }

    @Unique
    private void checkOffHandItemForArmDisabling() {
        if (!getOffhandItem().isEmpty()) {
            boolean isCrossbowOrBow = offArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                    offArmPosition.equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE) ||
                    (offArmPosition.equals(HumanoidModel.ArmPose.BOW_AND_ARROW) && !(getOffhandItem().getItem() instanceof BowItem));

            isDisableArms = isCrossbowOrBow || IS_NEA_LOADED;
        }
    }

    @Unique
    private void applyArmDisabling() {
        applySpecificArmDisabling(isDisableRightArmB, ArmsEnum.RIGHT_ARM, "disable_right");
        applySpecificArmDisabling(isDisableLeftArmB, ArmsEnum.LEFT_ARM, "disable_left");
        applyHandBasedArmDisabling(isDisableMainArmB, MAIN_HAND, "disable_main");
        applyHandBasedArmDisabling(isDisableOffArmB, OFF_HAND, "disable_off");
        applyBothArmsDisabling();
        applyAnimationDisabling();
    }

    @Unique
    private void applySpecificArmDisabling(boolean shouldDisable, ArmsEnum arm, String modifyIdValue) {
        if (shouldDisable) {
            disableArm(arm);
            modifyId = modifyIdValue;
            fadeTime = !Objects.equals(prevModifyId, modifyId) ? 1 : fadeTime;
        }
    }

    @Unique
    private void applyHandBasedArmDisabling(boolean shouldDisable, InteractionHand hand, String modifyIdValue) {
        if (shouldDisable) {
            disableArmBasedOnHand(hand);
            modifyId = modifyIdValue;
            fadeTime = !Objects.equals(prevModifyId, modifyId) ? 1 : fadeTime;
        }
    }

    @Unique
    private void applyBothArmsDisabling() {
        if (isDisableArms) {
            disableBothArms();
            modifyId = "disable_both";
            fadeTime = !Objects.equals(prevModifyId, modifyId) ? 1 : fadeTime;
        }
    }

    @Unique
    private void applyAnimationDisabling() {
        if (isDisableAnimationB) {
            disableAnimation();
        }
        if (isDisableOverlayB) {
            disableAnimationOverlay();
        }
    }


    @Unique
    private void updateMainAnimationContainer() {
        if ((!Objects.equals(currentAnimationId, prevAnimationId) && priority >= prevPriority) ||
                !animationContainer.isActive() ||
                !Objects.equals(modifyId, prevModifyId)) {

            animationContainer.replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(fadeTime, INOUTSINE),
                    new KeyframeAnimationPlayer(currentAnimation)
            );

            prevAnimationId = currentAnimationId;
            prevModifyId = modifyId;
            prevPriority = priority;
        }
    }


    @Unique
    private void playUseItemAnimations() {
        if (isUsingItem()) {
            Item activeItem = getUseItem().getItem();
            if (activeItem.components().get(DataComponents.FOOD) != null || activeItem instanceof PotionItem) {
                playEatingAnimation();
            } else if (isScoping() || activeItem instanceof InstrumentItem || activeItem instanceof BrushItem) {
                playSpecialItemAnimation();
            } else if (activeItem instanceof TridentItem) {
                playTridentAnimation();
            } else if (activeItem instanceof BowItem) {
                playBowAnimation();
            } else if (activeItem instanceof ShieldItem) {
                playShieldAnimation();
            } else if (activeItem instanceof CrossbowItem) {
                isDisableArms = true;
            } else if (IS_SUPPLEMENTARIES_LOADED) {
                if (SupplementariesCompat.checkFluteItem(activeItem)) {
                    isDisableArms = true;
                }
            }
        }
    }

    @Unique
    private void playEatingAnimation() {
        if (CONFIG.getEatingAnimationsConfig().isEnabled()) {
            overlayFadeTime = 9;
            overlayAnimationSpeed = 1 * CONFIG.getEatingAnimationsConfig().getSpeedMultiplier();
            overlayPriority = 0;

            if (getUsedItemHand().equals(rightHand)) {
                setEatingAnimation(ArmsEnum.RIGHT_ARM, false);
            } else if (getUsedItemHand().equals(leftHand)) {
                setEatingAnimation(ArmsEnum.LEFT_ARM, true);
            }
        }
    }

    @Unique
    private void setEatingAnimation(ArmsEnum arm, boolean mirror) {
        currentOverlay = EATINHG_ANIMATION.getAnimation();
        currentOverlayId = (mirror ? LEFT_PREFIX : RIGHT_PREFIX) + EATINHG_ANIMATION.getAnimationId();
        disableArmOverlayPos(arm);
        overlayMirrorModifier.setEnabled(mirror);
    }

    @Unique
    private void playSpecialItemAnimation() {
        disableActiveArm();
        fadeTime = 1;
        priority = 0;
    }

    @Unique
    private void playTridentAnimation() {
        if (CONFIG.getTridentAnimationConfig().isEnabled()) {
            overlayFadeTime = 5;
            overlayAnimationSpeed = CONFIG.getTridentAnimationConfig().getSpeedMultiplier();
            overlayPriority = 0;

            if (getUsedItemHand().equals(rightHand)) {
                setTridentAnimation(true, 55);
            } else if (getUsedItemHand().equals(leftHand)) {
                setTridentAnimation(false, -55);
            }
        } else {
            disableActiveArm();
            priority = 0;
            fadeTime = 1;
        }
    }

    @Unique
    private void setTridentAnimation(boolean isRightHand, int yawOffset) {
        if (isCrouching()) {
            disableArmOverlayPos(ArmsEnum.RIGHT_ARM);
            disableArmOverlayPos(ArmsEnum.LEFT_ARM);
        } else {
            currentOverlay = TRIDENT_ANIMATION.getAnimation();
            currentOverlayId = (isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + TRIDENT_ANIMATION.getAnimationId();
        }
        setYBodyRot(playerHeadYaw + yawOffset);
        overlayMirrorModifier.setEnabled(!isRightHand);
    }

    @Unique
    private void playBowAnimation() {
        if (isPassenger() || isVisuallyCrawling() || !CONFIG.getBowAnimationsConfig().isEnabled()) {
            disableBowArms();
        } else {
            setBowAnimation();
        }
    }

    @Unique
    private void disableBowArms() {
        disableArm(ArmsEnum.RIGHT_ARM);
        disableArm(ArmsEnum.LEFT_ARM);
        modifyId = "bow_idle";
        fadeTime = 1;
    }

    @Unique
    private void setBowAnimation() {
        overlayFadeTime = 6;
        overlayAnimationSpeed = 1 * CONFIG.getBowAnimationsConfig().getSpeedMultiplier();
        overlayPriority = 0;

        if (getUsedItemHand().equals(rightHand)) {
            setBowAnimationForHand(true);
        } else if (getUsedItemHand().equals(leftHand)) {
            setBowAnimationForHand(false);
        }
    }

    @Unique
    private void setBowAnimationForHand(boolean isRightHand) {
        if (isCrouching()) {
            currentOverlay = BOW_SNEAK_ANIMATION.getAnimation();
            currentOverlayId = (isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + BOW_SNEAK_ANIMATION.getAnimationId();
            overlayFadeTime = 1;
        } else {
            currentOverlay = BOW_IDLE_ANIMATION.getAnimation();
            currentOverlayId = (isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + BOW_IDLE_ANIMATION.getAnimationId();
        }
        disableArmOverlayPos(isRightHand ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);

        if (isRightHand) {
            rightBowModifier.enabled = true;
            setYBodyRot(playerHeadYaw - 90);
        } else {
            leftBowModifier.enabled = true;
            setYBodyRot(playerHeadYaw + 90);
        }
        overlayMirrorModifier.setEnabled(!isRightHand);
    }

    @Unique
    private void playShieldAnimation() {
        if (CONFIG.getShieldAnimationConfig().isEnabled()) {
            overlayFadeTime = 5;
            overlayAnimationSpeed = 1 * CONFIG.getShieldAnimationConfig().getSpeedMultiplier();
            overlayPriority = 0;

            if (getUsedItemHand().equals(rightHand)) {
                setShieldAnimation(true);
            } else if (getUsedItemHand().equals(leftHand)) {
                setShieldAnimation(false);
            }
        } else {
            disableActiveArm();
            priority = 0;
            fadeTime = 1;
        }
    }

    @Unique
    private void setShieldAnimation(boolean isRightHand) {
        if (isCrouching()) {
            currentOverlay = SHIELD_SNEAK_ANIMATION.getAnimation();
            currentOverlayId = (isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + SHIELD_SNEAK_ANIMATION.getAnimationId();
        } else {
            currentOverlay = SHIELD_ANIMATION.getAnimation();
            currentOverlayId = (isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + SHIELD_ANIMATION.getAnimationId();
        }
        disableArmOverlayPos(isRightHand ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);
        overlayMirrorModifier.setEnabled(!isRightHand);
    }


    @Unique
    private void disableArmBasedOnHand(InteractionHand hand) {
        builder = currentAnimation.mutableCopy();
        disableArmInBuilder(hand == rightHand ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);
        currentAnimation = builder.build();
    }


    @Unique
    public void disableArm(ArmsEnum arm) {
        builder = currentAnimation.mutableCopy();
        disableArmInBuilder(arm);
        currentAnimation = builder.build();
    }

    @Unique
    public void disableBothArms() {
        builder = currentAnimation.mutableCopy();
        disableArmInBuilder(ArmsEnum.RIGHT_ARM);
        disableArmInBuilder(ArmsEnum.LEFT_ARM);
        currentAnimation = builder.build();
    }

    @Unique
    public void disableArmOverlayPos(ArmsEnum arm) {
        builder = currentAnimation.mutableCopy();
        var currentArm = builder.getPart(arm.getArmId());
        if (currentArm != null) {
            currentArm.x.setEnabled(false);
            currentArm.y.setEnabled(false);
            currentArm.z.setEnabled(false);
        }
        currentAnimation = builder.build();
    }

    @Unique
    public void disableAnimation() {
        currentAnimation = BLANK_LOOP_ANIMATION.getAnimation();
        currentAnimationId = BLANK_LOOP_ANIMATION.getAnimationId();
    }

    @Unique
    public void disableAnimationOverlay() {
        currentOverlay = BLANK_LOOP_ANIMATION.getAnimation();
        currentOverlayId = BLANK_LOOP_ANIMATION.getAnimationId();
    }

    @Unique
    public void loopedToolAnimation(PlayerAnimations.Animations animation, PlayerAnimations.Animations sneakAnimation, ClientConfig.AnimationConfig config, int fade, float speed, int priority) {
        if (config.isEnabled()) {
            overlayFadeTime = fade;
            overlayAnimationSpeed = speed * config.getSpeedMultiplier();
            overlayPriority = priority;
            overlayMirrorModifier.setEnabled(rightHand != MAIN_HAND);

            currentOverlay = isCrouching() ? sneakAnimation.getAnimation() : animation.getAnimation();
            currentOverlayId = isCrouching() ? sneakAnimation.getAnimationId() : animation.getAnimationId();
        } else {
            genericHandswing();
        }
    }

    @Unique
    public void genericHandswing() {
        disableArmBasedOnHand(swingingArm);
        currentAnimationId = "handswinging" + currentAnimationId;
        modifyId = "handswinging";
        fadeTime = 0;
        priority = 0;
    }

    @Unique
    private void playWalkingAnimation() {
        if (!CONFIG.getWalkingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = CONFIG.getWalkingAnimationConfig().getSpeedMultiplier();
            if (moveSpeed > 0.218) {
                animationSpeed += (float) moveSpeed;
            }

            currentAnimation = WALKING_ANIMATION.getAnimation();
            currentAnimationId = WALKING_ANIMATION.getAnimationId();
        }

        fadeTime = 10;
        priority = 0;
    }

    @Unique
    private void playWalkingBackwardsAnimation() {
        if (!CONFIG.getWalkingBackwardsAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = CONFIG.getWalkingBackwardsAnimationConfig().getSpeedMultiplier();
            if (moveSpeed > 0.218) {
                animationSpeed += (float) moveSpeed;
            }

            currentAnimation = WALKING_BACKWARDS_ANIMATION.getAnimation();
            currentAnimationId = WALKING_BACKWARDS_ANIMATION.getAnimationId();
        }

        fadeTime = 10;
        priority = 0;
    }

    @Unique
    private void playWalkingSneakAnimation() {
        if (!CONFIG.getIdleSneakAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = CONFIG.getWalkingSneakAnimationConfig().getSpeedMultiplier();
            if (moveSpeed > 0.065) {
                animationSpeed += (float) moveSpeed;
            }

            currentAnimation = WALKING_SNEAK_ANIMATION.getAnimation();
            currentAnimationId = WALKING_SNEAK_ANIMATION.getAnimationId();
        }

        if (prevAnimationId.equals(IDLE_SNEAK_ANIMATION.getAnimationId())
                || prevAnimationId.equals(WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId())) {
            fadeTime = 10;
        } else {
            fadeTime = 5;
        }
        priority = 0;
    }

    @Unique
    private void playWalkingSneakBackwardsAnimation() {
        if (!CONFIG.getWalkingSneakBackwardsAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = CONFIG.getWalkingSneakBackwardsAnimationConfig().getSpeedMultiplier();
            if (moveSpeed > 0.065) {
                animationSpeed += (float) moveSpeed;
            }

            currentAnimation = WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimation();
            currentAnimationId = WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId();
        }

        if (prevAnimationId.equals(IDLE_SNEAK_ANIMATION.getAnimationId())
                || prevAnimationId.equals(WALKING_SNEAK_ANIMATION.getAnimationId())) {
            fadeTime = 10;
        } else {
            fadeTime = 5;
        }
        priority = 0;
    }

    @Unique
    private void playRunningAnimation() {
        if (!CONFIG.getRunningAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            animationSpeed = CONFIG.getRunningAnimationConfig().getSpeedMultiplier();
            if (moveSpeed > 0.281) {
                animationSpeed += (float) moveSpeed;
            }

            currentAnimation = RUNNING_ANIMATION.getAnimation();
            currentAnimationId = RUNNING_ANIMATION.getAnimationId();
        }
        fadeTime = 10;
        priority = 0;
    }

    @Unique
    private void playTurnLeftAndRightAnimation() {
        if (!CONFIG.getTurningStandingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            currentAnimation = (bodyYawDelta < 0) ? TURN_LEFT_ANIMATION.getAnimation() : TURN_RIGHT_ANIMATION.getAnimation();
            currentAnimationId = (bodyYawDelta < 0) ? TURN_LEFT_ANIMATION.getAnimationId() : TURN_RIGHT_ANIMATION.getAnimationId();

            if ((((float) 1 / 2) * bodyYawDelta) > 1.5 || (((float) 1 / 2) * bodyYawDelta) < -1.5) {
                animationSpeed = CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier();
            } else {
                animationSpeed = abs((((float) 1 / 2) * bodyYawDelta) * CONFIG.getTurningStandingAnimationConfig().getSpeedMultiplier());
            }
        }

        fadeTime = 10;
        priority = 0;
    }

    @Unique
    private void playIdleStandingAnimation() {
        if (!CONFIG.getIdleStandingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            currentAnimation = IDLE_STANDING_ANIMATION.getAnimation();
            currentAnimationId = IDLE_STANDING_ANIMATION.getAnimationId();

            animationSpeed = CONFIG.getIdleStandingAnimationConfig().getSpeedMultiplier();
        }
        fadeTime = 10;
        priority = 0;
    }

    @Unique
    private void playIdleSneakAnimation() {
        if (!CONFIG.getIdleSneakAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            currentAnimation = IDLE_SNEAK_ANIMATION.getAnimation();
            currentAnimationId = IDLE_SNEAK_ANIMATION.getAnimationId();

            animationSpeed = CONFIG.getIdleStandingAnimationConfig().getSpeedMultiplier();

        }

        if (prevAnimationId.equals(WALKING_SNEAK_ANIMATION.getAnimationId()) || prevAnimationId.equals(WALKING_SNEAK_BACKWARDS_ANIMATION.getAnimationId())) {
            fadeTime = 10;
        } else {
            fadeTime = 5;
        }
        priority = 0;
    }

    @Unique
    private void playFlyIdleCreativeAnimation() {
        if (!CONFIG.getIdleCreativeFlyingAnimationConfig().isEnabled()) {
            disableAnimation();
        } else {
            currentAnimation = IDLE_CREATIVE_FLYING_ANIMATION.getAnimation();
            currentAnimationId = IDLE_CREATIVE_FLYING_ANIMATION.getAnimationId();

            animationSpeed = CONFIG.getIdleCreativeFlyingAnimationConfig().getSpeedMultiplier();
        }
        fadeTime = 10;
        priority = 0;
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
                disableAnimationOverlay();
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
            playFlyIdleCreativeAnimation();
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
            animationSpeed = (float) ((1 / 0.22) * moveSpeed * CONFIG.getOnFenceWalkAnimationConfig().getSpeedMultiplier() * 0.1); // Ajustando por la nueva duración
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
            playIdleSneakAnimation();
        }
    }

    @Unique
    private void playTurningStandingAnimation() {
        if (bodyYawDelta != 0) {
            playTurnLeftAndRightAnimation();
        } else {
            if (isOnFence) {
                currentAnimation = ON_FENCE_IDLE_ANIMATION.getAnimation();
                currentAnimationId = ON_FENCE_IDLE_ANIMATION.getAnimationId();
            }/* else if (isOnEdge) {
                currentAnimation = ON_EDGE_IDLE_ANIMATION.getAnimation();
                currentAnimationId = ON_EDGE_IDLE_ANIMATION.getAnimationId();
            }*/ else {
                playIdleStandingAnimation();
            }


            if (prevAnimationId.equals(IDLE_STANDING_ANIMATION.getAnimationId())
                    || prevAnimationId.equals(WALKING_SNEAK_ANIMATION.getAnimationId())
                    || prevAnimationId.equals(JUMP_ANIMATION.getAnimationId())
            ) {
                fadeTime = 1;
            } else {
                fadeTime = 10;
            }

            priority = 0;
        }
    }
}